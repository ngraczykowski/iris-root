package com.silenteight.simulator.dataset.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.entity.BaseEntity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Slf4j
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table
class DatasetEntity extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 3449138657092155697L;

  @Id
  @Column(name = "id", updatable = false)
  @GeneratedValue(strategy = IDENTITY)
  @EqualsAndHashCode.Include
  @ToString.Include
  private Long id;

  @NonNull
  @ToString.Include
  @Column(name = "dataset_id", updatable = false, nullable = false)
  private UUID datasetId;

  @NonNull
  @ToString.Include
  @Column(name = "resource_name", nullable = false)
  private String resourceName;

  @NonNull
  @ToString.Include
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "initial_alert_count", nullable = false)
  private long initialAlertCount;

  @NonNull
  @ToString.Include
  @Column(name = "name", nullable = false)
  private String name;

  @ToString.Include
  @Column(name = "description")
  private String description;

  @ToString.Include
  @Column(name = "generation_date_from")
  private OffsetDateTime generationDateFrom;

  @ToString.Include
  @Column(name = "generation_date_to")
  private OffsetDateTime generationDateTo;
}
