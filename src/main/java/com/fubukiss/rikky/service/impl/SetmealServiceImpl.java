package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    @Override
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


    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据,只有停止售卖的套餐才能删除
     * <p>@Transactional注解的rollbackFor属性，用于指定哪些异常需要回滚，哪些异常不需要回滚，默认情况下，只有运行时异常才会回滚。
     *
     * @param ids 套餐id集合
     */
    @Override
    @Transactional
    public void removeWithDishes(List<Long> ids) {
        // 查询套餐是否在售卖 select count(1) from setmeal where id in (1, 2, 3) and status = 1
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);   //　count()方法为MyBatis-Plus提供的查询方法，返回查询结果的数量
        if (count > 0) {
            throw new RuntimeException("套餐正在售卖，不能删除");  //
        }
        // todo: 删除套餐和菜品的关联关系，睡觉了
        // 如果不能删除，抛出异常
        // 如果能删除，先删除套餐表中的数据
        // 删除关系表中的数据
    }
}
