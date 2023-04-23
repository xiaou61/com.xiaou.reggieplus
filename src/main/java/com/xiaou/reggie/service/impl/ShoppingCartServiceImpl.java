package com.xiaou.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaou.reggie.common.BaseContext;
import com.xiaou.reggie.entity.ShoppingCart;
import com.xiaou.reggie.mapper.ShoppingCartMapper;
import com.xiaou.reggie.service.ShoopingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoopingCartService {
    @Autowired
    private ShoopingCartService shoppingCartService;
    @Override
    public void clean() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        wrapper.eq(ShoppingCart::getUserId,userId);
        //清空购物车数据
        shoppingCartService.remove(wrapper);
    }
}
