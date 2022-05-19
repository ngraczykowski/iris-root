package com.silenteight.fab.dataprep.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.support.hibernate.StringListConverter;

import java.util.List;
import javax.persistence.*;

@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table
class AlertEntity extends BaseEntity {

  enum State {
    REGISTERED, IN_UDS
  }

  @Id
  @EqualsAndHashCode.Include
  @Column(nullable = false, updatable = false, name = "message_name")
  private String messageName;

  @Column(nullable = false, updatable = false, name = "alert_name")
  private String alertName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private State state;

  @Column(name = "match_names")
  @Convert(converter = StringListConverter.class)
  private List<String> matchNames;
}
