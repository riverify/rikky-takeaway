package com.fubukiss.rikky.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.dto.SetmealDto;
import com.fubukiss.rikky.entity.Category;
import com.fubukiss.rikky.entity.Setmeal;
import com.fubukiss.rikky.service.CategoryService;
import com.fubukiss.rikky.service.SetmealDishService;
import com.fubukiss.rikky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Project: rikky-takeaway - SetmealController 套餐控制器
 * <p>Powered by river On 2023/01/14 5:34 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    /**
     * 套餐服务
     */
    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐菜品服务
     */
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 分类服务
     */
    @Autowired
    private CategoryService categoryService;


    /**
     * <h2>新增套餐<h2/>
     *
     * @param setmealDto 套餐数据传输对象，包含了套餐信息(Setmeal)和套餐菜品信息(SetmealDish List)，@RequestBody注解用于接收前端传递的json数据
     * @return 消息
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐:{}", setmealDto);
        // 新增套餐
        setmealService.saveWithDishes(setmealDto); // 该方法为自定义方法，详见SetmealServiceImpl

        return R.success("添加套餐成功");
    }


    /**
     * <h2>分页查询套餐<h2/>
     * <p>其中菜品的图片由{@link CommonController}提供下载到页面的功能。
     *
     * @param page     前端传入的分页参数，一次性传入当前页码
     * @param pageSize 前端传入的分页参数，一次性传入每页显示的条数
     * @param name     查询条件，如果name为空，则查询所有套餐
     * @return Page对象，mybatis-plus提供的分页对象，包含了分页的所有信息
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 分页构造器
        Page<Setmeal> setmealPage = new Page<>(page, pageSize); // page为当前页码，pageSize为每页显示的条数
        Page<SetmealDto> setmealDtoPage = new Page<>(); // setmealDto比setmeal多了一个categoryName属性，用于存储套餐所属的分类名称，因为只用setmeal对象的Page无法获取到分类名称在前端响应，该page暂时为空，后面会拷贝和填充数据
        // 条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件 where name like '%name%' (如果前端传入name的话)
        queryWrapper.like(name != null, Setmeal::getName, name);    // name != null 为true时，添加查询条件
        // 添加排序条件 order by update_time desc
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 调用分页查询方法
        setmealService.page(setmealPage, queryWrapper); // setmealPage为分页对象，queryWrapper为条件构造器
        // 拷贝分页对象，将setmealPage中的数据拷贝到setmealDtoPage中
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");  // setmealPage为源对象，setmealDtoPage为目标对象，"records"为不需要拷贝的属性，之所以不拷贝records属性，是因为records属性是一个List，而List是引用类型，拷贝的话，两个对象的records属性指向的是同一个List，会导致数据混乱，而且俩个对象的records属性是不同类型的，拷贝会报错
        // records是Page对象中的一个字段，用于存储分页查询的结果，因为setmealDtoPage中的records字段是空的，所以需要手动将setmealDtoPage中的records字段赋值
        List<Setmeal> records = setmealPage.getRecords();

        // 将records中的每一个setmeal对象的categoryId经过查询得到categoryName，然后封装到setmealDto对象中，同时也将其它字段赋给setmealDto对象，返回的是一个List<SetmealDto>对象
        List<SetmealDto> setmealDtoRecordsList = records.stream().map(item -> {
            // 1.new SetmealDto()是为了将Setmeal对象转换为SetmealDto对象，因为SetmealDto中多了一个categoryName属性，用于存储套餐所属的分类名称
            SetmealDto setmealDto = new SetmealDto();
            // 2.先进行setmealDto的普通字段拷贝
            BeanUtils.copyProperties(item, setmealDto);
            // 3.再进行setmealDto的categoryName字段拷贝
            Long categoryId = item.getCategoryId();                     // 获取page里面records的每一个setmeal对象的categoryId
            Category category = categoryService.getById(categoryId);    // 根据categoryId查询category对象
            if (category != null) {                                     // 防止category为空
                String categoryName = category.getName();               // 通过查询的category对象获取categoryName
                setmealDto.setCategoryName(categoryName);               // 将categoryName赋值给dishDto对象的categoryName
            }

            return setmealDto;
        }).collect(Collectors.toList());  // collect方法将stream转换为List，因为setmealDtoRecordsList是一个List对象，所以需要将stream转换为List

        // 将setmealDtoRecordsList赋值给setmealDtoPage的records字段
        setmealDtoPage.setRecords(setmealDtoRecordsList);

        // 返回分页对象
        return R.success(setmealDtoPage);
    }

}
