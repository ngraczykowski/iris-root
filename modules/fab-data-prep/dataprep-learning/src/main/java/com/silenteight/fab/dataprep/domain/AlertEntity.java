package com.silenteight.fab.dataprep.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.support.hibernate.StringListConverter;

import java.io.Serializable;
import java.util.List;
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

  enum State {
    REGISTERED, IN_UDS
  }

  @Id
  @EqualsAndHashCode.Include
  @Column(nullable = false, updatable = false)
  private String discriminator;

  @Column(nullable = false, updatable = false, name = "alert_name")
  private String alertName;

  @Column(nullable = false, updatable = false, name = "message_name")
  private String messageName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private State state;

  @Column(name = "match_names")
  @Convert(converter = StringListConverter.class)
  private List<String> matchNames;
}
