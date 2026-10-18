package oit.is.z3412.kaizi.janken.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Entry {
  private final List<String> users = Collections.synchronizedList(new ArrayList<>());

  public void addUser(String username) {
    if (!users.contains(username)) {
      users.add(username);
    }
  }

  public List<String> getUsers() {
    return users;
  }
}
