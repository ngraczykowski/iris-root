package com.silenteight.simulator.dataset.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;
import com.silenteight.simulator.dataset.dto.DatasetDto;
import com.silenteight.simulator.dataset.dto.RangeQueryDto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.*;

import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;
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
  @Column(name = "dataset_name", nullable = false)
  private String datasetName;

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

  @Builder.Default
  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DatasetState state = CURRENT;

  @ToString.Include
  @Column(name = "generation_date_from")
  private OffsetDateTime generationDateFrom;

  @ToString.Include
  @Column(name = "generation_date_to")
  private OffsetDateTime generationDateTo;

  static DatasetDto toDto(DatasetEntity datasetEntity) {
    return DatasetDto.builder()
        .datasetName(datasetEntity.getDatasetName())
        .name(datasetEntity.getName())
        .description(datasetEntity.getDescription())
        .state(datasetEntity.getState())
        .alertsCount(datasetEntity.getInitialAlertCount())
        .query(toQuery(datasetEntity))
        .createdAt(datasetEntity.getCreatedAt())
        .createdBy(datasetEntity.getCreatedBy())
        .build();
  }

  private static AlertSelectionCriteriaDto toQuery(DatasetEntity datasetEntity) {
    return AlertSelectionCriteriaDto.builder()
        .alertGenerationDate(toRange(datasetEntity))
        .build();
  }

  private static RangeQueryDto toRange(DatasetEntity datasetEntity) {
    return RangeQueryDto.builder()
        .from(datasetEntity.getGenerationDateFrom())
        .to(datasetEntity.getGenerationDateTo())
        .build();
  }
}
