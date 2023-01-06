package com.fubukiss.rikky.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.entity.Dish;
import com.fubukiss.rikky.mapper.DishMapper;
import com.fubukiss.rikky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>Project: rikky-takeaway - DishServiceImpl
 * <p>Powered by river On 2023/01/06 11:14 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    // ServiceImpl<DishMapper, Dish> 为MyBatis-Plus提供的基础实现类，<DishMapper, Dish> 为泛型，DishMapper为Mapper接口，Dish为实体类
}
