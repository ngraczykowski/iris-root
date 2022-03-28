package com.silenteight.fab.dataprep.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;

import java.io.Serializable;
import javax.persistence.*;

@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table
class AlertEntity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 2247309026523028000L;

  @Id
  @EqualsAndHashCode.Include
  @Column(nullable = false, updatable = false)
  private String discriminator;

  @Column(nullable = false, updatable = false, name = "alert_name")
  private String alertName;
}
