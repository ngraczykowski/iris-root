package com.silenteight.serp.governance.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.model.Model;
import com.silenteight.sep.base.common.support.hibernate.ByteStringConverter;

import com.google.protobuf.ByteString;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
class FeaturesSignatures {

  @Convert(converter = ByteStringConverter.class)
  private ByteString alertFeaturesSignature;
  @Convert(converter = ByteStringConverter.class)
  private ByteString matchFeaturesSignature;
  @Convert(converter = ByteStringConverter.class)
  private ByteString decisionFeaturesSignature;

  FeaturesSignatures(Model model) {
    this.alertFeaturesSignature = model.getAlertFeatures().getFeaturesSignature();
    this.matchFeaturesSignature = model.getMatchFeatures().getFeaturesSignature();
    this.decisionFeaturesSignature = model.getDecisionFeatures().getFeaturesSignature();
  }
}
