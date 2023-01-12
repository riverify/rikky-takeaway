package com.fubukiss.rikky.controller;

import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.dto.DishDto;
import com.fubukiss.rikky.service.DishFlavorService;
import com.fubukiss.rikky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Project: rikky-takeaway - DishController 菜品相关的控制类
 * <p>Powered by river On 2023/01/12 2:50 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    /**
     * 菜品服务
     */
    @Autowired
    private DishService dishService;
    /**
     * 菜品口味服务
     */
    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 新增菜品
     *
     * @param dishDto dto: data transfer object，主要用于多表查询时，将查询结果封装成一个对象，方便前端使用，如在本项目的菜品新增中，
     *                前端需要传入菜品的基本信息，以及菜品的口味信息，而菜品和菜品口味是两张表，在后端拥有两个实体类，
     *                所以需要将这两个实体类封装成一个对象。@RequestBody注解用于将前端传入的json数据转换成对象
     * @return 通用返回对象
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品，dishDto: {}", dishDto.toString());       // Slf4j的日志输出
        // 保存菜品
        dishService.saveWithFlavors(dishDto);   // saveWithFlavors方法为非mybatis-plus提供的方法，用于同时保存菜品和菜品口味

        return R.success("新增成功");
    }

}
