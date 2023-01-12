package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.dto.DishDto;
import com.fubukiss.rikky.entity.Dish;


/**
 * <p>Project: rikky-takeaway - DishService 菜品Service
 * <p>Powered by river On 2023/01/06 11:10 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
public interface DishService extends IService<Dish> {

    /**
     * 添加菜品，同时插入口味的数据
     *
     * @param dishDto 菜品数据
     */
    void saveWithFlavors(DishDto dishDto);
}
