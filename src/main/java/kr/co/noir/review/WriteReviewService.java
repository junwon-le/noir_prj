package kr.co.noir.review;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WriteReviewService {

	@Autowired
    private WriteReviewDAO rDAO;
	
	@Autowired
	private ReviewMapper rm;
    
	@Value("${user.upload-dir}")
    private String baseUploadPath;

    public void registerReview(WriteReviewDTO wrDTO) throws SQLException {
        // baseUploadPath를 사용하고, 리뷰 전용 하위 폴더를 붙여줍니다.
        String uploadPath = baseUploadPath + "review/"; 
        
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            folder.mkdirs(); // 폴더가 없으면 생성
        }

        List<String> savedFileNames = new ArrayList<>();
        
        if(wrDTO.getPhotos() != null) {
            for (MultipartFile file : wrDTO.getPhotos()) {
                if (!file.isEmpty()) {
                    String saveName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    try {
                        File dest = new File(uploadPath + saveName);
                        file.transferTo(dest);
                        
                        // DB에는 실제 전체 경로가 아닌 파일명(또는 상대경로)만 저장하는 것이 관리에 좋습니다.
                        savedFileNames.add(saveName);
                        System.out.println("파일 저장 성공: " + dest.getAbsolutePath());
                    } catch (IOException e) {
                        System.err.println("파일 저장 실패: " + file.getOriginalFilename());
                        e.printStackTrace();
                    }
                }
            }
        }
        
        if(!savedFileNames.isEmpty()) {
            // 이미지들을 콤마로 구분해서 DTO에 세팅
            wrDTO.setReviewImg1(String.join(",", savedFileNames));
        }
        
        rDAO.insertReview(wrDTO);
        if(wrDTO.getReviewImg1() != null) {
            rDAO.insertReviewImg(wrDTO);
        }
    }
	
	public int getMemberNumById(String memberId) {
	    return rm.selectMemberNum(memberId);
	}
	
    
	public List<WriteReviewDTO> getUnreviewedRooms(int memberNum) {
	    return rDAO.getUnreviewedRooms(memberNum);
	}
    
}

