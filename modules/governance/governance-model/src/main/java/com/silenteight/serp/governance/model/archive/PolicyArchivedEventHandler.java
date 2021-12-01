package com.silenteight.serp.governance.model.archive;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.ModelsArchived;
import com.silenteight.serp.governance.model.archive.amqp.ModelsArchivedMessageGateway;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.get.ModelDetailsQuery;
import com.silenteight.serp.governance.policy.domain.events.PolicyArchivedEvent;

import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.common.PolicyResource.toResourceName;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class PolicyArchivedEventHandler {

  @NonNull
  private final ModelDetailsQuery modelDetailsQuery;
  @NonNull
  private final ModelsArchivedMessageGateway messageGateway;

  @EventListener
  public void handle(PolicyArchivedEvent event) {
    UUID policyId = event.getPolicyId();
    log.info("Sending models archived message: policyId={}", policyId);
    List<String> modelNames = toModelNamesByPolicyId(policyId);

    if (!modelNames.isEmpty()) {
      messageGateway.send(toModelsArchivedMessage(modelNames));
      log.info("Models archived message has been sent: modelNames={}", modelNames);
    }
  }

  private List<String> toModelNamesByPolicyId(@NonNull UUID policyId) {
    return modelDetailsQuery
        .getByPolicy(toResourceName(policyId))
        .stream()
        .map(ModelDto::getName)
        .collect(toList());
  }

  private static ModelsArchived toModelsArchivedMessage(List<String> modelNames) {
    return ModelsArchived.newBuilder()
        .addAllModels(modelNames)
        .build();
  }
}
