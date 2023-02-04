package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.common.CustomException;
import com.fubukiss.rikky.dto.SetmealDto;
import com.fubukiss.rikky.entity.Category;
import com.fubukiss.rikky.entity.Setmeal;
import com.fubukiss.rikky.entity.SetmealDish;
import com.fubukiss.rikky.mapper.SetmealMapper;
import com.fubukiss.rikky.service.CategoryService;
import com.fubukiss.rikky.service.SetmealDishService;
import com.fubukiss.rikky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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
     * 菜品分类Service
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Redis缓存
     */
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


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

        // 清理redis缓存
        Set<Object> keys = redisTemplate.keys("setmeal_*");    // 获取所有以dish_开头的key
        assert keys != null;
        redisTemplate.delete(keys);

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


        // 清理redis缓存
        Set<Object> keys = redisTemplate.keys("setmeal_*");    // 获取所有以dish_开头的key
        assert keys != null;
        redisTemplate.delete(keys);

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

        // 清理redis缓存
        Set<Object> keys = redisTemplate.keys("setmeal_*");    // 获取所有以dish_开头的key
        assert keys != null;
        redisTemplate.delete(keys);

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


        // 清理redis缓存
        Set<Object> keys = redisTemplate.keys("setmeal_*");    // 获取所有以dish_开头的key
        assert keys != null;
        redisTemplate.delete(keys);

    }


    /**
     * 查询套餐列表，同时查询套餐所含菜品信息
     *
     * @param setmeal 查询条件
     * @return 套餐列表
     */
    @Override
    public List<SetmealDto> listWithDishes(Setmeal setmeal) {

        // 构造返回类型
        List<SetmealDto> setmealDtoList = null;

        // 动态构造key
        String key = "setmeal_" + setmeal.getCategoryId() + "_" + setmeal.getStatus();   // 生成redis的key

        // 获取redis中的数据
        setmealDtoList = (List<SetmealDto>) redisTemplate.opsForValue().get(key);

        // 如果redis中有数据，则直接返回
        if (setmealDtoList != null) {
            return setmealDtoList;
        }

        // 如果redis中没有数据，则从数据库中查询
        // 创建构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        // 设置查询条件 where category_id = ? and status = 1 order by update_time desc
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId())
                .eq(Setmeal::getStatus, 1)
                .orderByDesc(Setmeal::getUpdateTime);

        // 查询套餐基本信息
        List<Setmeal> setmealList = this.list(queryWrapper);

        // 查询套餐所含菜品信息
        setmealDtoList = setmealList.stream().map(item -> {

            // 创建套餐dto 包含套餐基本信息和套餐所含菜品信息
            SetmealDto setmealDto = new SetmealDto();

            // 设置套餐基本信息
            BeanUtils.copyProperties(item, setmealDto);

            // 查询套餐所含菜品信息
            LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>(); // select * from setmeal_dish
            setmealDishQueryWrapper.eq(SetmealDish::getSetmealId, item.getId()); // where setmeal_id = ?

            // 查询套餐所含菜品信息
            List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishQueryWrapper);

            // 加入套餐dto
            setmealDto.setSetmealDishes(setmealDishList);

            return setmealDto;

        }).collect(Collectors.toList());

        // 将数据存入redis
        redisTemplate.opsForValue().set(key, setmealDtoList);


        return setmealDtoList;
    }

    /**
     * 分页查询套餐信息
     *
     * @param page     当前页
     * @param pageSize 每页显示条数
     * @param name     套餐名称
     */
    public Page<SetmealDto> page(int page, int pageSize, String name) {
        // 分页构造器
        Page<Setmeal> setmealPage = new Page<>(page, pageSize); // page为当前页码，pageSize为每页显示的条数
        Page<SetmealDto> setmealDtoPage = new Page<>(); // setmealDto比setmeal多了一个categoryName属性，用于存储套餐所属的分类名称，因为只用setmeal对象的Page无法获取到分类名称在前端响应，该page暂时为空，后面会拷贝和填充数据
        // 条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件 where name like '%name%' (如果前端传入name的话)
        queryWrapper.like(name != null, Setmeal::getName, name);    // name != null 为true时，添加查询条件
        // 添加排序条件 order by update_time desc
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 调用分页查询方法
        this.page(setmealPage, queryWrapper); // setmealPage为分页对象，queryWrapper为条件构造器
        // 拷贝分页对象，将setmealPage中的数据拷贝到setmealDtoPage中
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");  // setmealPage为源对象，setmealDtoPage为目标对象，"records"为不需要拷贝的属性，之所以不拷贝records属性，是因为records属性是一个List，而List是引用类型，拷贝的话，两个对象的records属性指向的是同一个List，会导致数据混乱，而且俩个对象的records属性是不同类型的，拷贝会报错
        // records是Page对象中的一个字段，用于存储分页查询的结果，因为setmealDtoPage中的records字段是空的，所以需要手动将setmealDtoPage中的records字段赋值
        List<Setmeal> records = setmealPage.getRecords();

        // 将records中的每一个setmeal对象的categoryId经过查询得到categoryName，然后封装到setmealDto对象中，同时也将其它字段赋给setmealDto对象，返回的是一个List<SetmealDto>对象
        List<SetmealDto> setmealDtoRecordsList = records.stream().map(item -> {
            // 1.new SetmealDto()是为了将Setmeal对象转换为SetmealDto对象，因为SetmealDto中多了一个categoryName属性，用于存储套餐所属的分类名称
            SetmealDto setmealDto = new SetmealDto();
            // 2.先进行setmealDto的普通字段拷贝
            BeanUtils.copyProperties(item, setmealDto);
            // 3.再进行setmealDto的categoryName字段拷贝
            Long categoryId = item.getCategoryId();                     // 获取page里面records的每一个setmeal对象的categoryId
            Category category = categoryService.getById(categoryId);    // 根据categoryId查询category对象
            if (category != null) {                                     // 防止category为空
                String categoryName = category.getName();               // 通过查询的category对象获取categoryName
                setmealDto.setCategoryName(categoryName);               // 将categoryName赋值给dishDto对象的categoryName
            }

            return setmealDto;
        }).collect(Collectors.toList());  // collect方法将stream转换为List，因为setmealDtoRecordsList是一个List对象，所以需要将stream转换为List

        // 将setmealDtoRecordsList赋值给setmealDtoPage的records字段
        setmealDtoPage.setRecords(setmealDtoRecordsList);

        return setmealDtoPage;
    }
}
