package com.silenteight.warehouse.report.reasoning.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.reasoning.domain.exception.WrongReportStateException;

import javax.persistence.*;

import static com.silenteight.warehouse.report.reasoning.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.reasoning.domain.ReportState.NEW;
import static java.util.Arrays.stream;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report_ai_reasoning")
class AiReasoningReport extends BaseEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "report_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private AiReasoningReportDefinition reportType;

  @ToString.Include
  @Column(name = "analysis", nullable = false)
  private String analysisId;

  @ToString.Include
  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportState state;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "data")
  private String file;

  static AiReasoningReport of(AiReasoningReportDefinition definition, String analysisId) {
    AiReasoningReport report = new AiReasoningReport();
    report.state = NEW;
    report.analysisId = analysisId;
    report.reportType = definition;
    return report;
  }

  void generating() {
    assertAllowedStateChange(GENERATING, NEW);
    state = GENERATING;
  }

  void done() {
    assertAllowedStateChange(DONE, GENERATING);
    state = DONE;
  }

  void failed() {
    assertAllowedStateChange(FAILED, NEW, GENERATING);
    state = FAILED;
  }

  private void assertAllowedStateChange(ReportState desirable, ReportState... state) {
    if (notInState(state))
      throw new WrongReportStateException(getId(), getState(), desirable);
  }

  private boolean notInState(ReportState... allowedStates) {
    return stream(allowedStates).noneMatch(allowedState -> allowedState == getState());
  }

  void storeReport(String report) {
    file = report;
  }

  String getFileName() {
    return id + "-" + reportType.getFilename();
  }
}
