package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.common.BaseContext;
import com.fubukiss.rikky.common.CustomException;
import com.fubukiss.rikky.dto.OrdersDto;
import com.fubukiss.rikky.entity.*;
import com.fubukiss.rikky.mapper.OrdersMapper;
import com.fubukiss.rikky.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * FileName: OrdersServiceImpl
 * Date: 2023/01/20
 * Time: 23:57
 * Author: river
 */
@Service
@EnableTransactionManagement
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    /**
     * 购物车服务
     */
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 用户服务
     */
    @Autowired
    private UserService userService;

    /**
     * 地址本数据服务
     */
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 订单商品服务
     */
    @Autowired
    private OrderDetailService orderDetailService;


    /**
     * 用户下单
     * <p> @Transactional 注解会在方法执行前开启事务，执行完毕后提交事务
     *
     * @param orders 订单信息
     */
    @Transactional
    public void submit(Orders orders) {
        // 获取当前用户id
        long currentId = BaseContext.getCurrentId();
        // 查询当前用户购物车数据
        LambdaQueryWrapper<ShoppingCart> cartQueryWrapper = new LambdaQueryWrapper<>();
        cartQueryWrapper.eq(ShoppingCart::getUserId, currentId);    // where user_id = currentId
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(cartQueryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空");
        }
        // 查询用户数据
        User user = userService.getById(currentId);
        // 查询地址数据
        Long addressBookId = orders.getAddressBookId();  // 获取发送订单时使用的地址id
        AddressBook addressBook = addressBookService.getById(addressBookId); // 根据地址id查询地址数据
        if (addressBook == null) {
            throw new CustomException("地址不存在");
        }

        long orderId = IdWorker.getId();    // IdWorker是mybatis-plus提供的工具类，用于生成订单号

        AtomicInteger amount = new AtomicInteger(0);    // 订单总金额，原子操作，保证线程安全

        // 遍历购物车数据，生成订单数据
        List<OrderDetail> orderDetails = shoppingCarts.stream().map(item -> {
            // a.生成订单商品数据对象
            OrderDetail orderDetail = new OrderDetail();
            // b.设置订单商品数据对象的属性
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        // 向orders插入数据
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(currentId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
        // 向order_detail插入数据
        orderDetailService.saveBatch(orderDetails);
        // 清空购物车数据
        shoppingCartService.remove(cartQueryWrapper);
    }


    /**
     * 获取用户订单分页
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return 分页数据
     */
    public Page<OrdersDto> getUserPage(int page, int pageSize) {
        // 构造分页对象
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>(); // 用于存放转换后的数据
        // 构造查询条件
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        // 查询基本订单数据
        this.page(ordersPage, queryWrapper);
        // 拷贝对象，将ordersPage中的数据拷贝到dtoPage中，除去records属性,records属性需要单独处理
        BeanUtils.copyProperties(ordersPage, dtoPage, "records");
        // records是Page对象的一个属性，用于存放分页查询的结果，因为orderPage的records很多数据比dtoPage的少，需要单独处理
        List<Orders> ordersPageRecords = ordersPage.getRecords();
        // 将records中的每个Orders对象转换为OrdersDto对象
        List<OrdersDto> dtoPageRecords = ordersPageRecords.stream().map(item -> {
            // 1.先创建OrdersDto对象
            OrdersDto ordersDto = new OrdersDto();
            // 2.将Orders对象中的基本数据拷贝到OrdersDto对象中
            BeanUtils.copyProperties(item, ordersDto);
            // 3.查询订单商品数据
            LambdaQueryWrapper<OrderDetail> orderDetailQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailQueryWrapper);
            // 4.将订单商品数据设置到OrdersDto对象中
            ordersDto.setOrderDetails(orderDetails);
            // 5.将其它数据设置到OrdersDto对象中
            LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.eq(User::getId, item.getUserId());
            User user = userService.getOne(userQueryWrapper);
            if (user != null) {
                ordersDto.setUserName(user.getName());
                ordersDto.setEmail(user.getEmail());
            }
            ordersDto.setAmount(item.getAmount());
            ordersDto.setConsignee(item.getConsignee());
            ordersDto.setAddress(item.getAddress());
            ordersDto.setUserName(item.getConsignee()); // todo:!!暂时没有开发用户信息，所以将收货人设置为用户名

            return ordersDto;

        }).collect(Collectors.toList());

        // 将转换后的数据设置到dtoPage中
        dtoPage.setRecords(dtoPageRecords);

        return dtoPage;
    }


    /**
     * 获取管理员订单详情
     *
     * @param page      页码
     * @param pageSize  每页数量
     * @param number    订单号
     * @param beginTime 开始时间
     * @param endTime   结束时间
     */
    public Page<OrdersDto> getPage(int page,
                                   int pageSize,
                                   String number,
                                   @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss") Date beginTime,
                                   @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss") Date endTime) {
        // 构造分页对象
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>(); // 用于存放转换后的数据
        // 构造查询条件
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);
        queryWrapper.like(StringUtils.isNotEmpty(number), Orders::getNumber, number);
        queryWrapper.between((beginTime != null && endTime != null), Orders::getOrderTime, beginTime, endTime);
        // 查询基本订单数据
        this.page(ordersPage, queryWrapper);
        // 拷贝对象，将ordersPage中的数据拷贝到dtoPage中，除去records属性,records属性需要单独处理
        BeanUtils.copyProperties(ordersPage, dtoPage, "records");
        // records是Page对象的一个属性，用于存放分页查询的结果，因为orderPage的records很多数据比dtoPage的少，需要单独处理
        List<Orders> ordersPageRecords = ordersPage.getRecords();
        // 将records中的每个Orders对象转换为OrdersDto对象
        List<OrdersDto> dtoPageRecords = ordersPageRecords.stream().map(item -> {
            // 1.先创建OrdersDto对象
            OrdersDto ordersDto = new OrdersDto();
            // 2.将Orders对象中的基本数据拷贝到OrdersDto对象中
            BeanUtils.copyProperties(item, ordersDto);
            // 3.查询订单商品数据
            LambdaQueryWrapper<OrderDetail> orderDetailQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailQueryWrapper);
            // 4.将订单商品数据设置到OrdersDto对象中
            ordersDto.setOrderDetails(orderDetails);
            // 5.将其它数据设置到OrdersDto对象中
            LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.eq(User::getId, item.getUserId());
            User user = userService.getOne(userQueryWrapper);
            if (user != null) {
                ordersDto.setUserName(user.getName());
                ordersDto.setEmail(user.getEmail());
            }
            ordersDto.setAmount(item.getAmount());
            ordersDto.setConsignee(item.getConsignee());
            ordersDto.setAddress(item.getAddress());
            ordersDto.setUserName(item.getConsignee()); // todo:!!暂时没有开发用户信息，所以将收货人设置为用户名

            return ordersDto;
        }).collect(Collectors.toList());

        // 将转换后的数据设置到dtoPage中
        dtoPage.setRecords(dtoPageRecords);

        return dtoPage;
    }
}
