package com.jjjhs.reggie.Dto;

import com.jjjhs.reggie.entity.Setmeal;
import com.jjjhs.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
