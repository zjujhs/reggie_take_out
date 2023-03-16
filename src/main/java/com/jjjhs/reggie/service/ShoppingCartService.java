package com.jjjhs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jjjhs.reggie.entity.ShoppingCart;


public interface ShoppingCartService extends IService<ShoppingCart> {
    public void removeByUserId(Long userId);
}
