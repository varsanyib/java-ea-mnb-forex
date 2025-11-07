package hu.gamf.springlectureproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class MainController {

    @GetMapping("/")
    public String Root(Model model) {
        model.addAttribute("nowDate", new Date());
        return "index";
    }

}
