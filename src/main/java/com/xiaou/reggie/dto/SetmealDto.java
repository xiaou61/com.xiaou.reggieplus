package com.xiaou.reggie.dto;

import com.xiaou.reggie.entity.Setmeal;
import com.xiaou.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
