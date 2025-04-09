package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorsMapper dishFlavorsMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Transactional
    public void addDish(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish); //与数据库的格式一定要对应

        Long dishId = dish.getId(); //获取插入菜品的id

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorsMapper.addFlavors(flavors);
            log.info("增添口味：{}", dishDTO.getFlavors());
        }
    }


    public PageResult page(DishPageQueryDTO dishPageQueryDTO){
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> pageList = dishMapper.pageSelect(dishPageQueryDTO); //查找符合要求的实体类

//        分页结果实体的封装
        Long total = pageList.getTotal();
        List<DishVO> dishList= pageList.getResult();

        PageResult pageResult = new PageResult(total, dishList);
        return pageResult;
    }

    public void delete(List<Long> ids){
        for(Long id : ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus().equals(StatusConstant.ENABLE)){
                log.info("菜品状态：{}", dish.getStatus());
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
            List<Long> setmealDishIds = setmealDishMapper.getDishIds(id); //可能有多个菜品，一个菜品可能在不同的套餐中
            log.info("关联套餐：{}", setmealDishIds);
            if(setmealDishIds != null && setmealDishIds.size() != 0){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
        dishMapper.delete(ids);
        dishFlavorsMapper.delete(ids);

    }


    public void startOrStop(Integer status, Long id){
        log.info("需要设置的id为{}", id);

//        Dish dish = new Dish();
//        dish.setStatus(status);
//        dish.setId(id);
//
//        dishMapper.startOrStop(dish);
        dishMapper.startOrStop(status, id);  //有多个参数的情况下，尽量返回
    }

    public void update(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish); //与数据库的格式一定要对应

        Long dishId = dish.getId(); //获取插入菜品的id

        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishFlavorsMapper.update(flavors);
    }
}
