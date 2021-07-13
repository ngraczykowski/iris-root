package com.silenteight.warehouse.report.rbs.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;

import javax.persistence.*;

import static com.silenteight.warehouse.report.rbs.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.rbs.domain.ReportState.NEW;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report_rbs")
class Report extends BaseEntity implements IdentifiableEntity {

  private static final long serialVersionUID = -4592869339522050083L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "report_type", nullable = false)
  private ReportDefinition reportType;

  @ToString.Include
  @Column(name = "state", nullable = false)
  private ReportState state;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "data")
  private byte[] file;

  static Report of(ReportDefinition reportType) {
    Report rbsReport = new Report();
    rbsReport.setReportType(reportType);
    rbsReport.setState(NEW);
    return rbsReport;
  }

  void generating() {
    state = GENERATING;
  }

  void done() {
    state = DONE;
  }

  void storeReport(byte[] report) {
    file = report;
  }

  ReportDto toDto() {
    return ReportDto.of(reportType.getTitle(), getFile());
  }
}
