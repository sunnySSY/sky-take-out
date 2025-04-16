package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.*;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    public PageResult checkHistory(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Orders order = new Orders();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId()); //确保只查找该用户发订单
        BeanUtils.copyProperties(ordersPageQueryDTO, order);

        Page<Orders> pageList = orderMapper.pageSelect(order); //查找符合要求的实体类
        List<OrderVO> list = new ArrayList();
//      注意响应格式不然前端，不显示
        // 查询出订单明细，并封装入OrderVO进行响应
        if (pageList != null && pageList.getTotal() > 0) {
            for (Orders orders : pageList) {
                Long orderId = orders.getId();// 两个数据库的联系：订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);  //前端 + 数据库

                list.add(orderVO);
            }
        }

//       分页结果实体的封装
        Long total = pageList.getTotal();

        PageResult pageResult = new PageResult(total, list);
        return pageResult;
    }

    /**
     * 注释
     * @param id
     */
    public void cancelOrder(Long id){
        // 不是删除，是更新状态！！！！！！
        Orders orders = Orders.builder()
                        .id(id)
                        .status(Orders.CANCELLED)
                        .cancelTime(LocalDateTime.now())
                        .build();
        orderMapper.updateStatus(orders);
    }

    /**
     *
     * @param id
     * @return
     */
    public OrderVO checkDetail(Long id){
        Orders orders = new Orders();
        orders.setId(id);

        Orders order = orderMapper.select(orders);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO); //order本身不包含orderDetail，而是通过外键关联 ,组成了orderVo

        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 再来一单
     * @param id
     */
    public void againOrder(Long id){
        Orders orders = new Orders();
        orders.setId(id);

        //查询order 并拷贝数据
        orders = orderMapper.select(orders);
        Orders newOrders = new Orders();
        BeanUtils.copyProperties(orders, newOrders);

        newOrders.setId(null);
        newOrders.setOrderTime(LocalDateTime.now());
        newOrders.setPayStatus(Orders.UN_PAID);
        newOrders.setStatus(Orders.PENDING_PAYMENT);
        newOrders.setNumber(String.valueOf(System.currentTimeMillis())); //将时间戳设置为订单号


//        orderMapper.insert(newOrders);


        List<OrderDetail> newOrderDetailList = new ArrayList<>();
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        Long userId = BaseContext.getCurrentId(); //和线程有关
        List<ShoppingCart> shoppingCartList = new ArrayList<>();

        for(OrderDetail orderDetail : orderDetailList ){
            OrderDetail cart = new OrderDetail();
            BeanUtils.copyProperties(orderDetail, cart);
            cart.setOrderId(newOrders.getId());

            ShoppingCart shoppingCart = new ShoppingCart();

            shoppingCart.setName(cart.getName());
            shoppingCart.setNumber(cart.getNumber());
            shoppingCart.setImage(cart.getImage());
            shoppingCart.setDishFlavor(cart.getDishFlavor());
            shoppingCart.setDishId(cart.getDishId());
            shoppingCart.setAmount(cart.getAmount());
            shoppingCart.setSetmealId(cart.getSetmealId());
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            if (shoppingCart.getName() == null || shoppingCart.getAmount() == null) {
                throw new IllegalArgumentException("ShoppingCart has null values");
            }
            log.info("被加入到购物车中的数据是：{}", shoppingCart);

            newOrderDetailList.add(cart);
            shoppingCartList.add(shoppingCart);
        }

//        orderDetailMapper.insert(newOrderDetailList);
        shoppingCartMapper.addAllShoppingCart(shoppingCartList);


    }

}
