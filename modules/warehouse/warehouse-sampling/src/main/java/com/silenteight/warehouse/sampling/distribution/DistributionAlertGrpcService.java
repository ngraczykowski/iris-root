package com.silenteight.warehouse.sampling.distribution;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionRequest;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionResponse;
import com.silenteight.model.api.v1.DistributionAlertsServiceGrpc.DistributionAlertsServiceImplBase;

import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;

import static com.google.rpc.Code.INTERNAL_VALUE;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@RequiredArgsConstructor
@Slf4j
public class DistributionAlertGrpcService extends DistributionAlertsServiceImplBase {

  @NonNull
  DistributionAlertsService distributionAlertsService;

  @Override
  public void getAlertsDistribution(
      AlertsDistributionRequest request,
      StreamObserver<AlertsDistributionResponse> responseObserver) {

    log.info("AlertsDistributionRequest received, request={}", request);
    try {
      AlertsDistributionResponse response =
          distributionAlertsService.getAlertsDistribution(request);

      responseObserver.onNext(response);
      responseObserver.onCompleted();
      log.debug("Alerts distribution request processed, response={}", response);
    } catch (RuntimeException e) {
      handleException(
          responseObserver, e, INTERNAL_VALUE, "Getting alerts distribution response failed.");
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
