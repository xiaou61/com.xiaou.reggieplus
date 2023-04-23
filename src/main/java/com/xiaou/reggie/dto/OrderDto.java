package com.xiaou.reggie.dto;

import com.xiaou.reggie.entity.OrderDetail;
import com.xiaou.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;
}