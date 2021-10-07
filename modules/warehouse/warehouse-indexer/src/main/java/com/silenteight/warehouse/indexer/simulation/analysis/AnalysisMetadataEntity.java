package com.silenteight.warehouse.indexer.simulation.analysis;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

@Data
@Setter(PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "warehouse_analysis_metadata")
class AnalysisMetadataEntity extends BaseEntity implements IdentifiableEntity {

  private static final long serialVersionUID = -6331339845762729656L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(PUBLIC)
  private Long id;

  @NonNull
  @Column(name = "analysis_id", nullable = false, updatable = false)
  private String analysisId;

  @NonNull
  @Column(name = "analysis", nullable = false, updatable = false)
  private String analysis;

  @NonNull
  @Column(name = "tenant", nullable = false, updatable = false)
  private String tenant;

  @NonNull
  @Column(name = "elastic_index_pattern", nullable = false, updatable = false)
  private String elasticIndexPattern;

  AnalysisMetadataDto toDto() {
    return AnalysisMetadataDto.builder()
        .elasticIndexName(getElasticIndexPattern())
        .tenant(getTenant())
        .build();
  }
}
