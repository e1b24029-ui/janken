package oit.is.z3412.kaizi.janken.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3412.kaizi.janken.model.Entry;
import oit.is.z3412.kaizi.janken.model.Janken;
import oit.is.z3412.kaizi.janken.model.Match;
import oit.is.z3412.kaizi.janken.model.MatchInfo;
import oit.is.z3412.kaizi.janken.model.MatchInfoMapper;
import oit.is.z3412.kaizi.janken.model.MatchMapper;
import oit.is.z3412.kaizi.janken.model.User;
import oit.is.z3412.kaizi.janken.model.UserMapper;
import oit.is.z3412.kaizi.janken.service.AsyncKekka;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchMapper matchMapper;

  @Autowired
  private MatchInfoMapper matchInfoMapper;

  @Autowired
  private AsyncKekka asyncKekka;

  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("loginUser", loginUser);

    List<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    List<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

    List<MatchInfo> activeMatches = matchInfoMapper.selectActiveMatchInfos();
    model.addAttribute("activeMatches", activeMatches);

    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam("id") int id, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    User opponent = userMapper.selectById(id);
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("opponent", opponent);

    return "match.html";
  }

  @PostMapping("/fight")
  public String fight(@RequestParam("id") int id, @RequestParam("hand") String hand, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    User loginObj = userMapper.selectByName(loginUser);

    if (loginObj == null) {
      return "janken.html";
    }

    // ★修正ポイント1: 画面から渡された id（先攻のID）と、現在のログインユーザー（後手ID）の両方から
    // 確実にアクティブな対戦情報を1件特定する
    MatchInfo activeMatch = matchInfoMapper.selectByUsers(id, loginObj.getId());

    // もし見つからない場合の予備検索
    if (activeMatch == null) {
      activeMatch = matchInfoMapper.selectByActiveUser2(loginObj.getId());
    }

    if (activeMatch != null) {
      // --- 【後手としての処理：試合成立】 ---
      String user1Hand = activeMatch.getUser1Hand();
      User user1Obj = userMapper.selectById(activeMatch.getUser1());

      // 勝敗判定
      String winner = Janken.judgeMatch(user1Hand, hand, user1Obj.getName(), loginObj.getName());

      // 試合結果を matches テーブルに保存
      Match match = new Match();
      match.setUser1(activeMatch.getUser1());
      match.setUser2(loginObj.getId());
      match.setUser1Hand(user1Hand);
      match.setUser2Hand(hand);
      match.setWinner(winner);
      matchMapper.insertMatch(match);

      // ★修正ポイント2: コントローラー側で勝手に isActive = false にせず、
      // データベースに残したままにしておく（AsyncKekka側に安全に検知させるため）
      // activeMatch.setIsActive(false);
      // matchInfoMapper.updateIsActive(activeMatch);

      model.addAttribute("loginUser", loginUser);
      model.addAttribute("match", match);
      model.addAttribute("opponent", user1Obj);
      List<Match> matches = matchMapper.selectAllMatches();
      model.addAttribute("matches", matches);

      return "match.html";
    } else {
      // --- 【先攻としての処理：新規登録して待機画面へ】 ---
      MatchInfo matchInfo = new MatchInfo();
      matchInfo.setUser1(loginObj.getId());
      matchInfo.setUser2(id);
      matchInfo.setUser1Hand(hand);
      matchInfo.setIsActive(true);
      matchInfoMapper.insertMatchInfo(matchInfo);

      model.addAttribute("loginUser", loginUser);
      return "wait.html";
    }
  }

  @GetMapping("/sample4/step3")
  public SseEmitter sample4Test(Principal prin) {
    return asyncKekka.syncKekka(prin.getName());
  }
}
