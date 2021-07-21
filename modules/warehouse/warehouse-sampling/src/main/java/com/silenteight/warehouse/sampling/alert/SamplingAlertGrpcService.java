package com.silenteight.warehouse.sampling.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.model.api.v1.SamplingAlertsServiceGrpc.SamplingAlertsServiceImplBase;

import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;

import static com.google.rpc.Code.INTERNAL_VALUE;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@RequiredArgsConstructor
@Slf4j
class SamplingAlertGrpcService extends SamplingAlertsServiceImplBase {

  @NonNull
  private final SamplingAlertService samplingAlertService;

  @Override
  public void getAlertsSample(
      AlertsSampleRequest request, StreamObserver<AlertsSampleResponse> responseObserver) {

    log.info("AlertsSampleRequest received, request={}",request);
    try {
      AlertsSampleResponse alertsSampleResponse =
          samplingAlertService.generateSamplingAlerts(request);

      responseObserver.onNext(alertsSampleResponse);
      responseObserver.onCompleted();
      log.debug("AlertsSampleRequest processed, request={}",request);
    } catch (RuntimeException e) {
      handleException(
          responseObserver, e, INTERNAL_VALUE, "Getting alerts sample response failed.");
    }
  }

  private <T> void handleException(
      StreamObserver<T> responseObserver, RuntimeException e, Integer code, String message) {

    Status status = Status.newBuilder()
        .setCode(code)
        .setMessage(message)
        .build();

    log.error(message, e);
    responseObserver.onError(toStatusRuntimeException(status));
  }
}
