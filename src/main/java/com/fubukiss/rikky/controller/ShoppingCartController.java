package com.fubukiss.rikky.controller;


import com.fubukiss.rikky.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileName: ShoppingCartController
 * Date: 2023/01/19
 * Time: 21:19
 * Author: river
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {


    @GetMapping("/list")
    public R list() {
        return null;
    }
}
