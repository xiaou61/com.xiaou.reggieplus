package com.xiaou.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaou.reggie.dto.SetmealDto;
import com.xiaou.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {


    /**
     * 新增套餐同时保存套餐和菜品的关系
     *
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     *
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 根据菜品id修改售卖状态
     *
     * @param status
     * @param ids
     */
    void updateSetmealStatusById(Integer status, List<Long> ids);


    /**
     * 回显套餐数据：根据套餐id查询套餐
     *
     * @return
     */
    public SetmealDto getDate(Long id);


}
