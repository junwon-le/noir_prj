package kr.co.noir.event;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EventAdminService {

		@Qualifier("eventAdminDAO")
		@Autowired(required = false)
		private EventAdminDAO eaDAO;

		/**
		 * 전체 게시글 수
		 * 
		 * @param rDTO
		 * @return
		 */
		public int totalCnt(EventRangeDTO erDTO) {
			int totalCnt = 0;
			try {
				totalCnt = eaDAO.selectEventTotalCnt(erDTO);

			} catch (SQLException se) {
				se.printStackTrace();

			} // end catch

			return totalCnt;
		}// totalCnt

		/**
		 * 한 화면에 보여줄 페이지의 수
		 * 
		 * @return
		 */
		public int pageScale() {
			return 10;
		}// pageScale

		/**
		 * 
		 * 총 페이지수
		 * 
		 * @param totalCount - 전체 게시물의 수
		 * @param pageScale  - 한 화면에 보여줄 게시물의 수
		 * @return
		 */
		public int totalPage(int totalCount, int pageScale) {
			return (int) Math.ceil((double) totalCount / pageScale);
		}// totalPage

		/**
		 * 페이지의 시작번호 구하기
		 * 
		 * @param currentPage - 현재 페이지
		 * @param pageScale   - 한화면에 보여줄 게시물의 수
		 * @return
		 */
		public int startNum(int currentPage, int pageScale) {
			return currentPage * pageScale - pageScale + 1;
		}// startNum

		/**
		 * 페이지의 끝번호 구하기
		 * 
		 * @param startNum  - 시작번호
		 * @param pageScale - 한 화면에 보여줄 게시물의 수
		 * @return
		 */
		public int endNum(int startNum, int pageScale) {
			return startNum + pageScale - 1;
		}// endNum

//		// 이벤트 목록 조회
		/**
		 * 시작번호, 끝번호, 검색필드, 검색 키워드를 사용한 게시글 검색
		 * 
		 * @param rDTO
		 * @return
		 */
		public List<EventAdminDomain> searchEventList(EventRangeDTO erDTO) {
			List<EventAdminDomain> list = null;

			try {
				list = eaDAO.selectEventList(erDTO);
			} catch (SQLException e) {
				e.printStackTrace();
			} // end catch

			return list;
		}// searchEventList

		/**
		 * 제목이 20자를 초과하면 19자까지 보여주고 뒤에 ..을 붙이는일
		 * 
		 * @param list
		 */
		public void titleSubStr(List<EventAdminDomain> eventList) {
			String title = "";
			for (EventAdminDomain eaDTO : eventList) {
				title = eaDTO.getEventTitle();
				if (title.length() > 19) {
					eaDTO.setEventTitle(title.substring(0, 20) + "...");
						//	(title.substring(0, 20) + "...");
				} // end if
			} // end for
		}// titleSubStr

		/**
		 * 페이지네이션 [<<]...[1][2][3]...[>>]
		 * 
		 * @param rDTO
		 * @return
		 */
		public String pagination(EventRangeDTO erDTO) {
			StringBuilder pagiNation = new StringBuilder();

			// 1. 한 화면에 보여줄 pagination의 수.
			int pageNumber = 3;

			// 2. 화면에 보여줄 시작 페이지 번호를 설정 1,2,3 => 1로 시작 , 4,5,6 => 4 로시작 7,8,9 => 7로 시작
			int startPage = ((erDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;

			// 3. 화면에 보여줄 마지막 페이지 번호 1,2,3 => 3
			int endPage = (((startPage - 1) + pageNumber) / pageNumber) * pageNumber;

			// 4. 총 페이지수가 연산된 마지막 페이지수보다 작다면 총페이지 수가 마지막 페이지수로 설정
			// 456인 경우 -> 4 로 설정
			if (erDTO.getTotalPage() <= endPage) {
				endPage = erDTO.getTotalPage();
			} // end if

			// 5. 첫페이지가 인덱스 화면 (1페이지)가 아닌 경우
			int movePage = 0;
			StringBuilder prevMark = new StringBuilder();
			prevMark.append("[&lt;&lt;]");
			// prevMark.append("<li class='page-item'><a class='page-link'
			// href='#'>Previous</a></li>");
			if (erDTO.getCurrentPage() > pageNumber) {// 시작 페이지 번호가 3보다 크면
				movePage = startPage - 1;// 4,5,6 -> 3이면 시작은 1 , 7,8,9 -> 6이면 시작은 4
				prevMark.delete(0, prevMark.length());// 이전에 링크가 없는 [<<] 삭제
				prevMark.append("[<a href='").append(erDTO.getUrl()).append("?currentPage=").append(movePage);

				// 검색 키워드가 있다면 검색 키워드를 붙인다.
				if (erDTO.getKeyword() != null && !erDTO.getKeyword().isEmpty()) {
					prevMark.append("&field=").append(erDTO.getField()).append("&keyword=").append(erDTO.getKeyword());

				} // end if

				prevMark.append("' class='prevMark'>&lt;&lt;</a>]");
			} // end if

			// 6.시작페이지 번호부터 끝 번호까지 화면에 출력
			StringBuilder pageLink = new StringBuilder();
			movePage = startPage;
			while (movePage <= endPage) {
				if (movePage == erDTO.getCurrentPage()) {// 현재 페이지 : 링크 x
					pageLink.append("[ <span class='currentPage'>").append(movePage).append("</span>]");

				} else {// 현재 페이지가 아닌 다른 페이지 : 링크 O
					pageLink.append("[ <a class='notCurrentPage' href='").append(erDTO.getUrl()).append("?currentPage=")
							.append(movePage);

					// 검색 키워드가 있다면 검색 키워드를 붙인다.
					if (erDTO.getKeyword() != null && !erDTO.getKeyword().isEmpty()) {
						pageLink.append("&field=").append(erDTO.getField()).append("&keyword=").append(erDTO.getKeyword());

					} // end if

					pageLink.append("'>").append(movePage).append("</a>]");
				} // end else

				movePage++;
			} // end while

			// 7. 뒤에 페이지가 더 있는 경우
			StringBuilder nextMark = new StringBuilder("[&gt;&gt;]");
			if (erDTO.getTotalPage() > endPage) {// 뒤에 페이지가 더 있음
				movePage = endPage + 1;
				nextMark.delete(0, nextMark.length());
				nextMark.append("[ <a class='nextMark' href='").append(erDTO.getUrl()).append("?currentPage=")
						.append(movePage);

				// 검색 키워드가 있다면 검색 키워드를 붙인다.
				if (erDTO.getKeyword() != null && !erDTO.getKeyword().isEmpty()) {
					nextMark.append("&field=").append(erDTO.getField()).append("&keyword=").append(erDTO.getKeyword());

				} // end if

				nextMark.append("'> &gt;&gt; </a> ]");

			} // end if

			pagiNation.append(prevMark).append("...").append(pageLink).append("...").append(nextMark);

			return pagiNation.toString();

		}// pagination

		
		/**
		 * 상세보기
		 * 
		 * @param num
		 * @return
		 */
		public EventAdminDomain searchOneEvent(int eventNum) {
			EventAdminDomain eaDTO = null;

			try {
				eaDTO = eaDAO.selectEventDetail(eventNum);
			} catch (SQLException e) {
				e.printStackTrace();
			} // end catch

			return eaDTO;

		}// searchOneEvent


		
		//이벤트 추가
		public boolean addEvent(EventAdminDTO eaDTO) {
		    try {
		        return eaDAO.insertEvent(eaDTO) == 1;
		    } catch (PersistenceException pe) {
		        pe.printStackTrace();
		        return false;
		    }
		}

		//다른 방식 : 이벤트 추가
//		public boolean addBoard(BoardDTO bDTO) {
//			boolean flag = false;
//			
//			try {
//				bDAO.insertBoard(bDTO);
//				flag=true;
//			}catch(PersistenceException pe){
//				pe.printStackTrace();
//			}
//			return flag;
//		}
//		
		
		
		public boolean modifyEvent(EventAdminDTO eaDTO) {
			boolean flag = false;

			try {
				flag = eaDAO.updateEvent(eaDTO) == 1;
			} catch (SQLException e) {
				e.printStackTrace();
			} // end catch

			return flag;
		}// modifyEvent

		
		public boolean removeEvent(EventAdminDTO eaDTO) {
		    boolean flag = false;

		    try {
		        flag = eaDAO.deleteEvent(eaDTO) == 1;
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return flag;
		}



}//class
