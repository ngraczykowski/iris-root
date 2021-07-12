package com.silenteight.searpaymentsmockup.datasource;

import com.silenteight.datasource.api.name.v1.*;
import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceImplBase;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
class NameAgentGrpcService extends NameInputServiceImplBase {

  @Override
  public void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request,
      StreamObserver<BatchGetMatchNameInputsResponse> responseObserver) {
    var features = request.getFeaturesList();
    var matches = request.getMatchesList();

    var responseBuilder = BatchGetMatchNameInputsResponse.newBuilder();

    for (int i = 0; i < matches.size(); i++) {
      responseBuilder.addNameInputs(toNameInput(features.get(0), matches.get(0)));
    }

    responseObserver.onNext(responseBuilder.build());
  }

  private static NameInput toNameInput(String feature, String match) {
    return NameInput
        .newBuilder()
        .setMatch(match)
        .addAllNameFeatureInputs(List.of(toNameFeatureInput(feature)))
        .build();
  }

  private static NameFeatureInput toNameFeatureInput(String feature) {
    var alertedPartyNames = List.of(
        AlertedPartyName.newBuilder().setName("SAUSAGE COMPANY").build(),
        AlertedPartyName.newBuilder().setName("XYZ COMPANY CENTRE").build());
    var watchListNames = List.of(
        WatchlistName.newBuilder().setName("JAY PARK ABC").setType(NameType.REGULAR).build(),
        WatchlistName.newBuilder().setName("JABBAR, ABDOL").setType(NameType.ALIAS).build());
    return NameFeatureInput
        .newBuilder()
        .setFeature(feature)
        .addAllAlertedPartyNames(alertedPartyNames)
        .addAllWatchlistNames(watchListNames)
        .setAlertedPartyType(EntityType.ORGANIZATION)
        .addAllMatchingTexts(List.of("PARK"))
        .build();
  }
}