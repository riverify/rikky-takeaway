package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.dto.SetmealDto;
import com.fubukiss.rikky.entity.Setmeal;
import com.fubukiss.rikky.entity.SetmealDish;
import com.fubukiss.rikky.mapper.SetmealMapper;
import com.fubukiss.rikky.service.SetmealDishService;
import com.fubukiss.rikky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Project: rikky-takeaway - SetmealServiceImpl
 * <p>Powered by river On 2023/01/06 11:17 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Service
@Slf4j
@EnableTransactionManagement    // 开启事务管理
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    // ServiceImpl<SetmealMapper, Setmeal> 为MyBatis-Plus提供的基础实现类，<SetmealMapper, Setmeal> 为泛型，SetmealMapper为Mapper接口，Setmeal为实体类


    /**
     * 套餐菜品关联Service
     */
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto 套餐数据传输对象
     */
    @Transactional          // 对于多表操作，需要开启事务管理
    public void saveWithDishes(SetmealDto setmealDto) {
        // 保存套餐基本信息
        this.save(setmealDto);
        // 获取setmeal的List<SetmealDish>信息，！需要注意的是，该list中的setmealId为null，前端未传入，所以需要手动遍历，通过SetmealDto继承的Setmeal获取id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().peek(item -> item.setSetmealId(setmealDto.getId())).collect(Collectors.toList()); // peek()方法为中间操作，不会改变原有的list，collect()方法为终止操作，会改变原有的list
        // 保存套餐和菜品的关联关系
        setmealDishService.saveBatch(setmealDishes);
    }
}
