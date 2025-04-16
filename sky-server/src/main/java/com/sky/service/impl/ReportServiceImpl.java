package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    public TurnoverReportVO TurnoverStaticReport(LocalDate begin, LocalDate end){
        List<LocalDate> localDateList = new ArrayList<>();

        LocalDate current = begin;
        while (!current.isAfter(end)) {
            localDateList.add(current);
            current = current.plusDays(1);
        }

//    纵坐标
        List<Double> turnoverList = new ArrayList<>();

        for(LocalDate localDate : localDateList){
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);

            Double turnover = orderMapper.getSumTurnover(map); //根据日期，查找计算每天的营业额
            turnover = turnover == null? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(localDateList, ","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    /**
     * 用户数和新增同户数量统计
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO userStaticReport(LocalDate begin, LocalDate end){
        List<LocalDate> localDateList = new ArrayList<>();
//      横坐标的坐标轴数据，每个日期都添加在坐标上
        LocalDate current = begin;
        while (!current.isAfter(end)) {
            localDateList.add(current);
            current = current.plusDays(1);
        }
//      每个日期的总用户量
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> userSumList = new ArrayList<>();
        for(LocalDate localDate : localDateList){
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN); // 第一个参数为当前日期
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);

            Integer userSum = orderMapper.getUserSum(map); //根据日期，查找计算每天的用户数量
            userSum = userSum == null? 0 : userSum;
            userSumList.add(userSum);

//            新增用户
            LocalDateTime beforeEndTime = LocalDateTime.of(localDate.plusDays(-1), LocalTime.MAX);
            Map map1 = new HashMap();
            map1.put("endTime", beforeEndTime);
//            获取在这天之前的所有用户数量
            Integer oldUserSum = userMapper.getUserSum(map1);
//            获取这天之后的所有用户数量
            Map map2 = new HashMap();
            map2.put("endTime", endTime);
            Integer newUserSum = userMapper.getUserSum(map2);

            Integer add = newUserSum - oldUserSum;
            newUserList.add(add);

        }
//      每天新增的用户量

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(localDateList, ","))
                .totalUserList(StringUtils.join(userSumList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }
}
