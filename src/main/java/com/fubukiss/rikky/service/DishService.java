package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.controller.CommonController;
import com.fubukiss.rikky.dto.DishDto;
import com.fubukiss.rikky.entity.Dish;

import java.util.List;


/**
 * <p>Project: rikky-takeaway - DishService 菜品Service
 * <p>Powered by river On 2023/01/06 11:10 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
public interface DishService extends IService<Dish> {

    /**
     * 添加菜品，同时插入口味的数据
     *
     * @param dishDto 菜品数据
     */
    void saveWithFlavors(DishDto dishDto);


    /**
     * 更新菜品，同时更新口味的数据
     *
     * @param dishDto 菜品数据
     */
    void updateWithFlavors(DishDto dishDto);

    /**
     * 根据id获得菜品和口味的数据
     *
     * @param id 菜品id
     * @return 菜品数据（包含口味数据）
     */
    DishDto getByIdWithFlavors(Long id);

    /**
     * 获得所有菜品和口味的数据
     *
     * @return 菜品数据List（包含口味数据）
     */
    List<DishDto> listWithFlavors(Dish dish);


    /**
     * 修改菜品状态，如果是在售状态则修改为下架，如果是下架状态则修改为在售
     *
     * @param ids    菜品id
     * @param status 需要修改成的状态
     */
    void updateDishStatus(String ids, Integer status);


    /**
     * 删除菜品（逻辑删除)
     *
     * @param ids 前端传入的菜品id，可能是一个，也可能是多个，多个数据是以逗号分隔的
     */
    void deleteByIds(String ids);


    /**
     * 分页查询菜品
     * <p>其中菜品的图片由{@link CommonController}提供下载到页面的功能。
     *
     * @param page     前端传入的分页参数，一次性传入当前页码
     * @param pageSize 前端传入的分页参数，一次性传入每页显示的条数
     * @param name     查询条件，如果name为空，则查询所有菜品
     * @return Page对象，mybatis-plus提供的分页对象，包含了分页的所有信息
     */
    Page<DishDto> page(int page, int pageSize, String name);
}
