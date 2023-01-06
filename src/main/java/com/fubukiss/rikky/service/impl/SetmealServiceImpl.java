package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.entity.Setmeal;
import com.fubukiss.rikky.mapper.SetmealMapper;
import com.fubukiss.rikky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    // ServiceImpl<SetmealMapper, Setmeal> 为MyBatis-Plus提供的基础实现类，<SetmealMapper, Setmeal> 为泛型，SetmealMapper为Mapper接口，Setmeal为实体类

}
