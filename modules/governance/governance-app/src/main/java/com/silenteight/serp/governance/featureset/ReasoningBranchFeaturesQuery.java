package com.silenteight.serp.governance.featureset;

import lombok.*;
import lombok.Builder.Default;

import com.silenteight.serp.governance.featureset.dto.FeatureSetViewDto;

import org.hibernate.annotations.Immutable;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Immutable
class ReasoningBranchFeaturesQuery {

  @NonNull
  @Id
  private Long featureVectorId;

  @NonNull
  private Long featureSetId;

  @Getter
  @Column(updatable = false)
  @Convert(converter = FeatureListConverter.class)
  @Default
  private List<String> features = new ArrayList<>();

  FeatureSetViewDto asView() {
    return new FeatureSetViewDto(featureSetId, getFeatures());
  }
}
