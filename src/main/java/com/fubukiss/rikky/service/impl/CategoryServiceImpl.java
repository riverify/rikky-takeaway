package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.common.CustomException;
import com.fubukiss.rikky.entity.Category;
import com.fubukiss.rikky.entity.Dish;
import com.fubukiss.rikky.entity.Setmeal;
import com.fubukiss.rikky.mapper.CategoryMapper;
import com.fubukiss.rikky.service.CategoryService;
import com.fubukiss.rikky.service.DishService;
import com.fubukiss.rikky.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Project: rikky-takeaway - CategoryServiceImpl
 * <p>Powered by Riverify On 01-02-2023 21:44:52
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    // ServiceImpl<CategoryMapper, Category> 为MyBatis-Plus提供的基础实现类，<CategoryMapper, Category> 为泛型，CategoryMapper为Mapper接口，Category为实体类


    // 注入菜品和套餐的Service，用于remove()中使用，判断分类下是否有菜品或套餐
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;


    /**
     * 自定义一个删除删除分类的服务满足特殊业务需求，即在删除之前需要判断该分类下是否有菜品(Dish)或者套餐(Setmeal)，如果有则不能删除该分类
     *
     * @param id 分类id
     */
    @Override
    public void remove(Long id) {
        // 构造Dish查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);     // eq means : where "getCategoryId" = "id"
        int count = dishService.count(dishLambdaQueryWrapper);  // count means : select count(*) from "Dish"
        // 查询当前分类是否关联了菜品，如果关联了则不能删除，抛出一个业务异常
        if (count > 0) {
            throw new CustomException("该分类下存在菜品，故不能删除");
        }

        // 构造Setmeal查询条件（同上）
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        count = setmealService.count(setmealLambdaQueryWrapper);
        // 同理查询分类是否关联了套餐，如果关联了则不能删除，抛出一个业务异常
        if (count > 0) {
            throw new CustomException("该分类下存在套餐，故不能删除");
        }

        // 正常删除分类
        super.removeById(id);
    }
}
