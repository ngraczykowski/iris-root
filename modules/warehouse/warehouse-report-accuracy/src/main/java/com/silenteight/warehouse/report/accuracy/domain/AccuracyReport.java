package com.silenteight.warehouse.report.accuracy.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.accuracy.domain.exception.WrongReportStateException;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static com.silenteight.warehouse.report.accuracy.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.accuracy.domain.ReportState.NEW;
import static java.util.Arrays.stream;
import static java.util.UUID.randomUUID;
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

  private static final String REPORT_TYPE = "ACCURACY";
  private static final String REPORT_TITLE = "Accuracy";

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
  @Column(name = "file_storage_name")
  private String fileStorageName;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "from_range")
  private OffsetDateTime from;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "to_range")
  private OffsetDateTime to;

  static AccuracyReport of(ReportRange range) {
    AccuracyReport report = new AccuracyReport();
    report.setState(NEW);
    report.setFrom(range.getFrom());
    report.setTo(range.getTo());
    report.generateFileStorageName();
    return report;
  }

  void generateFileStorageName() {
    setFileStorageName(randomUUID().toString());
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
    setFileStorageName(null);
  }

  private void assertAllowedStateChange(ReportState desirable, ReportState... state) {
    if (notInState(state))
      throw new WrongReportStateException(getId(), getState(), desirable);
  }

  private boolean notInState(ReportState... allowedStates) {
    return stream(allowedStates).noneMatch(allowedState -> allowedState == getState());
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
