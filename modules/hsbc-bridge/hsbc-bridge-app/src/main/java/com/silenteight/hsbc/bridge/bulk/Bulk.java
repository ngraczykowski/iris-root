package com.silenteight.hsbc.bridge.bulk;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hsbc_bridge_bulk")
class Bulk extends BaseEntity {

  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  @Enumerated(value = EnumType.STRING)
  private BulkStatus status = BulkStatus.STORED;
  @Column(name = "analysis_id")
  private Long analysisId;
  private String errorMessage;
  private OffsetDateTime errorTimestamp;
  private boolean learning;

  @Setter(AccessLevel.NONE)
  @OneToMany
  @JoinColumn(name = "bulk_id")
  private Collection<BulkAlertEntity> alerts = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "analysis_id", insertable = false, updatable = false)
  private BulkAnalysisEntity analysis;

  Bulk(String id) {
    this.id = id;
  }

  Bulk(String id, boolean learning) {
    this.id = id;
    this.learning = learning;
  }

  @Transient
  boolean hasAnalysis() {
    return Objects.nonNull(analysis);
  }

  @Transient
  void delivered() {
    this.status = BulkStatus.DELIVERED;
  }

  @Transient
  void error(String errorMessage) {
    this.status = BulkStatus.ERROR;
    this.errorMessage = errorMessage;
    this.errorTimestamp = OffsetDateTime.now();
  }

  @Transient
  Collection<BulkAlertEntity> getValidAlerts() {
    return alerts.stream().filter(BulkAlertEntity::isValid).collect(Collectors.toList());
  }

  @Transient
  boolean isNotCompleted() {
    return status != BulkStatus.COMPLETED;
  }
}
