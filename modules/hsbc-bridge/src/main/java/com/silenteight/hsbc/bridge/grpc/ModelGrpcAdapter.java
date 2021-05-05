package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.FeatureDto;
import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.SolvingModelDto;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.stream.Collectors;

import static com.google.protobuf.Empty.getDefaultInstance;
import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
class ModelGrpcAdapter implements ModelServiceClient {

  private final SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public SolvingModelDto getSolvingModel() {
    var solvingModel = getStub().getDefaultSolvingModel(getDefaultInstance());
    return mapSolvingModel(solvingModel);
  }

  private SolvingModelServiceBlockingStub getStub() {
    return solvingModelServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }

  private SolvingModelDto mapSolvingModel(SolvingModel solvingModel) {
    return SolvingModelDto.builder()
        .name(solvingModel.getName())
        .policyName(solvingModel.getPolicyName())
        .strategyName(solvingModel.getStrategyName())
        .features(solvingModel.getFeaturesList().stream()
            .map(feature -> FeatureDto.builder()
                .name(feature.getName())
                .agentConfig(feature.getAgentConfig())
                .build())
            .collect(Collectors.toList()))
        .categories(solvingModel.getCategoriesList())
        .build();
  }
}
