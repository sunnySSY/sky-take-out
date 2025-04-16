package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Api(tags = "统计接口")
@RestController
@RequestMapping ("/admin/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @ApiOperation("营业额统计")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStaticReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin ,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){

        log.info("开始日期为： {}, 结束日期为：{}", begin, end);
        TurnoverReportVO turnoverReportVO = reportService.TurnoverStaticReport(begin, end);
        return Result.success(turnoverReportVO);
    }

    @ApiOperation("用户统计")
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStaticReport(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin ,
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end){

        log.info("开始日期为： {}, 结束日期为：{}", begin, end);
        UserReportVO userReportVO = reportService.userStaticReport(begin, end);
        return Result.success(userReportVO);
    }
}
