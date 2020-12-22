package com.silenteight.serp.governance.model;

import com.silenteight.proto.serp.v1.model.Model;
import com.silenteight.proto.serp.v1.model.ModelName;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.sep.base.testing.utils.ByteStringTestUtils.randomSignature;
import static org.assertj.core.api.Assertions.*;

class ModelModuleAcceptanceTest {

  private ModelService serviceUnderTest;

  private ModelFinder finderUnderTest;

  @BeforeEach
  void setUp() {
    var conf = new ModelConfiguration();

    var repository = conf.inMemoryRepository();

    var modelActivator = conf.modelActivator(repository);
    serviceUnderTest = conf.modelService(
        repository, conf.modelDifferenceCalculator(), modelActivator);
    finderUnderTest = conf.modelFinder(repository);
  }

  @Test
  void afterModelIsReceived_canFindIt() {
    var modelSignature = randomSignature();
    var expectedModel = model(modelSignature, modelName("someName"));

    serviceUnderTest.storeOrUpdate(expectedModel);

    var actualModel = finderUnderTest.getBySignature(modelSignature);
    assertThat(actualModel).isEqualTo(expectedModel);
  }

  private static ModelName modelName(String name) {
    return ModelName.newBuilder().setFriendlyName(name).build();
  }

  private static Model model(ByteString modelSignature, ModelName modelName) {
    return Model.newBuilder()
                .setModelSignature(modelSignature)
                .setModelName(modelName).build();
  }

  @Test
  void receivedTwoModelsWithEqualSignatureButDifferentNames_modelIsUpdated() {
    var signature = randomSignature();
    var firstModel = model(signature, modelName("firstModel"));

    serviceUnderTest.storeOrUpdate(firstModel);

    var expectedName = "otherName";
    var secondModel = model(signature, modelName(expectedName));
    serviceUnderTest.storeOrUpdate(secondModel);

    var actualModel = finderUnderTest.getBySignature(signature);

    assertThat(actualModel.getModelName())
        .extracting(ModelName::getFriendlyName)
        .isEqualTo(expectedName);
  }
}
