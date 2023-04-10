package com.xiaou.reggie.controller;

import com.xiaou.reggie.entity.SetmealDish;
import com.xiaou.reggie.service.SetmealDishService;
import com.xiaou.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/setmeal")
@RestController
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;


}
