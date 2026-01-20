package kr.co.noir.event;

public class EventService {

	public String pagination(EventRangeDTO erDTO) {
	    StringBuilder sb = new StringBuilder();

	    int pageNumber = 3; // 한 블록에 보여줄 페이지 수

	    int currentPage = erDTO.getCurrentPage();
	    int totalPage = erDTO.getTotalPage();

	    int startPage = ((currentPage - 1) / pageNumber) * pageNumber + 1;
	    int endPage = startPage + pageNumber - 1;
	    if (endPage > totalPage) endPage = totalPage;

	    // URL (여기서 고정)
	    String baseUrl = "/evetList";

	    // 파라미터 붙이기 함수처럼 쓰기 위해
	    java.util.function.Function<Integer, String> makeHref = (page) -> {
	        StringBuilder href = new StringBuilder();
	        href.append(baseUrl).append("?currentPage=").append(page);

	        if (erDTO.getKeyword() != null && !erDTO.getKeyword().trim().isEmpty()) {
	            href.append("&keyword=").append(erDTO.getKeyword());
	        }
	        if (erDTO.getCategory() != null && !erDTO.getCategory().trim().isEmpty()) {
	            href.append("&category=").append(erDTO.getCategory());
	        }
	        return href.toString();
	    };

	    // 이전 블록(<<)
	    if (startPage > 1) {
	        int prevPage = startPage - 1;
	        sb.append("<li class='page-item'>")
	          .append("<a class='page-link' href='").append(makeHref.apply(prevPage)).append("'>")
	          .append("&lt;&lt;")
	          .append("</a>")
	          .append("</li>");
	    } else {
	        sb.append("<li class='page-item disabled'>")
	          .append("<a class='page-link' href='#' tabindex='-1' aria-disabled='true'>")
	          .append("&lt;&lt;")
	          .append("</a>")
	          .append("</li>");
	    }

	    // 페이지 번호들
	    for (int p = startPage; p <= endPage; p++) {
	        if (p == currentPage) {
	            sb.append("<li class='page-item active' aria-current='page'>")
	              .append("<a class='page-link' href='").append(makeHref.apply(p)).append("'>")
	              .append(p)
	              .append("</a>")
	              .append("</li>");
	        } else {
	            sb.append("<li class='page-item'>")
	              .append("<a class='page-link' href='").append(makeHref.apply(p)).append("'>")
	              .append(p)
	              .append("</a>")
	              .append("</li>");
	        }
	    }

	    // 다음 블록(>>)
	    if (endPage < totalPage) {
	        int nextPage = endPage + 1;
	        sb.append("<li class='page-item'>")
	          .append("<a class='page-link' href='").append(makeHref.apply(nextPage)).append("'>")
	          .append("&gt;&gt;")
	          .append("</a>")
	          .append("</li>");
	    } else {
	        sb.append("<li class='page-item disabled'>")
	          .append("<a class='page-link' href='#' tabindex='-1' aria-disabled='true'>")
	          .append("&gt;&gt;")
	          .append("</a>")
	          .append("</li>");
	    }

	    return sb.toString(); // li들만 반환
	}

	
}
