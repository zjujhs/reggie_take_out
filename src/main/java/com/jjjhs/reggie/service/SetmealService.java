package com.jjjhs.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jjjhs.reggie.Dto.SetmealDto;
import com.jjjhs.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithSetmealDish(SetmealDto setmealDto);
    public SetmealDto setmeal(Long id);
    public void updateWithSetmealDish(SetmealDto setmealDto);
}
