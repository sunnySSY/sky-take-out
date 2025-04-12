package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorsMapper {
    void addFlavors(List<DishFlavor> flavors);


    void delete(List<Long> ids);

    void update(List<DishFlavor> flavors);

    List<DishFlavor> getByDishId(Long id);
}
