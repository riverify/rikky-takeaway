package com.fubukiss.rikky.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.dto.OrdersDto;
import com.fubukiss.rikky.entity.Orders;
import com.fubukiss.rikky.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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


    /**
     * <h2>分页查询用户的订单详情<h2/>
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return {@link R}
     */
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(int page, int pageSize) {
        // 获取用户订单分页
        Page<OrdersDto> userPage = ordersService.getUserPage(page, pageSize);

        return R.success(userPage);
    }


    /**
     * <h2>分页查询总订单，如果有订单号或者日期，则同时考虑订单号和日期<h2/>
     *
     * @param page      页码
     * @param pageSize  每页数量
     * @param number    订单号
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return {@link R}
     */
    @GetMapping("/page")
    public R<Page<OrdersDto>> page(
            int page,
            int pageSize,
            String number,
            @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss") Date beginTime,
            @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss") Date endTime) {
        log.info(
                "订单分页查询：page={}，pageSize={}，number={},beginTime={},endTime={}",
                page,
                pageSize,
                number,
                beginTime,
                endTime);
        // 根据以上信息进行分页查询。
        // 创建分页对象
        Page<OrdersDto> pageInfo = ordersService.getPage(page, pageSize, number, beginTime, endTime);

        return R.success(pageInfo);
    }


}
