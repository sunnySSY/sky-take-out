package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO){
//        当前是否在购物车中存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list != null && list.size() > 0){
            shoppingCart.setNumber(list.get(0).getNumber() + 1);
            shoppingCartMapper.setNumberById(shoppingCart);//根据查询到的数据（对象）进行get
        }else{
            if(shoppingCartDTO.getDishId() != null){
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            }else{
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
//                BeanUtils.copyProperties(setmeal, shoppingCart);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.addShoppingCart(shoppingCart);
        }
    }
    public List<ShoppingCart> showShoppingCart(){
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        return shoppingCartList;
    }

    public void clean(){
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.clean(shoppingCart);
    }

    public void delete(ShoppingCartDTO shoppingCartDTO){
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list != null && list.size() > 0){
            shoppingCart.setNumber(list.get(0).getNumber() - 1);
            shoppingCartMapper.setNumberById(shoppingCart);//根据查询到的数据（对象）进行get
        }else{
            shoppingCartMapper.delete(shoppingCart);
        }
    }
}
