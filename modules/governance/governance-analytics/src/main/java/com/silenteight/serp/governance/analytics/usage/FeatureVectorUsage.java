package com.silenteight.serp.governance.analytics.usage;

import lombok.*;

import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.common.signature.Signature;
import com.silenteight.serp.governance.common.signature.SignatureConverter;

import javax.persistence.*;

@Entity
@Table(name = "governance_analytics_feature_vector_usage")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
class FeatureVectorUsage implements IdentifiableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  @Column(name = "usage_id", updatable = false)
  private Long id;

  @Version
  @Getter(AccessLevel.NONE)
  @Column(nullable = false)
  @Access(AccessType.FIELD)
  private Long version;

  @Column(updatable = false, unique = true)
  @Convert(converter = SignatureConverter.class)
  private Signature vectorSignature;

  @Column
  private Long usageCount;

  FeatureVectorUsage(Signature vectorSignature, Long usageCount) {
    this.vectorSignature = vectorSignature;
    this.usageCount = usageCount;
  }

  void markAsUsed() {
    usageCount++;
  }
}
