package com.silenteight.hsbc.bridge.retention;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hsbc_bridge_data_retention_dry_run_job")
@Data
@Builder
class DryRunJobEntity {

  @Id
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  @Builder.Default
  private OffsetDateTime createdAt = OffsetDateTime.now();

  private OffsetDateTime alertsExpirationDate;
}
