package com.silenteight.hsbc.bridge.bulk;

import lombok.*;

import com.silenteight.hsbc.bridge.common.entity.BaseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.*;

import static com.silenteight.hsbc.bridge.bulk.BulkStatus.COMPLETED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.DELIVERED;
import static com.silenteight.hsbc.bridge.bulk.BulkStatus.ERROR;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Entity
@NoArgsConstructor(access = PRIVATE)
@Table(name = "hsbc_bridge_bulk")
class Bulk extends BaseEntity {

  @Id
  @Setter(NONE)
  private String id;

  @Enumerated(value = EnumType.STRING)
  private BulkStatus status = BulkStatus.STORED;
  @Column(name = "analysis_id")
  private Long analysisId;
  private String errorMessage;
  private OffsetDateTime errorTimestamp;
  private boolean learning;

  @Setter(NONE)
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
    return nonNull(analysis);
  }

  @Transient
  void delivered() {
    this.status = DELIVERED;
  }

  @Transient
  void error(String errorMessage) {
    this.status = ERROR;
    this.errorMessage = errorMessage;
    this.errorTimestamp = OffsetDateTime.now();
  }

  @Transient
  Collection<BulkAlertEntity> getValidAlerts() {
    return alerts.stream().filter(BulkAlertEntity::isValid).collect(Collectors.toList());
  }

  @Transient
  boolean isNotCompleted() {
    return status != COMPLETED;
  }
}
