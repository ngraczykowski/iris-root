package com.silenteight.scb.feeding.domain;

import com.silenteight.scb.feeding.domain.agentinput.AgentInput;
import com.silenteight.scb.feeding.domain.category.CategoryValue;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.category.v2.CreateCategoryValuesIn;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedingService {

  private final List<AgentInput> agentInputs;
  private final List<CategoryValue> categoryValues;

  FeedingService(List<AgentInput> agentInputs, List<CategoryValue> categoryValues) {
    if (agentInputs.isEmpty()) {
      throw new IllegalStateException("There are no agent inputs.");
    }
    if (categoryValues.isEmpty()) {
      throw new IllegalStateException("There are no category values.");
    }
    this.agentInputs = agentInputs;
    this.categoryValues = categoryValues;
  }

  List<AgentInputIn<Feature>> createAgentInputIns(Alert alert, Match match) {
    return agentInputs.stream()
        .map(agentInput -> agentInput.createAgentInput(alert, match))
        .toList();
  }

  List<CreateCategoryValuesIn> createCategoryValuesIns(Alert alert, Match match) {
    return categoryValues.stream()
        .map(categoryValue -> categoryValue.createCategoryValuesIn(alert, match))
        .toList();
  }
}
