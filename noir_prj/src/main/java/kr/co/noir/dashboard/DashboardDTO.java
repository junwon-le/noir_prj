package kr.co.noir.dashboard;

import java.util.List;

import lombok.Data;

@Data
public class DashboardDTO {
    // 1. KPI 지표
    private double occupancyRate;       // 객실 점유율
    private String occupancyTrend;      // 전주 대비 등락 (예: "UP", "DOWN")
    
    private long adr;                   // 평균 객실 요금 (Average Daily Rate)
    private String adrTrend;
    
    private double avgLeadTime;         // 평균 리드 타임
    
    private double satisfactionScore;   // 고객 만족도 (평점)
    
    private long diningRevenue;         // 다이닝 매출
    private String diningTrend;
    
    private double cancelRate;          // 예약 취소율
    private String cancelTrend;

    // 2. 실시간 현황 (객실 가용성 및 투숙객)
    private int currentGuests;          // 현재 투숙객 수
    private int checkInCount;           // 오늘 체크인 예정
    private int checkOutCount;          // 오늘 체크아웃 예정
    private int occupiedRooms;          // 사용 중인 객실 수
    private int availableRooms;         // 가용 객실 수

    // 3. 차트용 데이터 (List 형태)
    // 주간 예약 현황 (월~일)
    private List<Integer> weeklyReservationData; 
    
    // 연령대 분석 (20대~60대+)
    private List<Integer> ageDemographicsData;
}