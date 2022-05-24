package com.silenteight.simulator.dataset.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.simulator.dataset.create.DatasetLabel;
import com.silenteight.simulator.dataset.domain.exception.DatasetNotInProperStateException;
import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;
import com.silenteight.simulator.dataset.dto.DatasetDto;
import com.silenteight.simulator.dataset.dto.RangeQueryDto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.simulator.dataset.common.DatasetResource.toResourceName;
import static com.silenteight.simulator.dataset.domain.DatasetLabelName.COUNTRY;
import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
import static com.silenteight.simulator.dataset.domain.DatasetState.ARCHIVED;
import static com.silenteight.simulator.dataset.domain.DatasetState.EXPIRED;
import static java.util.Collections.emptyList;
import static javax.persistence.GenerationType.IDENTITY;

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
  @Column(name = "dataset_id", nullable = false)
  private UUID datasetId;

  @NonNull
  @ToString.Include
  @Column(name = "name", nullable = false)
  private String name;

  @ToString.Include
  @Column(name = "description")
  private String description;

  @NonNull
  @ToString.Include
  @Column(name = "external_resource_name", nullable = false)
  private String externalResourceName;

  @NonNull
  @ToString.Include
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "initial_alert_count", nullable = false)
  private long initialAlertCount;

  @Builder.Default
  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DatasetState state = ACTIVE;

  @ToString.Include
  @Column(name = "generation_date_from")
  private OffsetDateTime generationDateFrom;

  @ToString.Include
  @Column(name = "generation_date_to")
  private OffsetDateTime generationDateTo;

  @ToString.Include
  @Column(name = "labels")
  private String labels;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AlertMatch alertMatch;

  void archive() {
    assertInState(ACTIVE);
    this.state = ARCHIVED;
  }

  void expire() {
    this.state = EXPIRED;
  }

  private void assertInState(DatasetState requiredState) {
    if (this.state != requiredState)
      throw new DatasetNotInProperStateException(requiredState);
  }

  DatasetDto toDto() {
    return DatasetDto.builder()
        .id(getDatasetId())
        .name(toResourceName(getDatasetId()))
        .datasetName(getName())
        .description(getDescription())
        .state(getState())
        .alertsCount(getInitialAlertCount())
        .query(toQuery())
        .createdAt(getCreatedAt())
        .createdBy(getCreatedBy())
        .alertMatch(getAlertMatch())
        .build();
  }

  private AlertSelectionCriteriaDto toQuery() {
    return AlertSelectionCriteriaDto.builder()
        .alertGenerationDate(toRange())
        .countries(getCountries())
        .build();
  }

  private List<String> getCountries() {
    return JsonConversionHelper.INSTANCE
        .deserializeCollection(getLabels(), DatasetLabel.class)
        .stream()
        .filter(label -> label.hasName(COUNTRY))
        .findFirst()
        .map(DatasetLabel::getValues)
        .orElse(emptyList());
  }

  private RangeQueryDto toRange() {
    return RangeQueryDto.builder()
        .from(getGenerationDateFrom())
        .to(getGenerationDateTo())
        .build();
  }
}
