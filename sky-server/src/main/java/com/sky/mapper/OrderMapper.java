package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders order);

    Page<Orders> pageSelect(Orders order);

    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO); //me

    @Delete("delete from orders where id = #{id}")
    void delete(Long id);

    @Select("select * from orders where id = #{id}")
    OrderVO select(Orders orders);

    @Update("update orders set status = #{status} , cancel_time = #{cancelTime} where id = #{id}")
    void updateStatus(Orders orders);

    @Select("select * from orders where status = #{status} and order_time = #{OrderTime}")
    List<Orders> checkOrderTime(Integer status, LocalDateTime OrderTime);

    /**
     * 动态方式统计营业额数据
     * @param map
     * @return
     */
    Double getSumTurnover(Map map);

    Integer getUserSum(Map map);
}
