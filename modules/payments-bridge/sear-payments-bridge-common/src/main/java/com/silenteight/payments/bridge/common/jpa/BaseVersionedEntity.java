package com.silenteight.payments.bridge.common.jpa;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseModifiableEntity;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.NONE)
@MappedSuperclass
public abstract class BaseVersionedEntity extends BaseModifiableEntity {

  @Getter(AccessLevel.NONE)
  @Version
  @Column(nullable = false)
  @Access(AccessType.FIELD)
  @Audited
  private Integer version;
}
