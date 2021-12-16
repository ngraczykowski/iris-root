package com.silenteight.warehouse.report.rbs.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.rbs.domain.exception.WrongReportStateException;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static com.silenteight.warehouse.report.rbs.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.NEW;
import static java.util.Arrays.stream;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report_rbs")
class RbsReport extends BaseEntity implements IdentifiableEntity {

  private static final long serialVersionUID = -4592869339522050083L;
  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String REPORT_TYPE = "RB_SCORER";
  private static final String REPORT_TITLE = "RB Scorer";

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
  @Column(name = "from_range")
  private OffsetDateTime from;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "to_range")
  private OffsetDateTime to;

  static RbsReport of(ReportRange range) {
    RbsReport rbsReport = new RbsReport();
    rbsReport.setState(NEW);
    rbsReport.setFrom(range.getFrom());
    rbsReport.setTo(range.getTo());
    return rbsReport;
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

  static ReportTypeDto toSimulationReportTypeDto(String analysisId) {
    return ReportTypeDto
        .builder()
        .name(getReportName(analysisId))
        .type(REPORT_TYPE)
        .title(REPORT_TITLE)
        .build();
  }

  private static String getReportName(String analysisId) {
    return "analysis/" + analysisId + "/reports/" + REPORT_TYPE;
  }
}
