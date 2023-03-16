package com.jjjhs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjjhs.reggie.common.CustomException;
import com.jjjhs.reggie.entity.Category;
import com.jjjhs.reggie.entity.Dish;
import com.jjjhs.reggie.entity.Setmeal;
import com.jjjhs.reggie.mapper.CategoryMapper;
import com.jjjhs.reggie.service.CategoryService;
import com.jjjhs.reggie.service.DishService;
import com.jjjhs.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(queryWrapper);
        if(count > 0) {
            // 抛出业务异常
            throw new CustomException("存在菜品相关联，不能删除");
        }
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(queryWrapper1);
        if(count1 > 0) {
            // 抛出业务异常
            throw new CustomException("存在套餐相关联，不能删除");
        }
        super.removeById(id);
    }
}
