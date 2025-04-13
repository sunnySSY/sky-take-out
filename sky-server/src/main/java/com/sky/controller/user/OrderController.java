package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderSubmitService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userOrderController")
@Slf4j
@Api(tags = "订单操作相关接口")
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderSubmitService orderSubmitService;
    @PostMapping("/submit")
    @ApiOperation("用户提交订单")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("提交的订单为：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderSubmitService.submit(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }
}
