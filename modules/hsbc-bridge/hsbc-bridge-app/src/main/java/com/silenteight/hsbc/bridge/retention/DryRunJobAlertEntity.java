package com.silenteight.hsbc.bridge.retention;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hsbc_bridge_data_retention_dry_run_job_alerts")
@Data
@Builder
class DryRunJobAlertEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long jobId;

  private String alertName;
}
