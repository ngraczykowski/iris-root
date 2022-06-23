package com.silenteight.serp.governance.vector.domain;

import lombok.*;

import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.common.signature.Signature;
import com.silenteight.serp.governance.common.signature.SignatureConverter;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "governance_analytics_feature_vector")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class FeatureVector extends BaseEntity implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(name = "feature_vector_id", updatable = false)
  private Long id;

  @Column(updatable = false, unique = true)
  @Convert(converter = SignatureConverter.class)
  private Signature vectorSignature;

  @Column(updatable = false)
  @Convert(converter = CommaSeparatedListConverter.class)
  private List<String> names;

  @Column(updatable = false)
  @Convert(converter = CommaSeparatedListConverter.class)
  private List<String> values;

  FeatureVector(
      @NonNull Signature vectorSignature,
      @NonNull List<String> names,
      @NonNull List<String> values) {

    this.vectorSignature = vectorSignature;
    this.names = names;
    this.values = values;
  }

  String getSignatureAsString() {
    return vectorSignature.asString();
  }
}
