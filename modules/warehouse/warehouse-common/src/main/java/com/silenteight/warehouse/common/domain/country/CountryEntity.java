package com.silenteight.warehouse.common.domain.country;

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
@Table(name = "warehouse_country")
public class CountryEntity extends BaseEntity implements IdentifiableEntity {

  private static final long serialVersionUID = -7733833790757656679L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "id", updatable = false)
  @ToString.Include
  @Setter(AccessLevel.PUBLIC)
  private Long id;

  @ToString.Include
  @Column(name = "country_group_id", nullable = false)
  private UUID countryGroupId;

  @ToString.Include
  @Column(name = "country", nullable = false)
  private String country;
}
