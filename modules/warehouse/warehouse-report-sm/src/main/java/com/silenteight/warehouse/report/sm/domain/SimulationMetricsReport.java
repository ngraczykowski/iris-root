package com.silenteight.warehouse.report.sm.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.sm.domain.dto.ReportDto;

import javax.persistence.*;

import static com.silenteight.warehouse.report.sm.domain.ReportState.DONE;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report_sm")
class SimulationMetricsReport extends BaseEntity implements IdentifiableEntity {

  private static final long serialVersionUID = -352956072929634708L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "report_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportDefinition reportType;

  @ToString.Include
  @Column(name = "analysis", nullable = false)
  String analysisId;

  @ToString.Include
  @Column(name = "state", nullable = false)
  private ReportState state;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "data")
  private String file;

  static SimulationMetricsReport of(ReportDefinition definition, String analysisId) {
    SimulationMetricsReport report = new SimulationMetricsReport();
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

  void storeReport(String report) {
    file = report;
  }

  ReportDto toDto() {
    return ReportDto.of(reportType.getFilename(), getFile());
  }
}
