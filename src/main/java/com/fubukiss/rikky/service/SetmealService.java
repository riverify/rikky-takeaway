package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.dto.SetmealDto;
import com.fubukiss.rikky.entity.Setmeal;

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
}
