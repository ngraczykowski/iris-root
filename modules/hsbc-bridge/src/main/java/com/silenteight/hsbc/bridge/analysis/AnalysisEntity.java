package com.silenteight.hsbc.bridge.analysis;

import lombok.*;

import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto;
import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Data
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@Table(name = "hsbc_bridge_analysis")
class AnalysisEntity extends BaseEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String dataset;
  private String policy;
  private String strategy;
  private long alertsCount;
  private OffsetDateTime timeoutAt;
  @Enumerated(EnumType.STRING)
  @Setter
  private Status status = Status.IN_PROGRESS;

  AnalysisEntity(AnalysisDto analysis, OffsetDateTime timeout) {
    this.alertsCount = analysis.getAlertCount();
    this.dataset = analysis.getDataset();
    this.name = analysis.getName();
    this.policy = analysis.getPolicy();
    this.strategy = analysis.getStrategy();
    this.timeoutAt = timeout;
  }

  AnalysisDto toAnalysisDto() {
    return AnalysisDto.builder()
        .id(this.id)
        .alertCount(this.alertsCount)
        .name(this.name)
        .policy(this.policy)
        .strategy(this.strategy)
        .dataset(this.dataset)
        .build();
  }

  @Transient
  boolean isInProgress() {
    return status == Status.IN_PROGRESS;
  }

  enum Status {
    IN_PROGRESS,
    COMPLETED,
    TIMEOUT_ERROR
  }
}
