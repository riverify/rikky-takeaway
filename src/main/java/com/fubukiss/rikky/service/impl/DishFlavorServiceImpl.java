package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.entity.DishFlavor;
import com.fubukiss.rikky.mapper.DishFlavorMapper;
import com.fubukiss.rikky.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * <p>Project: rikky-takeaway - DishFlavorServiceImpl
 * <p>Powered by river On 2023/01/12 2:46 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    // ServiceImpl<DishFlavorMapper, DishFlavor> 为MyBatis-Plus提供的基础实现类，<DishFlavorMapper, DishFlavor> 为泛型，DishFlavorMapper为Mapper接口，DishFlavor为实体类
}
