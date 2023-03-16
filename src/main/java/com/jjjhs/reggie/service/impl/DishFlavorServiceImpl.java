package com.jjjhs.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjjhs.reggie.Dto.DishDto;
import com.jjjhs.reggie.entity.DishFlavor;
import com.jjjhs.reggie.mapper.DishFlavorMapper;
import com.jjjhs.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

}
