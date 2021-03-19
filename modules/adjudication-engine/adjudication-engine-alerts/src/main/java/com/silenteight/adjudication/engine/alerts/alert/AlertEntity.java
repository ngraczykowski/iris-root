package com.silenteight.adjudication.engine.alerts.alert;

import lombok.*;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import org.hibernate.annotations.Fetch;

import java.time.OffsetDateTime;
import java.util.Map;
import javax.persistence.*;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromOffsetDateTime;
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
class AlertEntity extends BaseEntity implements IdentifiableEntity {

  @Id
  @Column(name = "alert_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String clientAlertIdentifier;

  @Column(nullable = false, updatable = false)
  private OffsetDateTime alertedAt;

  @Column(updatable = false, nullable = false)
  @NonNull
  @Default
  private Integer priority = 5;

  @ElementCollection(fetch = LAZY)
  @CollectionTable(joinColumns = @JoinColumn(name = "alert_id"))
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @Fetch(SUBSELECT)
  @Singular
  private Map<String, String> labels;

  @PrePersist
  void initialize() {
    if (alertedAt == null) {
      alertedAt = getCreatedAt();
    }
  }

  Alert toAlert() {
    return Alert
        .newBuilder()
        .setName("alerts/" + getId())
        .setAlertId(getClientAlertIdentifier())
        .setCreateTime(fromOffsetDateTime(getCreatedAt()))
        .setAlertTime(fromOffsetDateTime(getAlertedAt()))
        .setPriority(getPriority())
        .putAllLabels(getLabels())
        .build();
  }
}
