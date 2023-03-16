package com.jjjhs.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jjjhs.reggie.common.R;
import com.jjjhs.reggie.entity.AddressBook;
import com.jjjhs.reggie.entity.User;
import com.jjjhs.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService service;

    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("user");
        log.info("userId: {}", id);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, id);
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = service.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody AddressBook addressBook) {
        Long id = (Long)request.getSession().getAttribute("user");
        addressBook.setUserId(id);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, id);
        int count = service.count(queryWrapper);
        if(count == 0) {
            addressBook.setIsDefault(1);
        }
        service.save(addressBook);
        return R.success("");
    }

    @PutMapping("default")
    public R<String> setDefault(HttpServletRequest request, @RequestBody AddressBook addressBook) {
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook preAddressBook = service.getOne(queryWrapper);
        if(preAddressBook != null) {
            if(!Objects.equals((Long) preAddressBook.getId(), (Long) addressBook.getId())) {
                preAddressBook.setIsDefault(0);
                addressBook.setIsDefault(1);
                service.updateById(preAddressBook);
                service.updateById(addressBook);
            }
        }
        else {
            addressBook.setIsDefault(1);
            service.updateById(addressBook);
        }
        return R.success("");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(HttpServletRequest request) {
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, request.getSession().getAttribute("user"));
        lambdaQueryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = service.getOne(lambdaQueryWrapper);
        return R.success(addressBook);
    }

//    @PostMapping("")

    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id) {
        AddressBook book = service.getById(id);
        if(book == null) return R.error("不存在");
        else return R.success(book);
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
        service.updateById(addressBook);
        return R.success("");
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        service.removeById(ids);
        return R.success("");
    }
}

