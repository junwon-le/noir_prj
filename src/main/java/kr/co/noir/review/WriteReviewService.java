package kr.co.noir.review;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WriteReviewService {

	@Autowired
    private WriteReviewDAO rDAO;
	
	@Autowired
	private ReviewMapper rm;
    
	public void registerReview(WriteReviewDTO wrDTO) throws SQLException {
	    String uploadPath = "C:/dev/upload/review/";
	    File folder = new File(uploadPath);
	    if (!folder.exists()) folder.mkdirs();

	    List<String> savedFileNames = new ArrayList<>();
	    
	    if(wrDTO.getPhotos() != null) {
	        for (MultipartFile file : wrDTO.getPhotos()) {
	            if (!file.isEmpty()) {
	                String saveName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	                try {
	                    // 파일 저장 실행
	                    File dest = new File(uploadPath + saveName);
	                    file.transferTo(dest);
	                    
	                    savedFileNames.add(saveName);
	                    System.out.println("파일덩공^^ " + dest.getAbsolutePath());
	                } catch (IOException e) {
	                    System.out.println("파일실발");
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	    
	    if(!savedFileNames.isEmpty()) {
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

