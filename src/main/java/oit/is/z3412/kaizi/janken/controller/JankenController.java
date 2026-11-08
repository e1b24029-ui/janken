package oit.is.z3412.kaizi.janken.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3412.kaizi.janken.model.Match;
import oit.is.z3412.kaizi.janken.model.MatchMapper;
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

  @Autowired
  private MatchMapper matchMapper;

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    List<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    List<MatchInfo> activeMatches = matchInfoMapper.selectActiveMatchInfos();
    model.addAttribute("activeMatches", activeMatches);

    List<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam("id") int id, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    // 渡された id は対戦相手（または先手）のユーザーID
    User opponent = userMapper.selectById(id);
    model.addAttribute("opponent", opponent);

    return "match.html";
  }

  @GetMapping("/fight")
  @Transactional
  public String fight(@RequestParam("id") int opponentId, @RequestParam("hand") String hand, Principal prin,
      ModelMap model) {
    String loginUser = prin.getName();
    model.addAttribute("loginUser", loginUser);

    User loginObj = userMapper.selectByName(loginUser);
    if (loginObj == null) {
      return "janken.html";
    }

    // 自分宛て（user2 = 自分のID）かつ isActive = TRUE の待ちデータがあるか探す
    MatchInfo activeMatch = matchInfoMapper.selectByActiveUser2(loginObj.getId());

    if (activeMatch == null) {
      // 待ちデータがない場合 ＝ 先手としての新規待機登録
      MatchInfo matchInfo = new MatchInfo();
      matchInfo.setUser1(loginObj.getId());
      matchInfo.setUser2(opponentId);
      matchInfo.setUser1Hand(hand);
      matchInfo.setIsActive(true);
      matchInfoMapper.insertMatchInfo(matchInfo);

      return "wait.html";
    }

    // 待ちデータがある場合 ＝ 後手として参加し、試合成立！
    activeMatch.setIsActive(false);
    matchInfoMapper.updateIsActive(activeMatch);

    String user1Hand = activeMatch.getUser1Hand();
    String user2Hand = hand;

    int winner = 0; // 0は引き分け
    if (!user1Hand.equals(user2Hand)) {
      if ((user1Hand.equals("Gu") && user2Hand.equals("Choki")) ||
          (user1Hand.equals("Choki") && user2Hand.equals("Pa")) ||
          (user1Hand.equals("Pa") && user2Hand.equals("Gu"))) {
        winner = activeMatch.getUser1();
      } else {
        winner = loginObj.getId();
      }
    }

    Match match = new Match();
    match.setUser1(activeMatch.getUser1());
    match.setUser2(loginObj.getId());
    match.setUser1Hand(user1Hand);
    match.setUser2Hand(user2Hand);
    match.setWinner(winner);
    match.setIsActive(true);
    matchMapper.insertMatch(match);

    List<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

    User opponent = userMapper.selectById(activeMatch.getUser1());
    if (opponent != null) {
      model.addAttribute("opponent", opponent);
    }

    return "match.html";
  }
}
