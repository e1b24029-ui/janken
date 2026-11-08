package oit.is.z3412.kaizi.janken.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3412.kaizi.janken.model.MatchInfo;
import oit.is.z3412.kaizi.janken.model.MatchInfoMapper;
import oit.is.z3412.kaizi.janken.model.User;
import oit.is.z3412.kaizi.janken.model.UserMapper;

@Controller
public class JankenController {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchInfoMapper matchInfoMapper;

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    List<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    List<MatchInfo> activeMatches = matchInfoMapper.selectActiveMatchInfos();
    model.addAttribute("activeMatches", activeMatches);

    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam("id") int id, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    User opponent = userMapper.selectById(id);
    model.addAttribute("opponent", opponent);

    return "match.html";
  }

  @GetMapping("/fight")
  public String fight(@RequestParam("id") int opponentId, @RequestParam("hand") String hand, Principal prin,
      ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    User loginObj = userMapper.selectByName(loginUser);
    User opponent = userMapper.selectById(opponentId);

    MatchInfo matchInfo = new MatchInfo();
    matchInfo.setUser1(loginObj.getId());
    matchInfo.setUser2(opponent.getId());
    matchInfo.setUser1Hand(hand);
    matchInfo.setIsActive(true);

    matchInfoMapper.insertMatchInfo(matchInfo);

    return "wait.html";
  }
}
