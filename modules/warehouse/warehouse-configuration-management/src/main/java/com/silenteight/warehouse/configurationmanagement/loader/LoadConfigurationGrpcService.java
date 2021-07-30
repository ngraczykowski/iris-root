package com.silenteight.warehouse.configurationmanagement.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.internal.v1.ConfigurationManagementServiceGrpc.ConfigurationManagementServiceImplBase;
import com.silenteight.warehouse.internal.v1.ImportConfigurationRequest;

import com.google.protobuf.Empty;
import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;

import static com.google.rpc.Code.INTERNAL_VALUE;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@Slf4j
@RequiredArgsConstructor
public class LoadConfigurationGrpcService extends ConfigurationManagementServiceImplBase {

  private final LoadOpendistroConfigurationUseCase loadOpendistroConfigurationUseCase;

  @Override
  public void importConfiguration(
      ImportConfigurationRequest request, StreamObserver<Empty> responseObserver) {

    log.info("ImportConfigurationRequest received, request={}", request);
    try {
      loadOpendistroConfigurationUseCase.load(request);

      responseObserver.onNext(Empty.newBuilder().build());
      responseObserver.onCompleted();
      log.debug("ImportConfigurationRequest processed");
    } catch (RuntimeException e) {
      handleException(
          responseObserver, e, INTERNAL_VALUE, "Import configuration failed.");
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

