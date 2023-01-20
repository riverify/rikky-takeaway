package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.common.BaseContext;
import com.fubukiss.rikky.entity.ShoppingCart;
import com.fubukiss.rikky.mapper.ShoppingCartMapper;
import com.fubukiss.rikky.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FileName: ShoppingCartServiceImpl 购物车服务实现类
 * Date: 2023/01/19
 * Time: 21:13
 * Author: river
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    /**
     * 添加菜品或套餐 到购物车，如果加入同一份，数量累加
     *
     * @param shoppingCart 要添加的菜品
     * @return 添加后的购物车
     */
    public ShoppingCart addToCart(ShoppingCart shoppingCart) {

        // 设置用户id，指定当前是哪个用户的购物车
        long UserId = BaseContext.getCurrentId();
        shoppingCart.setUserId(UserId);

        // 查询当前菜品或套餐是否在购物车中，菜品口味要相同
        Long dishId = shoppingCart.getDishId(); // 查询前端传来的菜品id

        //　构造查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, UserId); // where user_id = ?

        // 如果能查到dishId，说明购物车传来的是菜品，否则是套餐
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId); // where dish_id = ?
//            queryWrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor()); // where dish_flavor = ?
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId()); // where set_meal_id = ?
        }

        ShoppingCart one = this.getOne(queryWrapper);

        // 如果在购物车中，数量累加
        if (one != null) {
            Integer number = one.getNumber();
            one.setNumber(number + 1); // 数量加一
            one.setCreateTime(LocalDateTime.now());
            this.updateById(one);
        } else {
            // 如果不在购物车中，添加到购物车
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            one = shoppingCart;        // 保存后，one就是shoppingCart
        }

        return one;
    }


    /**
     * 减少菜品或套餐到购物车，如果是最后一份，取消该菜品或套餐
     *
     * @param shoppingCart 要减少的菜品
     * @return 减少后的购物车
     */
    public ShoppingCart subInCart(ShoppingCart shoppingCart) {
        // 设置用户id，指定当前是哪个用户的购物车
        long UserId = BaseContext.getCurrentId();
        shoppingCart.setUserId(UserId);

        // 查询当前菜品或套餐是否在购物车中，菜品口味要相同
        Long dishId = shoppingCart.getDishId(); // 查询前端传来的菜品id

        //　构造查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, UserId); // where user_id = ?

        // 如果能查到dishId，说明购物车传来的是菜品，否则是套餐
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId); // where dish_id = ?
//            queryWrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor()); // where dish_flavor = ?
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId()); // where set_meal_id = ?
        }

        ShoppingCart one = this.getOne(queryWrapper);

        // 如果在购物车中大于２，数量减少
        if (one.getNumber() >= 2) {
            Integer number = one.getNumber();
            one.setNumber(number - 1);
            this.updateById(one);
        } else {
            one.setNumber(0);   // 如果是最后一份，数量设置为０，这样前端展示的时候就不会显示了
            this.remove(queryWrapper);
        }

        return one;
    }


    /**
     * 查询购物车
     *
     * @return 购物车列表
     */
    @Override
    public List<ShoppingCart> showCart() {
        // 创建条件构造器
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        // 设置查询条件
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());   // BaseContext.getCurrentId()获取当前用户id
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime); // 按照创建时间升序排列
        return this.list(queryWrapper);
    }


    /**
     * 清空购物车
     */
    @Override
    public void cleanCart() {
        // 条件构造器
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        this.remove(queryWrapper);
    }
}
