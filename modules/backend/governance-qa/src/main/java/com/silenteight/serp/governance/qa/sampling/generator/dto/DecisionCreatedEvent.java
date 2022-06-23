package com.silenteight.serp.governance.qa.sampling.generator.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor(staticName = "of")
@Data
public class DecisionCreatedEvent {

  @NotNull
  List<CreateDecisionRequest> createDecisionRequests;
}
