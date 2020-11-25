package com.silenteight.serp.governance.model;

import lombok.*;

import com.silenteight.proto.serp.v1.model.Model;
import com.silenteight.sep.base.common.entity.BaseAggregateRoot;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import javax.persistence.*;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ModelEntity extends BaseAggregateRoot implements IdentifiableEntity {

  @Getter
  @Setter
  @Id
  @Column(name = "modelId", updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  @Column(updatable = false)
  private String signature;

  @Embedded
  private FeaturesSignatures featuresSignatures;

  @Getter
  boolean isActive = false;

  @Getter
  private String name;

  @Getter
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private Model model;

  ModelEntity(Model model) {
    this.name = model.getModelName().getFriendlyName();
    this.signature = toBase64String(model.getModelSignature());
    this.featuresSignatures = new FeaturesSignatures(model);
    this.model = model;
  }

  void deactivate() {
    this.isActive = false;
  }

  void activate() {
    this.isActive = true;
  }

  void apply(ModelDifference diff) {
    diff.getModelDifference().ifPresent(this::setModel);
    diff.getNameDifference().ifPresent(this::setName);
  }
}
