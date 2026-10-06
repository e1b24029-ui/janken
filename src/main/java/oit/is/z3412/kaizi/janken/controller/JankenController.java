package oit.is.z3412.kaizi.janken.controller;

import oit.is.z3412.kaizi.janken.model.Janken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JankenController {

    @GetMapping("/janken")
    public String jankenGet() {
        return "janken.html";
    }

    @PostMapping("/janken")
    public String jankenPost(@RequestParam String name, Model model) {
        model.addAttribute("name", name);
        return "janken.html";
    }

    // Task 2: 手をクリックした時の処理
    @GetMapping("/janken/play")
    public String jankenPlay(@RequestParam String hand, Model model) {
        Janken janken = new Janken(hand);
        model.addAttribute("myHand", janken.getMyHand());
        model.addAttribute("cpuHand", janken.getCpuHand());
        model.addAttribute("result", janken.getResult());
        return "janken.html";
    }
}
