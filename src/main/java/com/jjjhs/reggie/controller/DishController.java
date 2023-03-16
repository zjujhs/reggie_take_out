package com.jjjhs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjjhs.reggie.Dto.DishDto;
import com.jjjhs.reggie.common.R;
import com.jjjhs.reggie.entity.Dish;
import com.jjjhs.reggie.entity.DishFlavor;
import com.jjjhs.reggie.entity.SetmealDish;
import com.jjjhs.reggie.service.CategoryService;
import com.jjjhs.reggie.service.DishFlavorService;
import com.jjjhs.reggie.service.DishService;
import com.jjjhs.reggie.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SetmealDishService setmealDishService;


    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((dish)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            String categoryName = categoryService.getById(dish.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> dish(@PathVariable Long id) {
        return R.success(dishService.dish(id));
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishService.list(queryWrapper);
        List<DishDto> dishDtos = dishes.stream().map((dish)->{
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(dish, dto);
            LambdaQueryWrapper<DishFlavor> flavorQuery = new LambdaQueryWrapper<>();
            flavorQuery.eq(DishFlavor::getDishId, dish.getId());
            dto.setFlavors(dishFlavorService.list(flavorQuery));
            return dto;
        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }

    @DeleteMapping
    public R<String> delete(String ids) {
        if(ids.contains(",")) {
            boolean part = false, all = true;
            for(String str: ids.split(",")) {
                if(deleteOne(Long.parseLong(str))) {
                    part = true;
                }
                else {
                    all = false;
                }
            }
            if(all) {
                return R.success("已全部删除");
            }
            else if(part) {
                return R.error("部分删除");
            }
            else {
                return R.error("所有菜品均存在于套餐中，删除失败");
            }
        }
        else {
            if(deleteOne(Long.parseLong(ids))) {
                return R.success("删除成功");
            }
            else {
                return R.error("菜品存在于套餐中，无法删除");
            }
        }
    }

    public boolean deleteOne(Long id) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getDishId, id);
        int count = setmealDishService.count(queryWrapper);
        if(count > 0) return false;
        dishService.removeById(id);
        return true;
    }

//    @DeleteMapping
//    public R<String> delete(Long[] ids) {
//        log.info("批量删除");
//        return R.success("没成功");
//    }
}
