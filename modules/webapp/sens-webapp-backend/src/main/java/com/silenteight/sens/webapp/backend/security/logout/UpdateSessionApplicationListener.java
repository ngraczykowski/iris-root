package com.silenteight.sens.webapp.backend.security.logout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.client.session.SessionMappingStorage;
import org.springframework.context.ApplicationListener;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Slf4j
public class UpdateSessionApplicationListener
    implements ApplicationListener<SessionFixationProtectionEvent> {

  private final SessionMappingStorage sessionStorage;

  @Override
  public void onApplicationEvent(SessionFixationProtectionEvent event) {
    CasAuthenticationToken authenticationToken = getCasAuthenticationTokenOrThrow(event);
    sessionStorage.removeBySessionById(event.getOldSessionId());
    String casToken = (String) authenticationToken.getCredentials();
    HttpSession session = getCurrentSessionOrThrow();
    sessionStorage.addSessionById(casToken, session);

    if (log.isDebugEnabled()) {
      log.debug("Stored changed session ID after session fixation protection: old={}, new={}",
                event.getOldSessionId(), event.getNewSessionId());
    }
  }

  private static CasAuthenticationToken getCasAuthenticationTokenOrThrow(
      SessionFixationProtectionEvent sessionFixationEvent) {
    Authentication authentication = sessionFixationEvent.getAuthentication();

    if (authentication instanceof CasAuthenticationToken) {
      return (CasAuthenticationToken) authentication;
    } else {
      String authenticationTypeName = authentication.getClass().getSimpleName();
      throw new IllegalStateException(
          "Could not update session for " + authenticationTypeName + ", expected authentication"
              + " type to be CasAuthenticationToken");
    }
  }

  private static HttpSession getCurrentSessionOrThrow() {
    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

    if (requestAttributes instanceof ServletRequestAttributes) {
      ServletRequestAttributes attr = (ServletRequestAttributes) requestAttributes;

      return getSessionFromAttributes(attr);
    } else {
      String requestAttributesTypeName = requestAttributes.getClass().getSimpleName();

      throw new IllegalStateException(
          "Expected request attributes type: ServletRequestAttributes, found: "
              + requestAttributesTypeName);
    }
  }

  private static HttpSession getSessionFromAttributes(ServletRequestAttributes attr) {
    HttpSession session = attr.getRequest().getSession(false);

    if (session != null) {
      return session;
    } else {
      throw new IllegalStateException("Session not found after session fixation event");
    }
  }
}
