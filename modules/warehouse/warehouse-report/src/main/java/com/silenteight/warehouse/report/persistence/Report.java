package com.silenteight.warehouse.report.persistence;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static com.silenteight.warehouse.report.persistence.ReportState.DONE;
import static com.silenteight.warehouse.report.persistence.ReportState.FAILED;
import static com.silenteight.warehouse.report.persistence.ReportState.GENERATING;
import static com.silenteight.warehouse.report.persistence.ReportState.NEW;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.UUID.randomUUID;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "warehouse_report")
class Report extends BaseEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "type", nullable = false)
  private String type;

  @ToString.Include
  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportState state;

  @ToString.Include
  @Column(name = "analysis")
  private String analysis;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "from_range")
  private OffsetDateTime from;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "to_range")
  private OffsetDateTime to;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "file_storage_name")
  private String fileStorageName;

  @ToString.Include
  @Column(name = "extension", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportFileExtension extension;

  static Report of(ReportRange range, String analysisType, String reportType) {
    Report report = new Report();
    report.setType(reportType);
    report.setState(NEW);
    report.setFrom(range.getFrom());
    report.setTo(range.getTo());
    report.generateFileStorageName();
    report.setAnalysis(analysisType);
    report.setExtension(ReportFileExtension.CSV);
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
    setCreatedAt(OffsetDateTime.now());
    setState(DONE);
  }

  void failed() {
    assertAllowedStateChange(FAILED, NEW, GENERATING);
    setState(FAILED);
    setFileStorageName(null);
  }

  void zipped() {
    setExtension(ReportFileExtension.ZIP);
  }

  private void assertAllowedStateChange(ReportState desirable, ReportState... state) {
    if (notInState(state))
      throw new IllegalStateException(
          format("Cannot change state from %s to %s for %s Report with id: %s",
              getState(), getType(), desirable, getId()));
  }

  private boolean notInState(ReportState... allowedStates) {
    return stream(allowedStates).noneMatch(allowedState -> allowedState == getState());
  }

  ReportDto toDto() {
    return ReportDto.builder()
        .id(this.id)
        .name(this.type)
        .fileStorageName(this.fileStorageName)
        .range(ReportRange.of(this.from, this.to, this.analysis))
        .createdAt(this.createdAt)
        .state(this.state)
        .extension(this.extension)
        .build();
  }
}
