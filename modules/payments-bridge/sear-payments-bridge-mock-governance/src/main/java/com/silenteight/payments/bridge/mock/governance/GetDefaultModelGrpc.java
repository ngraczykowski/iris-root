package com.silenteight.payments.bridge.mock.governance;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceImplBase;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile("mockgovernance")
@GrpcService
@RequiredArgsConstructor
class GetDefaultModelGrpc extends SolvingModelServiceImplBase {

  @Override
  public void getDefaultSolvingModel(Empty request, StreamObserver<SolvingModel> responseObserver) {
    responseObserver.onNext(
        SolvingModel
            .newBuilder()
            .setName("testModel")
            .setPolicyName("testPolicy")
            .setStrategyName("testStrategy")
            .addAllFeatures(List.of(
                Feature.newBuilder().setName("features/geo").build(),
                Feature.newBuilder().setName("features/name").build()))
            .addAllCategories(
                List.of("categories/crossmatch", "categories/oneLiner", "categories/specificTerms",
                    "categories/twoLines"))
            .build());
    responseObserver.onCompleted();
  }
}
