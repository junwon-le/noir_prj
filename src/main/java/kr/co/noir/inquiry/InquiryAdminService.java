package kr.co.noir.inquiry;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InquiryAdminService {

	@Autowired
	private InquiryAdminDAO iaDAO;
	
    // 전체 문의 개수
    public int totalCnt(InquiryRangeDTO irDTO) {
        int totalCnt = 0;
        try {
            totalCnt = iaDAO.selectinquiryTotal(irDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCnt;
    }

    // 한 화면에 보여줄 게시물 수
    public int pageScale() {
        return 10;
    }

    // 총 페이지 수
    public int totalPage(int totalCount, int pageScale) {
        return (int) Math.ceil((double) totalCount / pageScale);
    }

    // 시작 번호
    public int startNum(int pageScale, int currentPage) {
        return pageScale * (currentPage - 1) + 1;
    }

    // 끝 번호
    public int endNum(int startNum, int pageScale) {
        return startNum + pageScale - 1;
    }
	 

	
	/**
	 * 제목이 20자를 초과하면 19자까지 보여주고 ...을 붙이는 일 
	 * @param list
	 */
	public void titleSubStr(List<InquiryAdminDomain> iaList) {
		String title="";
		for(InquiryAdminDomain irDTO:iaList){
			title=irDTO.getInquiryTitle();
			if(title !=null && title.length() > 19){
				irDTO.setInquiryTitle(title.substring(0,20)+"...");
			}//end if
		}//end for
	}


    // 문의 목록 조회
    public List<InquiryAdminDomain> searchInquiryList(InquiryRangeDTO irDTO) {
        List<InquiryAdminDomain> list = null;
        try {
            list = iaDAO.selectinquiryList(irDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 문의 상세 조회
    public InquiryAdminDomain searchInquiryDetail(int inquiryNum) {
        InquiryAdminDomain detail = null;
        try {
            detail = iaDAO.selectInquiryDetail(inquiryNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detail;
    }


    // 답변 등록/수정
    // iaDTO에는 inquiryNum + inquiryReturn이 세팅되어 있어야 함
    public boolean updateInquiryReturn(InquiryAdminDTO iaDTO) {
        boolean flag = false;
        try {
            flag = iaDAO.updateinquiry(iaDTO) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return flag;
    }

    // 삭제(논리삭제)
    public boolean removeInquiry(int inquiryNum) {
        boolean flag = false;
        try {
            flag = iaDAO.deleteinquiry(inquiryNum) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    

    public String pagination2(InquiryRangeDTO rDTO, String justify) {
        StringBuilder pagination = new StringBuilder();

        int pageNumber = 3;
        int startPage = ((rDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
        int endPage = (((startPage - 1) + pageNumber) / pageNumber) * pageNumber;

        if (rDTO.getTotalPage() <= endPage) {
            endPage = rDTO.getTotalPage();
        }

        int movePage = 0;
        StringBuilder prevMark = new StringBuilder();
        prevMark.append("<li class='page-item disabled'>");
        prevMark.append("<a class='page-link'>Previous</a>");
        prevMark.append("</li>");

        if (rDTO.getCurrentPage() > pageNumber) {
            movePage = startPage - 1;
            prevMark.delete(0, prevMark.length());
            prevMark.append("<li class='page-item'><a class='page-link' href='").append(rDTO.getUrl())
                    .append("?currentPage=").append(movePage);

            if (rDTO.getKeyword() != null && !rDTO.getKeyword().isEmpty()) {
                prevMark.append("&field=").append(rDTO.getField())
                        .append("&keyword=").append(rDTO.getKeyword());
            }

            prevMark.append("'>Previous</a></li>");
        }

        StringBuilder pageLink = new StringBuilder();
        movePage = startPage;

        while (movePage <= endPage) {
            if (movePage == rDTO.getCurrentPage()) {
                pageLink.append("<li class='page-item active page-link'>").append(movePage).append("</li>");
            } else {
                pageLink.append("<li class='page-item'><a class='page-link' href='")
                        .append(rDTO.getUrl()).append("?currentPage=").append(movePage);

                if (rDTO.getKeyword() != null && !rDTO.getKeyword().isEmpty()) {
                    pageLink.append("&field=").append(rDTO.getField())
                            .append("&keyword=").append(rDTO.getKeyword());
                }

                pageLink.append("'>").append(movePage).append("</a>");
            }
            movePage++;
        }

        StringBuilder nextMark = new StringBuilder("<li class='page-item page-link'>Next</li>");
        if (rDTO.getTotalPage() > endPage) {
            movePage = endPage + 1;
            nextMark.delete(0, nextMark.length());
            nextMark.append("<li class='page-item page-link'><a href='")
                    .append(rDTO.getUrl()).append("?currentPage=").append(movePage);

            if (rDTO.getKeyword() != null && !rDTO.getKeyword().isEmpty()) {
                nextMark.append("&field=").append(rDTO.getField())
                        .append("&keyword=").append(rDTO.getKeyword());
            }

            nextMark.append("'>Next</a></li>");
        }

        if (!("center".equals(justify) || "left".equals(justify))) {
            justify = "left";
        }

        pagination.append("<nav aria-label='...'>")
                .append("  <ul class='pagination d-flex justify-content-")
                .append(justify)
                .append("'>");

        pagination.append(prevMark).append(pageLink).append(nextMark);

        pagination.append("</ul>")
                .append("  </nav>");

        return pagination.toString();
    }
}//class
