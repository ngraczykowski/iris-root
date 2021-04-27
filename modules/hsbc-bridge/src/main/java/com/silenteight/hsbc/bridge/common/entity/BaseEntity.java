package com.silenteight.hsbc.bridge.common.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static java.time.OffsetDateTime.now;

@Data
@Setter(AccessLevel.NONE)
@MappedSuperclass
public abstract class BaseEntity {

  @Column(nullable = false, updatable = false)
  @Access(AccessType.FIELD)
  @CreationTimestamp
  private OffsetDateTime createdAt = now();

  @Column(nullable = false)
  @Access(AccessType.FIELD)
  @UpdateTimestamp
  private OffsetDateTime updatedAt;

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }
}
