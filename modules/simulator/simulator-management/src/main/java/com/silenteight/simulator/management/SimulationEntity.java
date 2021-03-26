package com.silenteight.simulator.management;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.simulator.management.dto.SimulationDto;
import com.silenteight.simulator.management.dto.SimulationState;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

@Entity
@Data
@Setter(AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class SimulationEntity extends BaseEntity implements IdentifiableEntity, Serializable {

  private static final long serialVersionUID = 1696925601371419382L;

  @Id
  @Setter(AccessLevel.PUBLIC)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(updatable = false)
  private Long id;

  @ToString.Include
  @Column(name = "simulation_id", nullable = false)
  private UUID simulationId;

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  @Column(nullable = false)
  private String description;

  @NonNull
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "simulator_simulation_dataset_name",
      joinColumns = @JoinColumn(name = "simulation_id", referencedColumnName = "simulation_id"))
  @Column(name = "dataset_name", nullable = false)
  private Set<String> datasetNames;

  @ToString.Include
  @Column(name = "model_name", nullable = false)
  private String modelName;

  @ToString.Include
  @Column(name = "analysis_name", nullable = false)
  private String analysisName;

  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SimulationState state;

  @ToString.Include
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "started_at")
  private OffsetDateTime startedAt;

  @ToString.Include
  @Column(name = "finished_at")
  private OffsetDateTime finishedAt;

  static SimulationDto toDto(SimulationEntity simulationEntity) {
    List<String> datasetNames = new ArrayList<>(simulationEntity.getDatasetNames());
    if (datasetNames.size() != 1) {
      String msg = String.format(
          "Each simulation needs to have exactly one datasetName assigned. Found = %s",
          datasetNames);
      throw new IllegalStateException(msg);
    }
    String datasetName = datasetNames.get(0);

    return SimulationDto.builder()
        .id(simulationEntity.getSimulationId())
        .name(simulationEntity.getName())
        .status(simulationEntity.getState())
        .datasetName(datasetName)
        .modelName(simulationEntity.getModelName())
        .createdAt(simulationEntity.getCreatedAt())
        .createdBy(simulationEntity.getCreatedBy())
        .build();
  }
}
