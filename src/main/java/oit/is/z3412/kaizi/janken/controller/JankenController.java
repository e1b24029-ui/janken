package oit.is.z3412.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JankenController {

  // GETアクセス（/janken に直接来た時：AC2）
  @GetMapping("/janken")
  public String jankenGet() {
    return "janken.html";
  }

  // POSTアクセス（index.html のフォームから来た時：AC1）
  @PostMapping("/janken")
  public String jankenPost(@RequestParam String name, Model model) {
    model.addAttribute("name", name);
    return "janken.html";
  }
}
