package com.silenteight.warehouse.report.billing.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.billing.domain.dto.ReportDto;
import com.silenteight.warehouse.report.billing.domain.exception.WrongReportStateException;

import javax.persistence.*;

import static com.silenteight.warehouse.report.billing.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.billing.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.billing.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.billing.domain.ReportState.NEW;
import static java.util.Arrays.stream;
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
  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private ReportState state;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "data")
  private String data;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "file_name")
  private String fileName;

  static BillingReport of(String fileName) {
    BillingReport rbsReport = new BillingReport();
    rbsReport.setFileName(fileName);
    rbsReport.setState(NEW);
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

  ReportDto toDto() {
    return ReportDto.of(getFileName(), getData());
  }
}
