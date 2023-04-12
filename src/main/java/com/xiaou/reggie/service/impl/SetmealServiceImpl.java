package com.xiaou.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaou.reggie.common.CustomException;
import com.xiaou.reggie.dto.SetmealDto;
import com.xiaou.reggie.entity.Setmeal;
import com.xiaou.reggie.entity.SetmealDish;
import com.xiaou.reggie.mapper.SetmealMapper;
import com.xiaou.reggie.service.SetmealDishService;
import com.xiaou.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("AlibabaTransactionMustHaveRollback")
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    /**
     * 新增套餐同时保存套餐和菜品的关系
     *
     * @param setmealDto
     */
    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {

        /* 保存套餐的基本信息，操作setmeal，执行insert操作 */
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存产菜品的关键信息，操作setmeal_dish,执行insert

    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐的状态，来确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        //如果不能删除，抛出一个业务异常
        if (count > 0) {
            //有符合条件的，不能删除
            throw new CustomException("套餐正在售卖中，不能删除");
        }


        //如果可以删除，先删除套餐表中的数据---setmeal

        this.removeByIds(ids);


        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        //删除关系表中的数据--setmealDish
        setmealDishService.remove(lambdaQueryWrapper);

    }

    /**
     * 根据套餐id修改售卖状态
     *
     * @param status
     * @param ids
     */
    @Override
    public void updateSetmealStatusById(Integer status, List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(ids != null, Setmeal::getId, ids);
        List<Setmeal> list = this.list(queryWrapper);

        for (Setmeal setmeal : list) {
            if (setmeal != null) {
                setmeal.setStatus(status);
                this.updateById(setmeal);
            }
        }
    }

    /**
     * 回显套餐数据：根据套餐id查询套餐
     *
     * @return
     */
    @Override
    public SetmealDto getDate(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();

        //在关联表中查询setmealdish
        queryWrapper.eq(id != null, SetmealDish::getSetmealId, id);

        if (setmeal != null) {
            BeanUtils.copyProperties(setmeal, setmealDto);
            List<SetmealDish> list = setmealDishService.list(queryWrapper);
            setmealDto.setSetmealDishes(list);
            return setmealDto;
        }
        return null;

    }
}
