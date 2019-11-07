package com.silenteight.sens.webapp.common.audit;

import com.silenteight.sens.webapp.common.entity.AuditRevision;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

public class AuditRevisionListener implements RevisionListener {

  private static final String SECURITY_CONTEXT_HOLDER_CLASS =
      "org.springframework.security.core.context.SecurityContextHolder";

  @Override
  public void newRevision(Object revisionEntity) {
    ((AuditRevision) revisionEntity).setUserName(getUserName());
  }

  private static String getUserName() {
    String userName = System.getProperty("user.name");
    if (ClassUtils.isPresent(SECURITY_CONTEXT_HOLDER_CLASS, ClassUtils.getDefaultClassLoader())) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null)
        userName = authentication.getName();
    }
    return userName;
  }
}
