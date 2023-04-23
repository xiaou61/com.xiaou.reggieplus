package com.xiaou.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaou.reggie.entity.ShoppingCart;
import com.xiaou.reggie.mapper.ShoppingCartMapper;
import com.xiaou.reggie.service.ShoopingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoopingCartService {
}
