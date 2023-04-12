package com.xiaou.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaou.reggie.entity.OrderDetail;
import com.xiaou.reggie.mapper.OrderDetailMapper;
import com.xiaou.reggie.service.OrderDetailService;
import com.xiaou.reggie.service.OrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrdersDetailSerciceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
