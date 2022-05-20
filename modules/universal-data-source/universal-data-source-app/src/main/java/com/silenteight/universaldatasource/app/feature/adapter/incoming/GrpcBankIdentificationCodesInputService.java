package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesInputServiceGrpc.BankIdentificationCodesInputServiceImplBase;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsRequest;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class GrpcBankIdentificationCodesInputService extends BankIdentificationCodesInputServiceImplBase {

  private final FeatureAdapter featureAdapter;

  @Override
  public void batchGetMatchBankIdentificationCodesInputs(
      BatchGetMatchBankIdentificationCodesInputsRequest request,
      StreamObserver<BatchGetMatchBankIdentificationCodesInputsResponse> responseObserver) {
    featureAdapter.batchGetMatchBankIdentificationCodesInputs(request, responseObserver::onNext);
    responseObserver.onCompleted();
  }
}
