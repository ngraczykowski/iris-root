package utils;

import lombok.RequiredArgsConstructor;

import utils.datageneration.webapp.User;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ScenarioContext {

  private static final String USER = "user";

  private final Map<String, Object> context = new HashMap<>();

  public void set(String key, Object value) {
    context.put(key, value);
  }

  public Object get(String key) {
    return context.get(key);
  }

  public User getAdminUser() {
    return (User) context.get(USER + "admin");
  }

  public User getDefaultUser() {
    return (User) context.get(USER);
  }

  public void setDefaultUser(User user) {
    set(USER, user);
  }

  public User getUser(String userLabel) {
    return (User) context.get(USER + userLabel);
  }

  public void clearScenarioContext() {
    context.clear();
  }

  public void setUser(String userLabel, User user) {
    set(USER + userLabel, user);
  }
}
