package com.silenteight.sens.webapp.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class TestDataFacade {

  @Autowired
  private UsersDataInitializer usersDataInitializer;

  void initSensUsers() {
    usersDataInitializer.initSensUsers();
  }

  void cleanUpSensUsers() {
    usersDataInitializer.cleanUpSensUsers();
  }
}
