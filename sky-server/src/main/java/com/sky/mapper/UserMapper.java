package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE openid = #{openId}")
    User getByOpenId(String openId);

    void insert(User user);

    Integer getUserSum(Map map);
}
