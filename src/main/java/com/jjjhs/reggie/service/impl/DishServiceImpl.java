package com.jjjhs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjjhs.reggie.Dto.DishDto;
import com.jjjhs.reggie.common.R;
import com.jjjhs.reggie.entity.Dish;
import com.jjjhs.reggie.entity.DishFlavor;
import com.jjjhs.reggie.mapper.DishMapper;
import com.jjjhs.reggie.service.DishFlavorService;
import com.jjjhs.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> list = dishDto.getFlavors();
        for(DishFlavor dishFlavor: list) {
            dishFlavor.setDishId(dishId);
        }
        for(DishFlavor dishFlavor: list) {
            log.info("dish_id={}", dishFlavor.getDishId());
        }
        dishFlavorService.saveBatch(list);
    }

    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> list = dishDto.getFlavors();
        for(DishFlavor dishFlavor: list) {
            dishFlavor.setDishId(dishId);
        }
        for(DishFlavor dishFlavor: list) {
            log.info("dish_id={}", dishFlavor.getDishId());
        }
        dishFlavorService.updateBatchById(list);
    }

    @Override
    public DishDto dish(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id!=null, DishFlavor::getDishId, id);
        queryWrapper.orderByDesc(DishFlavor::getUpdateTime);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }
}
