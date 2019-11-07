package com.silenteight.sens.webapp.common.entity;

import lombok.*;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.Instant;
import java.util.Optional;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.NONE)
@MappedSuperclass
public abstract class BaseModifiableEntity extends BaseEntity {

  @Setter(AccessLevel.PACKAGE)
  @Column(insertable = false)
  @Access(AccessType.FIELD)
  @UpdateTimestamp
  @Audited
  private Instant updatedAt;

  @Getter(AccessLevel.NONE)
  @Version
  @Column(nullable = false)
  @Access(AccessType.FIELD)
  @Audited
  private Integer version;

  public Instant getLastModifyAt() {
    return Optional.ofNullable(updatedAt).orElse(getCreatedAt());
  }

  public void setCurrentTimeForUpdatedAt() {
    this.setUpdatedAt(Instant.now());
  }
}
