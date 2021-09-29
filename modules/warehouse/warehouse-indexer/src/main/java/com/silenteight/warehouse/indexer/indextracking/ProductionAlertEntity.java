package com.silenteight.warehouse.indexer.indextracking;

import lombok.*;

import com.silenteight.sep.base.common.entity.IdentifiableEntity;

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
@Table(name = "warehouse_production_alert")
class ProductionAlertEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @NonNull
  @ToString.Include
  @Column(name = "discriminator", nullable = false, updatable = false)
  private String discriminator;

  @NonNull
  @Column(name = "index_name", nullable = false, updatable = false)
  @Access(AccessType.FIELD)
  private String indexName;
}
