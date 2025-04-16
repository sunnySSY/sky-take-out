package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.service.OrderSubmitService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userOrderController")
@Slf4j
@Api(tags = "订单操作相关接口")
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderSubmitService orderSubmitService;
    @Autowired
    private OrderService orderService;
    @PostMapping("/submit")
    @ApiOperation("用户提交订单")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("提交的订单为：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderSubmitService.submit(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }

    @ApiOperation("查看历史订单")
    @GetMapping("/historyOrders")
    public Result<PageResult> checkHistory(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("传入的参数为：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.checkHistory(ordersPageQueryDTO);

        return Result.success(pageResult);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancelOrder(@PathVariable Long id){
        log.info("被取消的订单为：{}" , id);
        orderService.cancelOrder(id);

        return Result.success();
    }

    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> checkDetail(@PathVariable Long id){
        log.info("查询的订单为： {}", id);
        OrderVO orderVO = orderService.checkDetail(id);

        return Result.success(orderVO);
    }

    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result againOrder (@PathVariable Long id){
        orderService.againOrder(id);
        return Result.success();
    }
}
