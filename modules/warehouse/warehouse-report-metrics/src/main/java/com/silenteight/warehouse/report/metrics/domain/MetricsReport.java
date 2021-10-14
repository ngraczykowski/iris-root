package com.silenteight.warehouse.report.metrics.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;
import com.silenteight.warehouse.report.metrics.domain.exception.WrongReportStateException;
import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import javax.persistence.*;

import static com.silenteight.warehouse.report.metrics.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.NEW;
import static java.util.Arrays.stream;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report_metrics")
class MetricsReport extends BaseEntity implements IdentifiableEntity {

  private static final long serialVersionUID = -3729478842682162958L;
  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String REPORT_TYPE = "METRICS";

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportState state;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "data")
  private String data;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "file_name")
  private String fileName;

  static MetricsReport of(String fileName) {
    MetricsReport report = new MetricsReport();
    report.setState(NEW);
    report.setFileName(fileName);
    return report;
  }

  void generating() {
    assertAllowedStateChange(GENERATING, NEW);
    setState(GENERATING);
  }

  void done() {
    assertAllowedStateChange(DONE, GENERATING);
    setState(DONE);
  }

  void failed() {
    assertAllowedStateChange(FAILED, NEW, GENERATING);
    setState(FAILED);
  }

  private void assertAllowedStateChange(ReportState desirable, ReportState... state) {
    if (notInState(state))
      throw new WrongReportStateException(getId(), getState(), desirable);
  }

  private boolean notInState(ReportState... allowedStates) {
    return stream(allowedStates).noneMatch(allowedState -> allowedState == getState());
  }

  void storeReport(String report) {
    setData(report);
  }

  ReportDto toDto() {
    return ReportDto.of(getFileName(), getData());
  }

  static ReportTypeDto toSimulationReportTypeDto(String analysisId) {
    return ReportTypeDto
        .builder()
        .name(getReportName(analysisId))
        .type(REPORT_TYPE)
        .build();
  }

  private static String getReportName(String analysisId) {
    return "analysis/" + analysisId + "/reports/" + REPORT_TYPE;
  }
}
