package com.silenteight.adjudication.engine.mock.governance;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.protobuf.Uuid;
import com.silenteight.solving.api.v1.*;
import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse.Builder;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@GrpcService
@Profile("mock-governance")
@RequiredArgsConstructor
class MockPolicyStepsSolvingGrpc extends PolicyStepsSolvingGrpc.PolicyStepsSolvingImplBase {

  private final MockFeatureVectorSolutionReduction mockFeatureVectorSolutionReduction;

  @Override
  public void batchSolveFeatures(
      BatchSolveFeaturesRequest request,
      StreamObserver<BatchSolveFeaturesResponse> responseObserver) {
    Builder builder = BatchSolveFeaturesResponse.newBuilder();

    for (FeatureVector feature : request.getFeatureVectorsList()) {
      FeatureVectorSolution solution =
          mockFeatureVectorSolutionReduction.solveFeatureVector(feature.getFeatureValueList());
      builder.addSolutions(SolutionResponse.newBuilder()
          .setFeatureVectorSolution(solution)
          .setStepId(Uuid.newBuilder()
              .setValue(ByteString.copyFromUtf8("1"))
              .build())
          .setFeatureVectorSignature(ByteString.copyFromUtf8("UjI5MlpYSnVZVzVqWlNCTmIyTnJJREU9"))
          .build());
    }

    responseObserver.onNext(builder.build());
    responseObserver.onCompleted();
  }
}
