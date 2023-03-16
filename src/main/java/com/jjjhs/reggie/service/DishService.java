package com.jjjhs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jjjhs.reggie.Dto.DishDto;
import com.jjjhs.reggie.entity.Dish;


public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public void updateWithFlavor(DishDto dishDto);
    public DishDto dish(Long id);
}
