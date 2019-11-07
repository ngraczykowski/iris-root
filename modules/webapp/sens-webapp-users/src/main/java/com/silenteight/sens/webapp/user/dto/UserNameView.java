package com.silenteight.sens.webapp.user.dto;

import lombok.Value;

import com.silenteight.sens.webapp.domain.user.User;

@Value
public class UserNameView {

  private final long id;

  private final String userName;

  public UserNameView(User user) {
    id = user.getId();
    userName = user.getUserName();
  }

  public UserNameView(long id, String userName) {
    this.id = id;
    this.userName = userName;
  }
}
