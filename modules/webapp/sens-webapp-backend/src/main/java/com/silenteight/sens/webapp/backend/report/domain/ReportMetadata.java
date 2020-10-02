package com.silenteight.sens.webapp.backend.report.domain;

import lombok.*;

import java.time.OffsetDateTime;
import javax.persistence.*;

@Entity
@Data
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class ReportMetadata {

  @Id
  @Column(name = "reportMetadataId", updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ToString.Include
  @Column(updatable = false, nullable = false)
  private String reportName;

  @Column(updatable = false, nullable = false)
  @Access(AccessType.FIELD)
  private OffsetDateTime startTime;

  ReportMetadata(String reportName, OffsetDateTime startTime) {
    this.reportName = reportName;
    this.startTime = startTime;
  }
}
