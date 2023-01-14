package com.fubukiss.rikky.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fubukiss.rikky.common.CustomException;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.entity.Category;
import com.fubukiss.rikky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>Project: rikky-takeaway - CategoryController 分类管理的Controller类
 * <p>Powered by Riverify On 01-02-2023 21:48:43
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * <h2>新增菜品分类<h2/>
     *
     * @param category 菜品分类实体类，@RequestBody注解用于接收前端传递的json数据
     * @return 返回R对象，R对象为自定义的返回对象，用于统一返回数据格式
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增菜品分类，category={}", category);
        categoryService.save(category); // 调用Service层的save方法，将category保存到数据库

        return R.success("新增成功");
    }


    /**
     * <h2>显示菜品分类列表<h2/>
     *
     * @param page     前端传递的分页参数
     * @param pageSize 前端传递的分页参数
     * @return 返回R对象，R对象为自定义的返回对象，用于统一返回数据格式，这里返回的是分页对象，在前端页面中，我们需要将分页对象中的数据进行展示
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        // 构造分页构造器
        Page<Category> pageInfo = new Page<>();
        // 构造条件构造器 -- 由于要根据Category的sort字段进行排序，所以需要使用LambdaQueryWrapper
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件 使用sort字段进行排序，asc为升序，desc为降序
        queryWrapper.orderByAsc(Category::getSort); // Category::getSort 为Lambda表达式，等价于Category::getSort()，即调用Category的getSort()方法
        // 调用Service层的page方法，将分页构造器和条件构造器传入，返回分页数据
        categoryService.page(pageInfo, queryWrapper); // 调用Service层的page方法，将分页构造器和条件构造器传入，返回分页数据，得到的数据会自动填充到pageInfo中

        return R.success(pageInfo); // 返回分页数据
    }


    /**
     * <h2>根据id删除菜品分类<h2/>
     * <p>如果分类或套餐中仍有菜品，则不允许删除，抛出{@link CustomException}异常
     *
     * @param id 菜品分类id
     * @return 返回结果
     */
    @DeleteMapping
    public R<String> delete(Long id) {
        log.info("删除菜品分类，id={}", id);
        categoryService.remove(id); // 调用Service层的remove方法，根据id删除菜品分类，这里的remove为非MP自带的方法，是自己在Service层中添加的方法

        return R.success("分类信息删除成功");
    }


    /**
     * <h2>修改菜品分类信息<h2/>
     *
     * @param category 菜品分类实体类，@RequestBody注解用于接收前端传递的json数据
     * @return 返回通用返回结果类
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改菜品分类信息：{}", category);
        categoryService.updateById(category);   // 调用Service层的updateById方法，根据id修改菜品分类信息

        return R.success("修改信息成功");
    }


    /**
     * <h2>根据type获取菜品分类列表或套餐分类列表<h2/>
     *
     * @param category 实体类，用于接收前端传递的参数。在前端需要获取分类信息列表以添加菜品时，实体类内的参数为type = 1（1为菜品，2为套餐）。
     *                 同时本方法也能有其他类似功能，返回为List。由于前段传入的不是json数据，所以不需要使用@RequestBody注解。
     * @return 返回分类的List
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        // 条件构造器 type = ?
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加条件 type = ?
        lambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType()); // 第一个参数为条件，如果为true，则添加条件，否则不添加
        // 添加排序条件 sort asc by 'sort', and then desc by 'update_time'
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        // 调用Service层的list方法，将条件构造器传入，返回分类的List
        List<Category> list = categoryService.list(lambdaQueryWrapper);

        // 返回分类的List
        return R.success(list);
    }


}
