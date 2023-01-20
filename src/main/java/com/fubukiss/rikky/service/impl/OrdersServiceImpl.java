package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.entity.Orders;
import com.fubukiss.rikky.mapper.OrdersMapper;
import com.fubukiss.rikky.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * FileName: OrdersServiceImpl
 * Date: 2023/01/20
 * Time: 23:57
 * Author: river
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
}
