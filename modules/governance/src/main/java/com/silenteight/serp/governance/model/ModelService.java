package com.silenteight.serp.governance.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.model.Model;

import java.util.Optional;
import javax.transaction.Transactional;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@RequiredArgsConstructor
public class ModelService {

  private final ModelRepository modelRepository;
  private final ModelDifferenceCalculator modelDifferenceCalculator;
  private final ModelActivator modelActivator;

  @Transactional
  public void storeOrUpdate(Model receivedModel) {
    String modelSignature = toBase64String(receivedModel.getModelSignature());
    Optional<ModelEntity> existingModelOpt = modelRepository.findBySignature(modelSignature);

    existingModelOpt.ifPresentOrElse(
        existingModel -> updateModel(existingModel, receivedModel),
        () -> storeNewModel(receivedModel)
    );
  }

  private void storeNewModel(Model receivedModel) {
    ModelEntity newModel = modelRepository.save(new ModelEntity(receivedModel));
    modelActivator.activateModel(newModel);
  }

  private void updateModel(ModelEntity existingModel, Model receivedModel) {
    ModelDifference diff = modelDifferenceCalculator.calculate(existingModel, receivedModel);

    existingModel.apply(diff);
  }
}
