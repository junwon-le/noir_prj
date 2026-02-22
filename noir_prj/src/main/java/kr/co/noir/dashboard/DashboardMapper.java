package kr.co.noir.dashboard;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashboardMapper {
    // KPI 조회
    DashboardDTO selectDashboardKPI();
    
    // 주간 예약 차트 데이터 (월~일)
    List<Integer> selectWeeklyReservationStats();
    
    // 연령대별 통계 (20대~60대 이상)
    List<Integer> selectAgeDemographics();
}