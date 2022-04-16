package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){   // Model? model에 data를 넣어서 view에 넘겨준다.
        model.addAttribute("data","hello!!");
        return "hello";     // view 이름, 자동으로 .html이 보인다.
    }
}
