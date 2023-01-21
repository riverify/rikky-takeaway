package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.entity.Orders;

/**
 * FileName: OrdersService
 * Date: 2023/01/20
 * Time: 23:51
 * Author: river
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     *
     * @param orders 订单信息
     */
    void submit(Orders orders);


    /**
     * 获取用户订单分页
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 分页数据
     */
    Page<Orders> getUserPage(int page, int pageSize);
}
