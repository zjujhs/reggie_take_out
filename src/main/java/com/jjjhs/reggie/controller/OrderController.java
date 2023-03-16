package com.jjjhs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjjhs.reggie.common.R;
import com.jjjhs.reggie.entity.*;
import com.jjjhs.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, LocalDateTime beginTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(beginTime!=null&endTime!=null, Orders::getOrderTime, beginTime, endTime);
        queryWrapper.eq(number!=null, Orders::getId, number);
        Page<Orders> pageInfo = new Page(page, pageSize);
        ordersService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @GetMapping("/userPage")
    public R<Page> page(HttpServletRequest request, int page, int pageSize) {
        return R.success(ordersService.pageWithDetail(page, pageSize, (Long) request.getSession().getAttribute("user")));
    }

    @PostMapping("/submit")
    public R<String> submit(HttpServletRequest request, @RequestBody Orders order) {
        Long userId = (Long) request.getSession().getAttribute("user");
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        order.setUserId(userId);
        order.setStatus(2);

        AddressBook addressBook = addressBookService.getById(order.getAddressBookId());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setPhone(addressBook.getPhone());

        User user = userService.getById(userId);
        order.setUserName(user.getName());

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        BigDecimal amount = new BigDecimal(0);
        for(ShoppingCart s: shoppingCarts) {
            BigDecimal dishAmount = s.getAmount();
            BigDecimal dishNumber = new BigDecimal(s.getNumber());
            log.info("单价：{}, 数量：{}", dishAmount, dishNumber);
            amount = amount.add(dishAmount.multiply(dishNumber));
        }
        order.setAmount(amount);
        log.info("总金额：{}", amount);
        ordersService.save(order);

        shoppingCarts.forEach((s)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            BeanUtils.copyProperties(s, orderDetail, "id");
            orderDetailService.save(orderDetail);
        });

        shoppingCartService.removeByUserId(userId);

        return R.success("");
    }
}
