package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.dto.OrdersDto;
import com.fubukiss.rikky.entity.Orders;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    Page<OrdersDto> getUserPage(int page, int pageSize);


    /**
     * 获取管理员订单详情
     *
     * @param page      页码
     * @param pageSize  每页数量
     * @param number    订单号
     * @param beginTime 开始时间
     * @param endTime   结束时间
     */
    Page<OrdersDto> getPage(int page,
                            int pageSize,
                            String number,
                            @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss") Date beginTime,
                            @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss") Date endTime);
}
