package com.silenteight.sens.webapp.common.support.audit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class AuditReaderProvider {

  @NonNull
  private final EntityManager entityManager;

  public AuditReader get() {
    return AuditReaderFactory.get(entityManager);
  }
}
