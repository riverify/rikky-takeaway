package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.common.CustomException;
import com.fubukiss.rikky.dto.SetmealDto;
import com.fubukiss.rikky.entity.Setmeal;
import com.fubukiss.rikky.entity.SetmealDish;
import com.fubukiss.rikky.mapper.SetmealMapper;
import com.fubukiss.rikky.service.SetmealDishService;
import com.fubukiss.rikky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        int count = this.count(setmealQueryWrapper);   //　count()方法为MyBatis-Plus提供的查询方法，返回查询结果的数量
        // 如果不能删除，抛出异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖，不能删除");  // 自定义异常
        }
        // 如果能删除，先删除套餐表中的数据  setmeal的id为主键，所以可以直接删除
        this.removeByIds(ids);                  // removeByIds()方法为MyBatis-Plus提供的删除方法，根据id集合删除数据
        // 删除关系表中的数据  setmeal_dish的setmeal_id不是主键，所以需要先查询出setmeal_id对应的id集合，再根据id集合删除数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);   // where setmeal_id in (1, 2, 3)
        setmealDishService.remove(setmealDishLambdaQueryWrapper);   // remove()方法为MyBatis-Plus提供的删除方法，根据条件删除数据
    }


    /**
     * 修改套餐状态，如果是在售状态则修改为下架，如果是下架状态则修改为在售
     *
     * @param ids    套餐id列表
     * @param status 需要修改的状态
     */
    @Override
    public void changeStatus(List<Long> ids, Integer status) {
        for (Long id : ids) {
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();   // 条件构造器
            updateWrapper.eq(Setmeal::getId, id);   // where id = ?
            updateWrapper.set(Setmeal::getStatus, status);   // set status = ?
            this.update(updateWrapper);  // update()方法为MyBatis-Plus提供的更新方法，根据条件更新数据
        }
    }


    /**
     * 根据id获取某套餐的基本信息和套餐所含菜品
     *
     * @param id 套餐id
     */
    @Override
    public SetmealDto getByIdWithDishes(Long id) {
        // 获取套餐基本信息　不含菜品信息
        Setmeal setmeal = this.getById(id);
        // 创建setmealDto对象，用于封装套餐基本信息和套餐所含菜品
        SetmealDto setmealDto = new SetmealDto();
        // 将setmeal的基本信息封装到setmealDto对象中
        BeanUtils.copyProperties(setmeal, setmealDto);
        // 获取套餐所含菜品
        LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishQueryWrapper.eq(SetmealDish::getSetmealId, id);  // where setmeal_id = ?
        List<SetmealDish> dishes = setmealDishService.list(setmealDishQueryWrapper);  // list()方法为MyBatis-Plus提供的查询方法，根据条件查询数据
        // 将套餐所含菜品封装到setmealDto对象中
        setmealDto.setSetmealDishes(dishes);

        // 返回setmealDto对象
        return setmealDto;
    }


    /**
     * 更新套餐信息，同时更新套餐和菜品的关联关系
     * <p>@Transactional注解表示该方法需要事务支持，如果该方法抛出异常，则事务回滚。
     * <p>更新关联关系的业务逻辑：对于关联关系，先删除原有关联关系，再添加新的关联关系，这样可以保证关联关系的唯一性。
     *
     * @param setmealDto 套餐数据传输对象
     */
    @Transactional
    @Override
    public void updateWithDishes(SetmealDto setmealDto) {
        // 更新套餐基本信息
        this.updateById(setmealDto);
        // 获取套餐的id
        Long setmealId = setmealDto.getId();
        // 修改套餐和菜品的关联关系
        //　1.删除原有关联关系
        setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, setmealId));   // remove()方法为MyBatis-Plus提供的删除方法，根据条件删除数据
        // 2.添加新的关联关系 即将setmeal的id字段赋值给setmealDish的setmealId字段
        // 获取新的关联关系
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes(); // 该setmealDishes中的setmealId字段为null
        // 使用stream流将setmealDishes中的setmealId字段赋值为setmealDtoId
        setmealDishes = setmealDishes.stream().peek(item -> {
            item.setSetmealId(setmealId);   // 将套餐id赋值给setmealDish的setmealId字段
        }).collect(Collectors.toList());    // 将stream流转换为list集合

        // 保存新的关联关系
        setmealDishService.saveBatch(setmealDishes);  // saveBatch()方法为MyBatis-Plus提供的批量保存方法，保存的是一个集合

    }
}
