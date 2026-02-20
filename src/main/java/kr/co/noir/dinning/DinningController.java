package kr.co.noir.dinning;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class DinningController {

	@Autowired
	private DinningService ds;
	
	@GetMapping("/dinning")
	public String dinning(Model model) {
		DinningDomain dDomain=null;
		dDomain=ds.searchDinning();
		
		model.addAttribute("dinning",dDomain);
		
		return "dinning/dinning";
	}
	
	@GetMapping("/dinningDetail")
	public String dinningDetail(Model model) {
		List<DinningDomain> list=null;
		list=ds.searchDetailDinning();
		
		
		
		DinningDomain dDomain=list.get(0);
		
		
		model.addAttribute("dinning",dDomain);
		model.addAttribute("dinningList",list);
		
		
		
		
		return "dinning/dinningDetail";
	}
	
	@GetMapping("/admin/dinningMgr")
	public String dinningMgr(Model model, HttpSession session) {
		//session 체크 필요
		String id = (String)session.getAttribute("adminId");
		if(id==null || "".equals(id.trim())) {
			
			return "redirect:/adminLogin";
		}
		
		
		
		List<DinningDomain> list=null;
		list=ds.searchDetailDinning();
		
		
		
		DinningDomain dDomain=list.get(0);
		
		model.addAttribute("dinning",dDomain);
		model.addAttribute("dinningList",list);
		
		return "/manager/dinning/dinningManage";
	}
	
	
	@GetMapping("/admin/dinningMgrDetail")
	public String dinningMgrDetail(Model model, HttpSession session) {
		//session 체크 필요
		String id = (String)session.getAttribute("adminId");
		if(id==null || "".equals(id.trim())) {
			
			return "redirect:/adminLogin";
		}
		
		
		
		List<DinningDomain> list=null;
		list=ds.searchDetailDinning();
		
		
		
		DinningDomain dDomain=list.get(0);
		
		model.addAttribute("dinning",dDomain);
		model.addAttribute("dinningList",list);
		return "manager/dinning/dinningManageModify";
	}
	
	@Value("${user.upload-dir}")
	private String uploadDir;

	
	@PostMapping("/admin/dinningModifyProcess")
	public String roomModfiyProcess(@RequestParam(value="dinningImgFile1") MultipartFile mf1,
			@RequestParam(value="dinningImgFile2") MultipartFile mf2,
			@RequestParam(value="dinningImgFile3") MultipartFile mf3,
			@RequestParam(value="dinningImgFile4") MultipartFile mf4,
			@RequestParam(value="dinningImgFile5") MultipartFile mf5,	
			DinningDTO dDTO, RedirectAttributes ra, HttpSession session) throws IOException {
		
		//session 체크 필요
		String id = (String)session.getAttribute("adminId");
		if(id==null || "".equals(id.trim())) {
			
			return "redirect:/adminLogin";
		}
		
		List<DinningDomain> list=null;
		list=ds.searchDetailDinning();
		
		if(mf1.getOriginalFilename()!=null && !mf1.getOriginalFilename().isEmpty()) {
			String fileName1 =UUID.randomUUID()+"-"+mf1.getOriginalFilename();
			File upFile1 = new File(uploadDir+fileName1);
			mf1.transferTo(upFile1);
			dDTO.setDinningImg1(fileName1);
		}else {
			dDTO.setDinningImg1(list.get(0).getDinningImg1());
			
		}
		
		
		
		
		if(mf2.getOriginalFilename()!=null && !mf2.getOriginalFilename().isEmpty()) {
			String fileName2 =UUID.randomUUID()+"-"+mf2.getOriginalFilename();
			File upFile2 = new File(uploadDir+fileName2);
			mf2.transferTo(upFile2);
			dDTO.setDinningImg2(fileName2);
		}else {
			dDTO.setDinningImg2(list.get(0).getDinningImg2());
		}
		if(mf3.getOriginalFilename()!=null && !mf3.getOriginalFilename().isEmpty()) {
			String fileName3 =UUID.randomUUID()+"-"+mf3.getOriginalFilename();
			File upFile3 = new File(uploadDir+fileName3);
			mf3.transferTo(upFile3);
			dDTO.setDinningImg3(fileName3);
		}else {
			dDTO.setDinningImg3(list.get(0).getDinningImg3());
		}
		
		if(mf4.getOriginalFilename()!=null && !mf4.getOriginalFilename().isEmpty()) {
			String fileName4=UUID.randomUUID()+"-"+mf4.getOriginalFilename();
			File upFile4 = new File(uploadDir+fileName4);
			mf4.transferTo(upFile4);
			dDTO.setDinningImg4(fileName4);
		}else {
			dDTO.setDinningImg4(list.get(0).getDinningImg4());
		}
		
		if(mf5.getOriginalFilename()!=null && !mf5.getOriginalFilename().isEmpty()) {
			String fileName5=UUID.randomUUID()+"-"+mf5.getOriginalFilename();
			File upFile5 = new File(uploadDir+fileName5);
			mf5.transferTo(upFile5);
			dDTO.setDinningImg5(fileName5);			
		}else {
			dDTO.setDinningImg5(list.get(0).getDinningImg5());
		}
		
		if(dDTO.getDinningEx()==null) {
			dDTO.setDinningEx(list.get(0).getDinningEx());
		}
		
		if(dDTO.getDinningDetail()==null) {
			dDTO.setDinningDetail(list.get(0).getDinningDetail());
		}
		
		
		int cnt = ds.modifyDinning(dDTO);
		
		if (cnt == 8) {
	        ra.addFlashAttribute("msg", "다이닝 정보가 정상적으로 수정되었습니다.");
	    } else {
	        ra.addFlashAttribute("msg", "다이닝 정보 수정 중 오류가 발생했습니다.");
	    }
	
		
		
		return "redirect:/admin/dinningMgr";
	}
	
}
