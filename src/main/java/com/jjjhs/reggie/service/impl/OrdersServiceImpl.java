package com.jjjhs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjjhs.reggie.common.R;
import com.jjjhs.reggie.entity.OrderDetail;
import com.jjjhs.reggie.entity.Orders;
import com.jjjhs.reggie.entity.OrdersDto;
import com.jjjhs.reggie.mapper.OrdersMapper;
import com.jjjhs.reggie.service.OrderDetailService;
import com.jjjhs.reggie.service.OrdersService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private OrderDetailService orderDetailService;

    public Page<OrdersDto> pageWithDetail(int page, int pageSize, Long userId) {
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(userId!=null, Orders::getUserId, userId);
        ordersLambdaQueryWrapper.orderByDesc(Orders::getCheckoutTime);
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        this.page(ordersPage, ordersLambdaQueryWrapper);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");
        List<Orders> orders = ordersPage.getRecords();
        LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<OrdersDto> ordersDtos = orders.stream().map((order)->{
            OrdersDto orderDto = new OrdersDto();
            BeanUtils.copyProperties(order, orderDto);
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, order.getId());
            orderDto.setOrderDetails(orderDetailService.list(orderDetailLambdaQueryWrapper));
            return orderDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtos);
        return ordersDtoPage;
    }
}
