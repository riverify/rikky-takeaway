package com.fubukiss.rikky.controller;

import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.dto.SetmealDto;
import com.fubukiss.rikky.service.SetmealDishService;
import com.fubukiss.rikky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Project: rikky-takeaway - SetmealController 套餐控制器
 * <p>Powered by river On 2023/01/14 5:34 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    /**
     * 套餐服务
     */
    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐菜品服务
     */
    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * <h2>新增套餐<h2/>
     *
     * @param setmealDto 套餐数据传输对象，包含了套餐信息(Setmeal)和套餐菜品信息(SetmealDish List)，@RequestBody注解用于接收前端传递的json数据
     * @return 消息
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐:{}", setmealDto);
        // 新增套餐
        setmealService.saveWithDishes(setmealDto); // 该方法为自定义方法，详见SetmealServiceImpl

        return R.success("添加套餐成功");
    }

}
