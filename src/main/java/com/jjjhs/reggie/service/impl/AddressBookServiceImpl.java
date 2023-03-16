package com.jjjhs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjjhs.reggie.entity.AddressBook;
import com.jjjhs.reggie.mapper.AddressBookMapper;
import com.jjjhs.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
