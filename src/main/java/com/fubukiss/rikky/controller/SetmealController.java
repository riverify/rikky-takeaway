package com.fubukiss.rikky.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.dto.SetmealDto;
import com.fubukiss.rikky.entity.Category;
import com.fubukiss.rikky.entity.Setmeal;
import com.fubukiss.rikky.entity.SetmealDish;
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


    /**
     * <h2>删除套餐<h2/>
     * <p>相比较在{@link DishController}中的delete方法，这里我们使用List来接收前端传入的ids，并且会先判断是否为在售状态，如果是在售状态，则不能删除。
     * <p>相比较在{@link DishController}中的delete方法，我将这里的逻辑写在service下，不少人肯定会疑惑到底应该把业务逻辑写到controller
     * 还是service，其实这个问题没有绝对的标准，只要你能够清晰的知道你的业务逻辑在哪里，就可以了，我个人的习惯是将业务逻辑写在service下，因为
     * service是业务层，而controller是控制层，controller只负责接收前端的请求，然后将请求转发给service，service处理完业务逻辑后，将结果
     * 返回给controller，controller再将结果返回给前端，这样的逻辑清晰明了，不会混乱。但是将业务逻辑写在controller下，这样也是可以的，什么
     * service，不就是一个服务么，短信服务、API服务、售票服务，总是有一个特定领域才能叫服务，那他表示的就是一类有相同属性（数据、对象等）的
     * 操作集。所以我说极端点说，都可以写在controller里面，因为这样就可以满足了业务需求了。直到你发现某些代码在多个地方用到，且都是相关某个特定
     * 数据或功能的，那你就有了service的概念，那相应的功能代码就可以提炼到service里了。那多做几次，那就基本有感觉，上面的判断在设计阶段，我们
     * 就可以预先有一定的判断了，这就是放service层还是controller层的基础逻辑。
     * <p>项目后期，我会试图更加规范，将业务逻辑写在service下，这样会更加清晰明了，也更加符合面向对象的思想。
     *
     * @param ids 前端传入的ids，可以为多个，多个之间是以逗号分隔的，我们用List来接收，@RequestParam注解是用来接收前端传入的参数
     * @return 返回删除结果
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("被删除套餐ids:{}", ids);
        setmealService.removeWithDishes(ids);

        return R.success("删除套餐成功");
    }


    /**
     * <h2>修改套餐状态<h2/>
     *
     * @param ids    需要被修改的套餐的id，可以是多个，多个之间是以逗号分隔的，我们用List来接收
     * @param status 需要被修改的状态
     * @return 返回修改结果
     */
    @PostMapping("/status/{status}")
    public R<String> changeStatus(@RequestParam List<Long> ids, @PathVariable Integer status) {
        log.info("需要修改状态的套餐ids:{}, 需要修改的状态:{}", ids, status);
        setmealService.changeStatus(ids, status);   // 修改状态

        return R.success("修改套餐状态成功");
    }


    /**
     * <h2>根据id获取某套餐的基本信息和套餐所含菜品<h2/>
     *
     * @param id 套餐id
     * @return 返回套餐传输对象dto，包含套餐基本信息和套餐所含菜品信息
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id) {
        log.info("获取套餐id:{}", id);
        SetmealDto setmealDto = setmealService.getByIdWithDishes(id);

        return R.success(setmealDto);
    }


    /**
     * <h2>修改保存套餐<h2/>
     *
     * @param setmealDto dto: data transfer object，主要用于多表查询时，将查询结果封装成一个对象，方便前端使用，如在本项目的套餐保存中，
     *                   前端需要传入套餐的基本信息，以及套餐下捆绑菜品的信息，而套餐(setmeal)和套餐菜品(setmealDish)是两张表，在后端拥有两个实体类，
     *                   所以需要将这两个实体类封装成一个对象。@RequestBody注解用于将前端传入的json数据转换成对象
     * @return 通用返回
     */
    @PutMapping
    public R<String> put(@RequestBody SetmealDto setmealDto) {
        log.info("修改套餐:{}", setmealDto);
        // 保存套餐
        setmealService.updateWithDishes(setmealDto);

        return R.success("修改套餐成功");
    }


    /**
     * <h2>前台查询套餐和套餐的内容<h2/>
     *
     * @param setmeal 前端传入的查询条件
     * @return 返回查询结果
     */
    @GetMapping("/list")
    public R<List<SetmealDto>> list(Setmeal setmeal) {
        log.info("前台查询套餐:{}", setmeal);
        // 创建构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件 where category_id = ? and status = 1 order by update_time desc
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId())
                .eq(Setmeal::getStatus, 1)
                .orderByDesc(Setmeal::getUpdateTime);
        // 查询套餐基本信息
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        // 查询套餐所含菜品信息
        List<SetmealDto> setmealDtoList = setmealList.stream().map(item -> {
            // 创建套餐dto 包含套餐基本信息和套餐所含菜品信息
            SetmealDto setmealDto = new SetmealDto();
            // 设置套餐基本信息
            BeanUtils.copyProperties(item, setmealDto);
            // 查询套餐所含菜品信息
            LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>(); // select * from setmeal_dish
            setmealDishQueryWrapper.eq(SetmealDish::getSetmealId, item.getId()); // where setmeal_id = ?
            // 查询套餐所含菜品信息
            List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishQueryWrapper);
            // 加入套餐dto
            setmealDto.setSetmealDishes(setmealDishList);

            return setmealDto;
        }).collect(Collectors.toList());

        return R.success(setmealDtoList);
    }


    // todo: unfinished checking setmeal details
    @GetMapping("/dish/{id}")
    public R<SetmealDto> dish(@PathVariable Long id) {
        log.info("前台查询套餐:{}", id);
        // 查询套餐基本信息
        SetmealDto dishes = setmealService.getByIdWithDishes(id);

        return R.success(dishes);
    }
}
