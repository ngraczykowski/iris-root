/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain.agent.input;

import com.silenteight.iris.bridge.scb.feeding.domain.agent.input.feature.FeatureFactory;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.iris.bridge.scb.feeding.domain.category.CategoryValue;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.category.v2.CreateCategoryValuesIn;

import java.util.List;

public class AgentInputFactory {

  private final List<FeatureFactory> featureInputFactories;
  private final List<CategoryValue> categoryValues;

  public AgentInputFactory(List<FeatureFactory> featureInputFactories,
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

  public List<AgentInputIn<Feature>> createAgentInputIns(Alert alert, Match match) {
    return featureInputFactories.stream()
        .map(factory -> AgentInputIn.builder()
                    .alert(alert.getName())
                    .match(match.getName())
                    .featureInputs(List.of(factory.create(alert, match)))
            .build())
        .toList();
  }

  public List<CreateCategoryValuesIn> createCategoryValuesIns(Alert alert, Match match) {
    return categoryValues.stream()
        .map(categoryValue -> categoryValue.createCategoryValuesIn(alert, match))
        .toList();
  }
}
