package com.silenteight.serp.governance.model;

import com.silenteight.proto.serp.v1.model.Model;

import java.util.Optional;

interface ModelDifference {

  Optional<String> getNameDifference();

  Optional<Model> getModelDifference();
}
