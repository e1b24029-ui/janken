package oit.is.z3412.kaizi.janken.controller;

import java.security.Principal;
import oit.is.z3412.kaizi.janken.model.Entry;
import oit.is.z3412.kaizi.janken.model.Janken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JankenController {

  private final Entry entry;

  @Autowired
  public JankenController(Entry entry) {
    this.entry = entry;
  }

  @GetMapping("/janken")
  public String jankenGet(Principal principal, Model model) {
    if (principal != null) {
      String loginUser = principal.getName();
      entry.addUser(loginUser);
      model.addAttribute("loginUser", loginUser);
    }
    model.addAttribute("entries", entry.getUsers());
    return "janken.html";
  }

  @GetMapping("/janken/play")
  public String jankenPlay(@RequestParam String hand, Principal principal, Model model) {
    if (principal != null) {
      model.addAttribute("loginUser", principal.getName());
    }
    model.addAttribute("entries", entry.getUsers());

    Janken janken = new Janken(hand);
    model.addAttribute("myHand", janken.getMyHand());
    model.addAttribute("cpuHand", janken.getCpuHand());
    model.addAttribute("result", janken.getResult());
    return "janken.html";
  }
}
