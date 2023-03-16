package com.jjjhs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjjhs.reggie.entity.OrderDetail;
import com.jjjhs.reggie.mapper.OrderDetailMapper;
import com.jjjhs.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
