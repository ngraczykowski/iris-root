/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.application.logging;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import static com.silenteight.sens.webapp.logging.SensWebappMdcKeys.USERNAME;

@Slf4j
class AuthenticationSuccessLogger implements ApplicationListener<AuthenticationSuccessEvent> {

  @Override
  public void onApplicationEvent(AuthenticationSuccessEvent event) {
    String name = event.getAuthentication().getName();

    MDC.put(USERNAME.getKey(), name);
  }
}
