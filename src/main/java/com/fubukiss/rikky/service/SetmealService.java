package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.dto.SetmealDto;
import com.fubukiss.rikky.entity.Setmeal;

import java.util.List;

/**
 * <p>Project: rikky-takeaway - SetmealService
 * <p>Powered by river On 2023/01/06 11:11 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto 套餐数据传输对象
     */
    void saveWithDishes(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     *
     * @param ids 套餐id列表
     */
    void removeWithDishes(List<Long> ids);
}
