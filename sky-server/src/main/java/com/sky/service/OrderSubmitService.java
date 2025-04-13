package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrderSubmitService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
}
