package com.silenteight.warehouse.report.synchronization;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import java.util.UUID;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_report")
class ReportEntity extends BaseEntity implements IdentifiableEntity {

  private static final long serialVersionUID = 4434730975040186530L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @NonNull
  @Column(name = "report_id", nullable = false, updatable = false)
  private UUID reportId;

  @NonNull
  @ToString.Include
  @Column(name = "filename", nullable = false, updatable = false)
  private String filename;

  @NonNull
  @ToString.Include
  @Column(name = "tenant", nullable = false, updatable = false)
  private String tenant;

  @NonNull
  @ToString.Include
  @Column(name = "kibana_report_instance_id", nullable = false, updatable = false)
  private String kibanaReportInstanceId;

  boolean hasTenant(String expectedTenant) {
    return this.tenant.equals(expectedTenant);
  }

  ReportDto toDto() {
    return ReportDto.builder()
        .reportId(getReportId())
        .tenant(getTenant())
        .kibanaReportInstanceId(getKibanaReportInstanceId())
        .filename(getFilename())
        .build();
  }
}
