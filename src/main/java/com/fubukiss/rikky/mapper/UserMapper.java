package com.fubukiss.rikky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fubukiss.rikky.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * FileName: UserMapper
 * Date: 2023/01/16
 * Time: 20:18
 * Author: river
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
