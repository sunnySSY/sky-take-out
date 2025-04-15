package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list (ShoppingCart shoppingCart);

//    @Update("update shopping_cart set number = #{number} where user_id = #{userId}")
    void setNumberById (ShoppingCart shoppingCart);

    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            "values (#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
    void addShoppingCart(ShoppingCart shoppingCart);

    @Delete("DELETE FROM shopping_cart where user_id = #{userId}")
    void clean(ShoppingCart shoppingCart);
    void delete(ShoppingCart shoppingCart);

    void addAllShoppingCart(List<ShoppingCart> shoppingCartList);
}
