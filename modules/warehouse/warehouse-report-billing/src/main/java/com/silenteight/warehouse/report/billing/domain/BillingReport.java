package com.silenteight.warehouse.report.billing.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.warehouse.report.billing.domain.exception.WrongReportStateException;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import javax.persistence.*;

import static com.silenteight.warehouse.report.billing.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.billing.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.billing.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.billing.domain.ReportState.NEW;
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
  @Column(name = "file_storage_name")
  private String fileStorageName;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "from_range")
  private OffsetDateTime from;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "to_range")
  private OffsetDateTime to;

  static BillingReport of(ReportRange range) {
    BillingReport billingReport = new BillingReport();
    billingReport.setState(NEW);
    billingReport.setFrom(range.getFrom());
    billingReport.setTo(range.getTo());
    billingReport.generateFileStorageName();
    return billingReport;
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
}
