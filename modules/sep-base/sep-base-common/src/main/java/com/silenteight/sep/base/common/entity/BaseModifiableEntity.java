package com.silenteight.sep.base.common.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.Optional;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

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
  private OffsetDateTime updatedAt;

  public OffsetDateTime getLastModifyAt() {
    return Optional.ofNullable(updatedAt).orElse(getCreatedAt());
  }

  public void setCurrentTimeForUpdatedAt() {
    this.setUpdatedAt(OffsetDateTime.now());
  }
}
