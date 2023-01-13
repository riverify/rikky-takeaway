package com.fubukiss.rikky.dto;

import com.fubukiss.rikky.entity.Dish;
import com.fubukiss.rikky.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Project: rikky-takeaway - DishDto 菜品数据传输对象
 * <p>Powered by river On 2023/01/12 5:32 PM
 * <p>
 * <hr/>
 * <b>dto: data transfer object，主要用于多表查询时，将查询结果封装成一个对象，方便前端使用，如在本项目的菜品新增中，前端需要传入菜品的基本信息，
 * 以及菜品的口味信息，而菜品和菜品口味是两张表，在后端拥有两个实体类，所以需要将这两个实体类封装成一个对象。<b/>
 * <hr/>
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Data
public class DishDto extends Dish {         // 这里继承了Dish实体类，所以DishDto对象中拥有Dish实体类中的所有属性

    /**
     * 风味
     */
    private List<DishFlavor> flavors = new ArrayList<>(); // 同一个食材可以有多个风味选项，故这里使用List

    /**
     * 分类名
     */
    private String categoryName;

    private Integer copies;
}
