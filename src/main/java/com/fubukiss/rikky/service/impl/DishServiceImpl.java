package com.fubukiss.rikky.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.dto.DishDto;
import com.fubukiss.rikky.entity.Dish;
import com.fubukiss.rikky.entity.DishFlavor;
import com.fubukiss.rikky.mapper.DishMapper;
import com.fubukiss.rikky.service.DishFlavorService;
import com.fubukiss.rikky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Project: rikky-takeaway - DishServiceImpl
 * <p>Powered by river On 2023/01/06 11:14 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Service
@Slf4j
@EnableTransactionManagement
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    // ServiceImpl<DishMapper, Dish> 为MyBatis-Plus提供的基础实现类，<DishMapper, Dish> 为泛型，DishMapper为Mapper接口，Dish为实体类


    /**
     * 口味service
     */
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 添加菜品，同时插入口味的数据
     * <p>@Transactional 事务注解，如果添加菜品失败，口味数据也不会添加。需要在启动类上添加@EnableTransactionManagement注解。
     *
     * @param dishDto 菜品数据
     */
    @Transactional
    @Override
    public void saveWithFlavors(DishDto dishDto) {
        // 保存基本信息到菜品表
        this.save(dishDto);
        // 获取菜品id
        Long id = dishDto.getId();  // 此getId是dishDto继承自Dish的方法，返回的是菜品id,需要将dish的id与dishFlavor的dishId关联起来

        // 保存口味信息到口味表(不包括与之[dishFlavor的dishId]匹配的dish的 id)，所以需要在此之前对dishFlavor的dishId进行赋值
//        dishFlavorService.saveBatch(dishDto.getFlavors());  // saveBatch() 为MyBatis-Plus提供的批量插入方法

        // 获取菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        // 使用stream流对口味进行遍历，使用peek()方法对每个口味进行操作，操作内容为将口味的dishId赋值为菜品id
        flavors = flavors.stream().peek(item -> {
            // 对口味的dishId进行赋值
            item.setDishId(id);
        }).collect(Collectors.toList());    // 此时，flavors中的每个dishFlavor的dishId都已经被赋值了

        // 保存口味信息到口味表(包括与之[dishFlavor的dishId]匹配的dish的id)
        dishFlavorService.saveBatch(flavors);  // saveBatch() 为MyBatis-Plus提供的批量插入方法
    }

}
