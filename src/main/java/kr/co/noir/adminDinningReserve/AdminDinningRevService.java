package kr.co.noir.adminDinningReserve;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.noir.login.LoginService;
import kr.co.noir.mypageReserve.DinningRevDetailDomain;

@Service
public class AdminDinningRevService {

    private final LoginService loginService;

	@Autowired(required = false)
	private AdminDinningRevMapper adrm;
	@Value("${user.crypto.key}")
	private String key;

	@Value("${user.crypto.salt}")
	private String salt;

    AdminDinningRevService(LoginService loginService) {
        this.loginService = loginService;
    }
	
	/**
	 * 검색된 총 게시물의 수 
	 * @param adDTO
	 * @return
	 */
	public int totalCnt(AdminRangeDTO adDTO) {
		int totalCnt=0;
		
		try {
			totalCnt=adrm.totalCount(adDTO);
					
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch 
		
		return totalCnt;
	}//totalCnt
	
	/**
	 * 한 화면에 보여줄 페이지의 수
	 * @return
	 */
	public int pageScale() {
		
		return 8;
		
	}//pageScale
	
	
	/**
	 * 총페이지의 수
	 * @param totalCount - 전체 게시물의 수 
	 * @param pageScale - 한 화면에 보여줄 게시물의 수
	 * @return
	 */
	public int totalPage(int totalCount, int pageScale) {
		return (int)Math.ceil((double)totalCount/pageScale);
		
	}//totalPage
	
	
	/**
	 * 페이지의 시작번호 구하기
	 * @param currentPage - 현재 페이지
	 * @param pageScale - 한화면에 보여줄 게기물의 수
	 * @return
	 */
	public int startNum(int currentPage , int pageScale) {
		return currentPage * pageScale-pageScale+1;
		
	}//startNum
	
	
	/**
	 * 페이지의 끝번호 구하기
	 * @param startNum - 시작번호
	 * @param pageScale - 한 화면에 보여줄 게시물의 수 
	 * @return
	 */
	public int endNum(int startNum,int pageScale) {
		return startNum+pageScale-1;
		
	}//endNum
	
	
	public List<AdminDinningRevDomain> SearchDinningRevList(AdminRangeDTO arDTO){
		List<AdminDinningRevDomain> list = null;
		
		
		try {
		
			list=adrm.selectAdminDinningRevList(arDTO);
			
			System.out.println(list);
		}catch (PersistenceException pe) {

			pe.printStackTrace();
		}//end catch
		
		return list;
		
	}//SearchDinningRevList
	
	
	public DinningRevDetailDomain serachOneDinningDetail(int reserveNum) {
		DinningRevDetailDomain drdDomain=null;
		TextEncryptor te = Encryptors.text(key, salt);
		
		try {
			
			drdDomain=adrm.selectOneAdminDinningDetail(reserveNum);
			System.out.println("아아아아아아아앙"+drdDomain);
			drdDomain.setTel(te.decrypt(drdDomain.getTel()));
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch 		
		
		return drdDomain;
		
	}//serachOneDinningDetail
	
	@Transactional
	public boolean modifyDinningRev(int reseveNum) {
		boolean flag=false;
		try {
			int revCnt=adrm.removeDinningReserve(reseveNum);
			int payCnt=adrm.removeRevPay(reseveNum);
			
			flag=(revCnt+payCnt)==2;
			
			
		}catch (PersistenceException pe) {

			pe.printStackTrace();
		}//end catch
		
		return flag;
		
	}//modifyDinningRev
	
	
	public String pagenation(AdminRangeDTO arDTO) {
		    StringBuilder pagiNation = new StringBuilder();
		    int pageNumber = 5; // 한 번에 보여줄 페이지 번호 개수
		    
		    // 시작 페이지와 끝 페이지 계산
		    int startPage = ((arDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
		    int endPage = startPage + pageNumber - 1;
		    if (endPage > arDTO.getTotalPage()) {
		        endPage = arDTO.getTotalPage();
		    }  

		    // 공통 파라미터(검색어) 미리 생성
		    String params = "";
		    if (arDTO.getKeyword() != null && !arDTO.getKeyword().isEmpty()) {
		        params = "&field=" + arDTO.getField() + "&keyword=" + arDTO.getKeyword();
		    }
		    if (arDTO.getReserveType() != null) {
		        params += "&reserveType=" + arDTO.getReserveType();
		    }

		    // 1. [이전] 버튼 블록
		    if (arDTO.getCurrentPage() > pageNumber) {
		        int movePage = startPage - 1;
		        pagiNation.append("<button type='button' class='page-btn'  onclick=\"location.href='")
		                  .append(arDTO.getUrl()).append("?currentPage=").append(movePage).append(params)
		                  .append("'\"><i class='fa fa-chevron-left' style='font-size: 12px;'></i></button>");
		    } else {
		        // 이전으로 갈 수 없을 때 (비활성화)
		        pagiNation.append("<button type='button' class='page-btn' disabled><i class='fa fa-chevron-left' style='font-size: 12px;'></i></button>");
		    }

		    // 2. [숫자] 버튼 블록 (1 2 3 ... 10)
		    for (int i = startPage; i <= endPage; i++) {
		        if (i == arDTO.getCurrentPage()) {
		            // 현재 보고 있는 페이지 (active 클래스 추가)
		            pagiNation.append("<button type='button'  class='page-btn active'>").append(i).append("</button>");
		        } else {
		            pagiNation.append("<button type='button' class='page-btn' onclick=\"location.href='")
		                      .append(arDTO.getUrl()).append("?currentPage=").append(i).append(params)
		                      .append("'\">").append(i).append("</button>");
		        }
		    }

		    // 3. [다음] 버튼 블록
		    if (endPage < arDTO.getTotalPage()) {
		        int movePage = endPage + 1;
		        pagiNation.append("<button type='button' class='page-btn' onclick=\"location.href='")
		                  .append(arDTO.getUrl()).append("?currentPage=").append(movePage).append(params)
		                  .append("'\"><i class='fa fa-chevron-right' style='font-size: 12px;'></i></button>");
		    } else {
		        // 다음으로 갈 수 없을 때 (비활성화)
		        pagiNation.append("<button type='button' class='page-btn' disabled><i class='fa fa-chevron-right' style='font-size: 12px;'></i></button>");
		    }

		    return pagiNation.toString();
		
	}//pagenation
	
}//class
