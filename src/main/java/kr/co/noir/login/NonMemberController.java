package kr.co.noir.login;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class NonMemberController {
    @Autowired private NonMemberService nonMemberService;

    @GetMapping("/nonMembers")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String searchType,
                       @RequestParam(required = false) String keyword, Model model) {
        Map<String, Object> result = nonMemberService.getNonMemberPage(page, searchType, keyword);
        model.addAllAttributes(result);
        model.addAttribute("currentPage", page);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        return "manager/manageUser/manageNonMember";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestBody List<Integer> ids) {
        return nonMemberService.removeNonMembers(ids) ? "OK" : "FAIL";
    }
}