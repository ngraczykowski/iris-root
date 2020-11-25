package com.silenteight.serp.governance.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.model.Feature;
import com.silenteight.proto.serp.v1.model.FeatureCollection;
import com.silenteight.proto.serp.v1.model.Model;
import com.silenteight.serp.governance.featureset.FeatureSetService;
import com.silenteight.serp.governance.featureset.dto.FeatureSetToStoreDto;
import com.silenteight.serp.governance.featureset.dto.StoreFeatureSetRequest;
import com.silenteight.serp.governance.model.ModelService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparingInt;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class ModelReceiver {

  private final ModelService modelService;
  private final FeatureSetService featureSetService;

  @RabbitListener(queues = "#{governancePipelineModelQueue}")
  public void process(Model pipelineModel) {
    log.info("Received model");
    modelService.storeOrUpdate(pipelineModel);

    storeFeatureSets(pipelineModel);
  }

  private void storeFeatureSets(Model pipelineModel) {
    Set<FeatureSetToStoreDto> featureSets = new HashSet<>();

    createFeatureSet(pipelineModel.getAlertFeatures()).ifPresent(featureSets::add);
    createFeatureSet(pipelineModel.getMatchFeatures()).ifPresent(featureSets::add);
    createFeatureSet(pipelineModel.getDecisionFeatures()).ifPresent(featureSets::add);

    featureSetService.storeFeatureSet(new StoreFeatureSetRequest(featureSets));
  }

  private static Optional<FeatureSetToStoreDto> createFeatureSet(FeatureCollection features) {
    if (features.getFeaturesCount() <= 0)
      return empty();

    List<String> sortedFeatureNames = features
        .getFeaturesList()
        .stream()
        .sorted(comparingInt(Feature::getIndex))
        .map(Feature::getName)
        .collect(toList());

    return of(new FeatureSetToStoreDto(features.getFeaturesSignature(), sortedFeatureNames));
  }
}
