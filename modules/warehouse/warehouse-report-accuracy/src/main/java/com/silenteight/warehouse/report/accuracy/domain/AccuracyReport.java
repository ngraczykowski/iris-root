package com.silenteight.warehouse.report.accuracy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import javax.persistence.*;

import static com.silenteight.warehouse.report.accuracy.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.FAILED;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report_accuracy")
class AccuracyReport extends BaseEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "report_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private AccuracyReportDefinition reportType;

  @ToString.Include
  @Column(name = "analysis", nullable = false)
  String analysisId;

  @ToString.Include
  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportState state;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "data")
  private String file;

  static AccuracyReport of(AccuracyReportDefinition definition, String analysisId) {
    AccuracyReport report = new AccuracyReport();
    report.state = ReportState.NEW;
    report.analysisId = analysisId;
    report.reportType = definition;
    return report;
  }

  void generating() {
    state = ReportState.GENERATING;
  }

  void done() {
    state = DONE;
  }

  void failed() {
    state = FAILED;
  }

  void storeReport(String report) {
    file = report;
  }

  String getFileName() {
    return id + reportType.getFilename();
  }
}
