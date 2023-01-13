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


    /**
     * 更新菜品，同时更新口味的数据
     *
     * @param dishDto 菜品数据
     */
    void updateWithFlavors(DishDto dishDto);

    /**
     * 根据id获得菜品和口味的数据
     *
     * @param id 菜品id
     * @return 菜品数据（包含口味数据）
     */
    DishDto getByIdWithFlavors(Long id);
}
