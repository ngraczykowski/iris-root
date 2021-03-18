package com.silenteight.adjudication.engine.alerts.alert;

import lombok.*;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.sep.base.common.entity.BaseEntity;

import org.hibernate.annotations.Fetch;

import java.time.OffsetDateTime;
import java.util.Map;
import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;
import static org.hibernate.annotations.FetchMode.SUBSELECT;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "Alert")
@Builder(access = PACKAGE)
class AlertEntity extends BaseEntity {

  @Id
  @Column(name = "alertId", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String clientAlertIdentifier;

  @Column(nullable = false, updatable = false)
  @NonNull
  private OffsetDateTime alertedAt;

  @Column(updatable = false, nullable = false)
  @NonNull
  @Default
  private Integer priority = 5;

  @ElementCollection(fetch = LAZY)
  @CollectionTable(joinColumns = @JoinColumn(name = "alertId"))
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @Fetch(SUBSELECT)
  @Singular
  private Map<String, String> labels;
}
