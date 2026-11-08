package oit.is.z3412.kaizi.janken.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3412.kaizi.janken.model.Match;
import oit.is.z3412.kaizi.janken.model.MatchInfo;
import oit.is.z3412.kaizi.janken.model.MatchInfoMapper;
import oit.is.z3412.kaizi.janken.model.MatchMapper;
import oit.is.z3412.kaizi.janken.model.User;
import oit.is.z3412.kaizi.janken.model.UserMapper;

@Service
public class AsyncKekka {

  @Autowired
  private MatchMapper matchMapper;

  @Autowired
  private MatchInfoMapper matchInfoMapper;

  @Autowired
  private UserMapper userMapper;

  // SseEmitter を返すメソッドから @Async を外し、内部のループ処理に非同期を適用する
  public SseEmitter syncKekka(String loginUser) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    User user = userMapper.selectByName(loginUser);

    // 別スレッドで非同期処理を実行する
    executeAsync(emitter, user);

    return emitter;
  }

  @Async
  private void executeAsync(SseEmitter emitter, User user) {
    try {
      boolean dbUpdated = false;
      while (!dbUpdated) {
        TimeUnit.MILLISECONDS.sleep(1000);

        List<MatchInfo> activeMatches = matchInfoMapper.selectActiveMatchInfos();
        for (MatchInfo matchInfo : activeMatches) {
          if (matchInfo.getUser1() == user.getId() || matchInfo.getUser2() == user.getId()) {
            List<Match> matches = matchMapper.selectAllMatches();
            if (matches != null && !matches.isEmpty()) {
              Match latestMatch = matches.get(matches.size() - 1);
              if (latestMatch.getUser1() == user.getId() || latestMatch.getUser2() == user.getId()) {
                emitter.send(latestMatch);
                matchInfo.setIsActive(false);
                matchInfoMapper.updateIsActive(matchInfo);
                dbUpdated = true;
                break;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      emitter.completeWithError(e);
    } finally {
      emitter.complete();
    }
  }
}
