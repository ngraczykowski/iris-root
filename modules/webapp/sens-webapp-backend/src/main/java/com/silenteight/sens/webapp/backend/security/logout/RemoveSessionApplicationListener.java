package com.silenteight.sens.webapp.backend.security.logout;

import lombok.RequiredArgsConstructor;

import org.jasig.cas.client.session.SessionMappingStorage;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

@RequiredArgsConstructor
public class RemoveSessionApplicationListener
    implements ApplicationListener<HttpSessionDestroyedEvent> {

  private final SessionMappingStorage sessionStorage;

  @Override
  public void onApplicationEvent(HttpSessionDestroyedEvent event) {
    sessionStorage.removeBySessionById(event.getSession().getId());
  }
}
