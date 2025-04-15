package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderVO;

public interface OrderService {
//    PageResult checkHistory(int page, int pageSize, Integer status);
    PageResult checkHistory(OrdersPageQueryDTO ordersPageQueryDTO);

    void cancelOrder(Long id);

    OrderVO checkDetail(Long id);

    void againOrder(Long id);
}
