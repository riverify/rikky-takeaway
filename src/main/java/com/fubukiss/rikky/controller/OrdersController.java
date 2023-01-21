package com.fubukiss.rikky.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.entity.Orders;
import com.fubukiss.rikky.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * FileName: OrdersController
 * Date: 2023/01/21
 * Time: 11:07
 * Author: river
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    /**
     * 订单服务
     */
    @Autowired
    private OrdersService ordersService;


    /**
     * <h2>提交订单<h2/>
     *
     * @param orders 订单信息
     * @return {@link R}
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("submit order: {}", orders);
        ordersService.submit(orders);
        return R.success("订单提交成功");
    }


    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        // 获取用户订单分页
        Page<Orders> userPage = ordersService.getUserPage(page, pageSize);

        return R.success(userPage);
    }


}
