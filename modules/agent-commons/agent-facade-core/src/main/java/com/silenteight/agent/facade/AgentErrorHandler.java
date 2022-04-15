package com.silenteight.agent.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.agent.facade.exchange.StructReasonMapper;
import com.silenteight.agent.monitoring.Monitoring;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;
import com.silenteight.agents.v1.api.exchange.AgentOutput.FeatureSolution;

import com.google.protobuf.Struct;

import static java.lang.String.format;
import static java.lang.String.join;

@Slf4j
@RequiredArgsConstructor
public class AgentErrorHandler {

  public static final String ERROR_MESSAGE_FIELD = "errorMessage";

  private static final String DATA_SOURCE_ERROR = "DATA_SOURCE_ERROR";
  private static final String DEADLINE_EXCEEDED_ERROR = "DEADLINE_EXCEEDED";
  private static final String NO_DATA = "NO_DATA";

  private final Monitoring monitoring;

  public AgentExchangeResponse createErrorResponse(
      AgentExchangeRequest request, Exception exception) {

    logError(request, exception);

    var builder = AgentExchangeResponse.newBuilder();
    for (String matchId : request.getMatchesList()) {
      var agentOutputBuilder = AgentOutput.newBuilder().setMatch(matchId);
      for (String feature : request.getFeaturesList()) {
        var solution = getSolution(exception);
        FeatureSolution featureSolution = fromExceptionMessage(solution, exception);
        agentOutputBuilder.addFeatures(Feature.newBuilder()
            .setFeature(feature)
            .setFeatureSolution(featureSolution)
            .build());
      }

      builder.addAgentOutputs(agentOutputBuilder.build());
    }

    return builder.build();
  }

  private void logError(AgentExchangeRequest request, Exception exception) {
    var matches = join(",", request.getMatchesList());
    var features = join(",", request.getFeaturesList());
    log.error(format("Failed to process the AE request: matches=%s, features=%s, errorMessage=%s",
        matches, features, exception.getMessage()), exception);
    monitoring.captureException(exception);
  }

  private static String getSolution(Exception exception) {
    if (exception instanceof AgentRequestTimeoutException) {
      return DEADLINE_EXCEEDED_ERROR;
    }
    return DATA_SOURCE_ERROR;
  }

  private static Struct buildErrorMessage(String errorMessage) {
    return StructReasonMapper.mapToStruct(
        ERROR_MESSAGE_FIELD, errorMessage == null ? "" : errorMessage);
  }

  public FeatureSolution buildNoDataSolution() {
    return FeatureSolution.newBuilder()
        .setSolution(NO_DATA)
        .build();
  }

  private static FeatureSolution fromExceptionMessage(
      String solution, Exception exception) {
    var reason = buildErrorMessage(exception.getMessage());

    return FeatureSolution.newBuilder().setSolution(solution).setReason(reason).build();
  }

  public FeatureSolution buildErrorFeatureSolution(String solution, Exception exception) {
    monitoring.captureException(exception);
    return fromExceptionMessage(solution, exception);
  }
}
