package com.silenteight.warehouse.sampling.distribution;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionRequest;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertsDistributionResponse;
import com.silenteight.model.api.v1.DistributionAlertsServiceGrpc.DistributionAlertsServiceImplBase;

import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;

import static com.google.rpc.Code.INTERNAL_VALUE;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@Slf4j
public class DistributionAlertGrpcService extends DistributionAlertsServiceImplBase {

  @Override
  public void getAlertsDistribution(AlertsDistributionRequest request,
                                    StreamObserver<AlertsDistributionResponse> responseObserver) {
    try {
      AlertsDistributionResponse response = AlertsDistributionResponse.getDefaultInstance();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
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
