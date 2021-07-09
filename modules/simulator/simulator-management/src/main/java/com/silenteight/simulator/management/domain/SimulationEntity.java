package com.silenteight.simulator.management.domain;

import lombok.*;
import lombok.Builder.Default;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.simulator.management.domain.exception.SimulationNotInProperStateException;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

import static com.silenteight.simulator.management.domain.SimulationState.*;

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

  @Default
  @ToString.Include
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SimulationState state = NEW;

  @ToString.Include
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @ToString.Include
  @Column(name = "started_at")
  private OffsetDateTime startedAt;

  @ToString.Include
  @Column(name = "finished_at")
  private OffsetDateTime finishedAt;

  void run() {
    assertInState(PENDING);
    this.state = RUNNING;
  }

  void finish() {
    assertInState(RUNNING);
    this.state = DONE;
  }

  void archive() {
    assertInState(DONE);
    this.state = ARCHIVED;
  }

  private void assertInState(SimulationState requiredState) {
    if (this.state != requiredState)
      throw new SimulationNotInProperStateException(requiredState);
  }
}
