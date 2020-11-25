package com.silenteight.serp.governance.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.model.Model;

import com.google.protobuf.ByteString;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@RequiredArgsConstructor
public class ModelFinder {

  private final ModelRepository modelRepository;

  public Model getBySignature(ByteString modelSignature) {
    ModelEntity model = modelRepository.getBySignature(toBase64String(modelSignature));

    return model.getModel();
  }
}
