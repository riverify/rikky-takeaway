package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.entity.ShoppingCart;

import java.util.List;

/**
 * FileName: ShoppingCartService
 * Date: 2023/01/19
 * Time: 21:12
 * Author: river
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加菜品或套餐到购物车，如果加入同一份，数量累加
     *
     * @param shoppingCart 要添加的菜品
     * @return 添加后的购物车
     */
    ShoppingCart addToCart(ShoppingCart shoppingCart);

    /**
     * 减少菜品或套餐到购物车，如果是最后一份，取消该菜品或套餐
     *
     * @param shoppingCart 要减少的菜品
     * @return 减少后的购物车
     */
    ShoppingCart subInCart(ShoppingCart shoppingCart);

    /**
     * 展示购物车
     *
     * @return 购物车列表
     */
    List<ShoppingCart> showCart();

    /**
     * 清空购物车
     */
    void cleanCart();
}
