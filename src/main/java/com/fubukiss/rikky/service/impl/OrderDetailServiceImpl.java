package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.entity.OrderDetail;
import com.fubukiss.rikky.mapper.OrderDetailMapper;
import com.fubukiss.rikky.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * FileName: OrderDetailServiceImpl
 * Date: 2023/01/20
 * Time: 23:58
 * Author: river
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
