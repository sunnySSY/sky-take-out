package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {
    TurnoverReportVO TurnoverStaticReport(LocalDate begin, LocalDate end);

    UserReportVO userStaticReport(LocalDate begin, LocalDate end);
}
