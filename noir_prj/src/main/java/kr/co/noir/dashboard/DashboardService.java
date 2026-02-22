package kr.co.noir.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DashboardService {
	@Autowired
	private DashboardMapper mapper;

	public DashboardDTO getDashboardData() {
		// 1. 기본 KPI 및 현황 조회
		DashboardDTO dashboard = mapper.selectDashboardKPI();
		
		// 2. 차트 데이터 조회
		List<Integer> weeklyStats = mapper.selectWeeklyReservationStats();
		List<Integer> ageStats = mapper.selectAgeDemographics();
		
		dashboard.setWeeklyReservationData(weeklyStats);
		dashboard.setAgeDemographicsData(ageStats);
		
		// (옵션) 트렌드 데이터는 DB 쿼리가 복잡해지므로, 
		// 여기서는 예시로 하드코딩하거나 별도 로직으로 계산함.
		// 실제로는 '지난달 데이터'를 조회해서 비교해야 함
		dashboard.setOccupancyTrend("UP"); 
		dashboard.setAdrTrend("UP");
		
		return dashboard;
	}
	
}//DashboardService




