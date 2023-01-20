package com.fubukiss.rikky.controller;


import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.entity.ShoppingCart;
import com.fubukiss.rikky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FileName: ShoppingCartController
 * Date: 2023/01/19
 * Time: 21:19
 * Author: river
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    /**
     * 购物车服务
     */
    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * <h2>查看购物车<h2/>
     *
     * @return 购物车列表
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车");
        List<ShoppingCart> shoppingCarts = shoppingCartService.showCart();

        return R.success(shoppingCarts);
    }


    /**
     * <h2>添加某个菜品或套餐到购物车<h2/>
     *
     * @param shoppingCart 要添加到购物车的菜品，@RequestBody是从json中获取数据
     * @return 购物车类
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加购物车:{}", shoppingCart);     // 打印日志
        ShoppingCart shoppingCartOne = shoppingCartService.addToCart(shoppingCart);// 添加到购物车

        return R.success(shoppingCartOne);
    }

    /**
     * <h2>减少某个菜品或套餐到购物车<h2/>
     *
     * @param shoppingCart 要减少的菜品，@RequestBody是从json中获取数据
     * @return 购物车类
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车:{}", shoppingCart);     // 打印日志
        ShoppingCart shoppingCartOne = shoppingCartService.subInCart(shoppingCart);// 添加到购物车

        return R.success(shoppingCartOne);
    }


    /**
     * <h2>清空购物车<h2/>
     *
     * @return 消息
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        log.info("清空购物车");
        shoppingCartService.cleanCart();

        return R.success("清空购物车成功");
    }

}
