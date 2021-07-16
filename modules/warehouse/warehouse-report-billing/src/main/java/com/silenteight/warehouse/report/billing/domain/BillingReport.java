package com.silenteight.warehouse.report.billing.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.billing.domain.dto.ReportDto;

import javax.persistence.*;

import static com.silenteight.warehouse.report.billing.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.billing.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.billing.domain.ReportState.NEW;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report_billing")
class BillingReport extends BaseEntity implements IdentifiableEntity {

  private static final long serialVersionUID = -4592869339522050083L;

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
  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportState state;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "data")
  private String file;

  static BillingReport of(ReportDefinition reportType) {
    BillingReport rbsReport = new BillingReport();
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

  void storeReport(String report) {
    file = report;
  }

  ReportDto toDto() {
    return ReportDto.of(reportType.getFilename(), getFile());
  }
}
