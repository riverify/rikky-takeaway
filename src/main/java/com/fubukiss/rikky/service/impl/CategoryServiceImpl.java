package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.entity.Category;
import com.fubukiss.rikky.mapper.CategoryMapper;
import com.fubukiss.rikky.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * <p>Project: rikky-takeaway - CategoryServiceImpl
 * <p>Powered by Riverify On 01-02-2023 21:44:52
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    // ServiceImpl<CategoryMapper, Category> 为MyBatis-Plus提供的基础实现类，<CategoryMapper, Category> 为泛型，CategoryMapper为Mapper接口，Category为实体类
}
