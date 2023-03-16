package com.jjjhs.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjjhs.reggie.Dto.SetmealDto;
import com.jjjhs.reggie.entity.Setmeal;
import com.jjjhs.reggie.entity.SetmealDish;
import com.jjjhs.reggie.mapper.SetmealMapper;
import com.jjjhs.reggie.service.CategoryService;
import com.jjjhs.reggie.service.SetmealDishService;
import com.jjjhs.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    public void saveWithSetmealDish(SetmealDto setmealDto) {
        save(setmealDto);
        Long id = setmealDto.getId();
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        list.forEach((record)-> {
            record.setSetmealId(id);
        });
        setmealDishService.saveBatch(list);
    }
    public void updateWithSetmealDish(SetmealDto setmealDto) {
        updateById(setmealDto);
        Long id = setmealDto.getId();
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        list.forEach((record)-> {
            record.setSetmealId(id);
        });
        setmealDishService.updateBatchById(list);
    }
    public SetmealDto setmeal(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(getById(id), setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        queryWrapper.orderByDesc(SetmealDish::getUpdateTime);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

}
