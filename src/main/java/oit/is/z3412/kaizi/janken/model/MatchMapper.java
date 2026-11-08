package oit.is.z3412.kaizi.janken.model;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchMapper {

  @Select("SELECT * FROM matches")
  List<Match> selectAllMatches();

  // カラム5つに対してプレースホルダーも確実に5つ（#{user1}, #{user2}, #{user1Hand}, #{user2Hand},
  // #{winner}）指定する
  @Insert("INSERT INTO matches (user1, user2, user1Hand, user2Hand, winner) VALUES (#{user1}, #{user2}, #{user1Hand}, #{user2Hand}, #{winner});")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertMatch(Match match);
}
