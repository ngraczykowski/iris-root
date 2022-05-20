package com.silenteight.adjudication.engine.analysis.categoryrequest.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.CategoryRequestFacade;
import com.silenteight.adjudication.internal.v1.PendingRecommendations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.analysis.categoryrequest.integration.CategoryRequestChannels.CATEGORY_REQUEST_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.analysis.categoryrequest.integration.CategoryRequestChannels.MATCH_CATEGORIES_UPDATED_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
@Slf4j
@ConditionalOnProperty(
    value = "ae.solving.enabled",
    havingValue = "false",
    matchIfMissing = true
)
class CategoryRequestIntegrationFlow extends IntegrationFlowAdapter {

  private final CategoryRequestFacade categoryRequestFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(
        CATEGORY_REQUEST_PENDING_RECOMMENDATIONS_INBOUND_CHANNEL)
        .handle(PendingRecommendations.class, (payload, headers) ->
            categoryRequestFacade.handlePendingRecommendations(payload))
        .split()
        .channel(MATCH_CATEGORIES_UPDATED_OUTBOUND_CHANNEL);
  }
}
