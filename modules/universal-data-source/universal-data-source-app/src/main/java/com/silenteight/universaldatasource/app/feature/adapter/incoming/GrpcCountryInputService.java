package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsRequest;
import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsResponse;
import com.silenteight.datasource.api.country.v1.CountryInputServiceGrpc.CountryInputServiceImplBase;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcCountryInputService extends CountryInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchCountryInputs(
      BatchGetMatchCountryInputsRequest request,
      StreamObserver<BatchGetMatchCountryInputsResponse> responseObserver) {

    featureAdapter.batchGetMatchCountryInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
