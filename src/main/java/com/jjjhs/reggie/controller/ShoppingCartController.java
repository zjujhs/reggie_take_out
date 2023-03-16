package com.jjjhs.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jjjhs.reggie.common.R;
import com.jjjhs.reggie.entity.ShoppingCart;
import com.jjjhs.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService service;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, id);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        return R.success(service.list(queryWrapper));
    }

    @PostMapping("/add")
    public R<String> add(HttpServletRequest request, @RequestBody ShoppingCart shoppingCart) {
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        ShoppingCart preShoppingCart = service.getOne(queryWrapper);
        if(preShoppingCart == null) {
            shoppingCart.setUserId(userId);
            shoppingCart.setNumber(1);
            service.save(shoppingCart);
            return R.success("");
        }
        else {
            preShoppingCart.setNumber(preShoppingCart.getNumber() + 1);
            service.updateById(preShoppingCart);
            return R.success("");
        }
    }

    @DeleteMapping("/clean")
    public R<String> clean(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, id);
        service.remove(queryWrapper);
        return R.success("");
    }


    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        shoppingCart = service.getOne(queryWrapper);
        if(shoppingCart == null) {
            return R.error("不存在");
        }
        int count = shoppingCart.getNumber();
        if(count == 1) {
            service.removeById(shoppingCart.getId());
            return R.success("");
        }
        else {
            shoppingCart.setNumber(count-1);
            service.updateById(shoppingCart);
            return R.success("");
        }
    }
}
