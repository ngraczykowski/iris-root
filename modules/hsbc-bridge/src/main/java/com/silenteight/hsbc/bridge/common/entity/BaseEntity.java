package com.silenteight.hsbc.bridge.common.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import javax.persistence.*;

@Data
@Setter(AccessLevel.NONE)
@MappedSuperclass
public abstract class BaseEntity {

  @Column(nullable = false, updatable = false)
  @Access(AccessType.FIELD)
  @CreationTimestamp
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(nullable = false, updatable = false)
  @Access(AccessType.FIELD)
  private OffsetDateTime updatedAt = OffsetDateTime.now();

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  @PrePersist
  void prePersist() {
    updatedAt = OffsetDateTime.now();
  }
}
