package com.silenteight.serp.governance.featureset;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.featureset.dto.FeatureSetToStoreDto;
import com.silenteight.serp.governance.featureset.dto.FeatureSetViewDto;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
class FeatureSetEntity extends BaseEntity implements IdentifiableEntity {

  @Getter
  @Setter
  @EqualsAndHashCode.Include
  @Id
  @Column(name = "featureSetId", updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Column(updatable = false)
  private String featuresSignature;

  @Getter
  @Column(updatable = false)
  @Convert(converter = FeatureListConverter.class)
  private List<String> features = new ArrayList<>();

  FeatureSetEntity(FeatureSetToStoreDto dto) {
    featuresSignature = dto.getFeaturesSignatureAsString();
    features.addAll(dto.getFeatures());
  }

  FeatureSetViewDto asView() {
    return new FeatureSetViewDto(id, getFeatures());
  }
}
