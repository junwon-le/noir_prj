package kr.co.noir.review;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.noir.notice.BoardRangeDTO;

@Mapper
public interface ReviewMapper {

	int selectMemberNum(String memberId);
    
    List<ReviewDomain> selectReviewByMember(BoardRangeDTO rDTO);
    int selectBoardTotalCnt(BoardRangeDTO rDTO);
    ReviewDomain selectBoardDetail(int reviewNum);
    List<ReviewDomain> selectReviewByRoom(BoardRangeDTO rDTO);
    int selectRoomReviewCnt(int roomTypeNum);
}
	
