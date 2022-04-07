package com.silenteight.scb.feeding.domain;

import com.silenteight.scb.feeding.domain.category.CategoryValue;
import com.silenteight.scb.feeding.domain.featureinput.FeatureInputFactory;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.category.v2.CreateCategoryValuesIn;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedingService {

  private final List<FeatureInputFactory> featureInputFactories;
  private final List<CategoryValue> categoryValues;

  FeedingService(List<FeatureInputFactory> featureInputFactories,
      List<CategoryValue> categoryValues) {
    
    if (featureInputFactories.isEmpty()) {
      throw new IllegalStateException("There are no agent inputs.");
    }
    if (categoryValues.isEmpty()) {
      throw new IllegalStateException("There are no category values.");
    }
    this.featureInputFactories = featureInputFactories;
    this.categoryValues = categoryValues;
  }

  List<AgentInputIn<Feature>> createAgentInputIns(Alert alert, Match match) {
    return featureInputFactories.stream()
        .map(factory -> create(alert.getName(), match.getName(), factory.create(alert, match)))
        .toList();
  }

  List<CreateCategoryValuesIn> createCategoryValuesIns(Alert alert, Match match) {
    return categoryValues.stream()
        .map(categoryValue -> categoryValue.createCategoryValuesIn(alert, match))
        .toList();
  }

  private AgentInputIn<Feature> create(String alertName, String matchName, Feature feature) {
    return AgentInputIn.builder()
        .alert(alertName)
        .match(matchName)
        .featureInputs(List.of(feature)).build();
  }
}
