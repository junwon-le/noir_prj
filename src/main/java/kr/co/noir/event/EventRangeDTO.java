package kr.co.noir.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventRangeDTO {

    private int startNum = 1, endNum = 10;     // 시작번호, 끝번호
    private String field, keyword;             // (호환용) field는 안 쓰지만 남겨둠
    private String fieldStr;                   // 검색 컬럼 문자열

    private String url;
    private int currentPage = 1;
    private int totalPage = 0;

    /**
     * ✅ 안전하게 title 검색으로 고정
     * - 기존 field 방식은 null/parseInt로 자주 터져서 관리자 이벤트는 제목검색만 사용하도록 고정
     * - Mapper에서 ${fieldStr} 쓰는 경우에도 안전
     */
    public String getFieldStr() {
        return "event_title";
    }
}