package com.silenteight.serp.governance.featurevector;

import lombok.*;

import com.silenteight.proto.serp.v1.alert.VectorValue;
import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;
import com.silenteight.serp.governance.featurevector.dto.FeatureVectorView;
import com.silenteight.serp.governance.support.VectorValueListConverter;

import com.google.protobuf.ByteString;

import java.util.List;
import javax.persistence.*;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.fromBase64String;
import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;
import static java.util.stream.Collectors.toList;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "governance_feature_vector")
class FeatureVectorEntity extends BaseEntity implements IdentifiableEntity {

  @Getter
  @Setter
  @EqualsAndHashCode.Include
  @Id
  @Column(name = "featureVectorId", updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Column(updatable = false)
  private String featuresSignature;

  @Getter
  @Column(updatable = false, unique = true)
  private String vectorSignature;

  @Getter
  @Column(updatable = false)
  @Convert(converter = VectorValueListConverter.class)
  private List<VectorValue> values;

  FeatureVectorEntity(
      @NonNull ByteString featuresSignature,
      @NonNull ByteString vectorSignature,
      @NonNull List<VectorValue> values) {
    this.featuresSignature = toBase64String(featuresSignature);
    this.vectorSignature = toBase64String(vectorSignature);
    this.values = values;
  }

  FeatureVectorView asView() {
    return FeatureVectorView
        .builder()
        .id(getId())
        .featuresSignature(fromBase64String(getFeaturesSignature()))
        .vectorSignature(fromBase64String(getVectorSignature()))
        .values(getValues().stream().map(VectorValue::getTextValue).collect(toList()))
        .build();
  }
}
