package com.fubukiss.rikky.controller;

import com.fubukiss.rikky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Project: rikky-takeaway - CategoryController 分类管理的Controller类
 * <p>Powered by Riverify On 01-02-2023 21:48:43
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


}
