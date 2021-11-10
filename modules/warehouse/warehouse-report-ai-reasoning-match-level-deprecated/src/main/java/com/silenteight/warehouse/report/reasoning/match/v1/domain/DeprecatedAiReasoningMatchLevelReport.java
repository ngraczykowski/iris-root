package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.reasoning.match.v1.domain.exception.WrongReportStateException;

import javax.persistence.*;

import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.DONE;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.FAILED;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.GENERATING;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.NEW;
import static java.util.Arrays.stream;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report_ai_reasoning_match_level")
class DeprecatedAiReasoningMatchLevelReport extends BaseEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "report_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private DeprecatedAiReasoningMatchLevelReportDefinition reportType;

  @ToString.Include
  @Column(name = "analysis", nullable = false)
  private String analysisId;

  @ToString.Include
  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private DeprecatedReportState state;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "file_storage_name")
  private String fileStorageName;

  static DeprecatedAiReasoningMatchLevelReport of(
      DeprecatedAiReasoningMatchLevelReportDefinition definition, String analysisId) {

    DeprecatedAiReasoningMatchLevelReport report = new DeprecatedAiReasoningMatchLevelReport();
    report.setState(NEW);
    report.setAnalysisId(analysisId);
    report.setReportType(definition);
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

  private void assertAllowedStateChange(
      DeprecatedReportState desirable, DeprecatedReportState... state) {

    if (notInState(state))
      throw new WrongReportStateException(getId(), getState(), desirable);
  }

  private boolean notInState(DeprecatedReportState... allowedStates) {
    return stream(allowedStates).noneMatch(allowedState -> allowedState == getState());
  }

  void storeReport(String report) {
    fileStorageName = report;
  }

  String getFileStorageName() {
    return id + "-" + reportType.getFilename();
  }
}
