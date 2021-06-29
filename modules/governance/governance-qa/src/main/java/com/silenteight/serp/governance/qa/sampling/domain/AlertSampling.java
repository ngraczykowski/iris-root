package com.silenteight.serp.governance.qa.sampling.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.qa.sampling.domain.dto.AlertSamplingDto;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;

import java.time.OffsetDateTime;
import javax.persistence.*;
import javax.validation.Valid;

import static com.silenteight.serp.governance.qa.sampling.domain.JobState.FAILED;
import static com.silenteight.serp.governance.qa.sampling.domain.JobState.FINISHED;
import static com.silenteight.serp.governance.qa.sampling.domain.JobState.STARTED;

@Entity
@Table(name = "governance_qa_alert_sampling")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class AlertSampling extends BaseAggregateRoot implements IdentifiableEntity  {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column
  private Integer alertsCount;

  @ToString.Include
  @Column
  private String alertsDistribution;

  @ToString.Include
  @Column(updatable = false, nullable = false)
  private OffsetDateTime startedAt;

  @ToString.Include
  @Column
  private OffsetDateTime finishedAt;

  @ToString.Include
  @Column(nullable = false, length = 25)
  @Enumerated(EnumType.STRING)
  private JobState state;

  @ToString.Include
  @Column(updatable = false, nullable = false)
  private OffsetDateTime rangeFrom;

  @ToString.Include
  @Column(updatable = false, nullable = false)
  private OffsetDateTime rangeTo;

  static AlertSampling of(@Valid DateRangeDto dateRange, OffsetDateTime startedAt) {
    AlertSampling alertSampling = new AlertSampling();
    alertSampling.setRangeFrom(dateRange.getFrom());
    alertSampling.setRangeTo(dateRange.getTo());
    alertSampling.setState(STARTED);
    alertSampling.setStartedAt(startedAt);
    return alertSampling;
  }

  AlertSamplingDto toDto() {
    return AlertSamplingDto
        .builder()
        .id(getId())
        .rangeFrom(getRangeFrom())
        .rangeTo(getRangeTo())
        .startedAt(getStartedAt())
        .finishedAt(getFinishedAt())
        .state(getState())
        .build();
  }

  void failed() {
    setState(FAILED);
  }

  void finished(OffsetDateTime finishedAt) {
    setState(FINISHED);
    setFinishedAt(finishedAt);
  }
}
