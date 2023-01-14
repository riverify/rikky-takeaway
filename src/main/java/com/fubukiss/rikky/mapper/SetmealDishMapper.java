package com.fubukiss.rikky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fubukiss.rikky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>Project: rikky-takeaway - SetmealDishMapper 套餐菜品关联Mapper
 * <p>Powered by river On 2023/01/14 5:30 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
    // 这里不需要写任何方法，因为BaseMapper中已经包含了常用的增删改查方法
}
