package com.jjjhs.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jjjhs.reggie.entity.Orders;
import com.jjjhs.reggie.entity.OrdersDto;

public interface OrdersService extends IService<Orders> {
    public Page<OrdersDto> pageWithDetail(int page, int pageSize, Long userId);
}
