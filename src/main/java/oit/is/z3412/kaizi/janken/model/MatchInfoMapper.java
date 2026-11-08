package oit.is.z3412.kaizi.janken.model;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchInfoMapper {

  @Select("SELECT * FROM matchinfo WHERE isActive = TRUE")
  List<MatchInfo> selectActiveMatchInfos();

  @Select("SELECT * FROM matchinfo WHERE user1 = #{user1} AND user2 = #{user2} AND isActive = TRUE")
  MatchInfo selectByUsers(int user1, int user2);

  @Select("SELECT * FROM matchinfo WHERE user2 = #{user2} AND isActive = TRUE")
  MatchInfo selectByActiveUser2(int user2);

  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, TRUE);")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertMatchInfo(MatchInfo matchInfo);

  @Update("UPDATE matchinfo SET isActive = #{isActive} WHERE id = #{id}")
  void updateIsActive(MatchInfo matchInfo);
}
