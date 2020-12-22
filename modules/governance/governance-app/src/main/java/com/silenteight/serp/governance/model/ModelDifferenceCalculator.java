package com.silenteight.serp.governance.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.serp.v1.model.Model;
import com.silenteight.serp.governance.model.ModelDifferenceCalculator.CalculatedModelDifference.CalculatedModelDifferenceBuilder;

import java.util.Optional;

class ModelDifferenceCalculator {

  ModelDifference calculate(ModelEntity existingModel, Model newModel) {

    CalculatedModelDifferenceBuilder builder = CalculatedModelDifference.builder();

    if (modelDifferenceExists(existingModel, newModel))
      builder.model(newModel);

    if (nameDifferenceExists(existingModel, newModel))
      builder.name(newModel.getModelName().getFriendlyName());

    return builder.build();
  }

  private static boolean modelDifferenceExists(ModelEntity existingModel, Model newModel) {
    return !newModel.equals(existingModel.getModel());
  }

  private static boolean nameDifferenceExists(ModelEntity existingModel, Model newModel) {
    String newModelName = newModel.getModelName().getFriendlyName();
    return !existingModel.getName().equals(newModelName);
  }

  @Value
  @Builder
  static class CalculatedModelDifference implements ModelDifference {

    String name;
    Model model;

    @Override
    public Optional<String> getNameDifference() {
      return Optional.ofNullable(name);
    }

    @Override
    public Optional<Model> getModelDifference() {
      return Optional.ofNullable(model);
    }
  }

}
