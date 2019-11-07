package com.silenteight.sens.webapp.common.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import java.time.Instant;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@Setter(AccessLevel.NONE)
@MappedSuperclass
public abstract class BaseEntity {

  @Column(nullable = false, updatable = false)
  @Access(AccessType.FIELD)
  @CreationTimestamp
  @Audited
  private Instant createdAt = Instant.now();
}
