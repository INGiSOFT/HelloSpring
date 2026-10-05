package org.example.hellospring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// MVC Controller (Model View Controller)
@Controller
public class HelloController {

    @GetMapping({"/","/index"})
    public String hello(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Integer age,
            Model model) {

        if (firstName == null) {
            firstName = "Anonymous";
        }

        model.addAttribute("firstName", firstName);
        model.addAttribute("age", age);
        model.addAttribute("javaVersion", System.getProperty("java.version"));

        // HTML page/template 'index.html'
        return "index";
    }
}
