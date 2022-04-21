package com.silenteight.connector.ftcc.ingest.adapter.incoming.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.common.resource.MessageResource;
import com.silenteight.connector.ftcc.request.get.MessageByIdsQuery;
import com.silenteight.connector.ftcc.request.get.dto.MessageDto;
import com.silenteight.proto.fab.api.v1.*;

import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.MDC;

import java.util.Collection;
import java.util.List;

import static com.google.rpc.Code.INTERNAL_VALUE;
import static com.silenteight.connector.ftcc.common.MdcParams.BATCH_NAME;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class AlertMessageDetailsGrpcService
    extends AlertMessageDetailsServiceGrpc.AlertMessageDetailsServiceImplBase {

  private static final String ALERT_DETAILS_ERROR =
      "Unhandled error occurred in Firco Trust CMAPI Connector while calling 'alertsDetails'.";

  @NonNull
  private final MessageByIdsQuery messageByIdsQuery;

  @Override
  public void alertsDetails(
      AlertMessagesDetailsRequest request,
      StreamObserver<AlertMessagesDetailsResponse> responseObserver) {

    try {
      Collection<MessageDto> messages = listMessages(request.getAlertsList());
      AlertMessagesDetailsResponse response = AlertMessagesDetailsResponse.newBuilder()
          .addAllAlerts(toAlertMessageDetails(messages))
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      handleException(responseObserver, e, INTERNAL_VALUE, ALERT_DETAILS_ERROR);
    }
  }

  private Collection<MessageDto> listMessages(List<AlertMessageHeader> headers) {
    return headers
        .stream()
        .map(this::messageDetails)
        .collect(toList());
  }

  private MessageDto messageDetails(AlertMessageHeader header) {
    MDC.put(BATCH_NAME, header.getBatchName());
    try {
      log.info(
          "Getting message details for batchName={} and messageName={}",
          header.getBatchName(),
          header.getMessageName());

      return messageByIdsQuery.get(
          BatchResource.fromResourceName(header.getBatchName()),
          MessageResource.fromResourceName(header.getMessageName()));
    } finally {
      MDC.remove(BATCH_NAME);
    }
  }

  private static List<AlertMessageDetails> toAlertMessageDetails(
      Collection<MessageDto> messages) {

    return messages
        .stream()
        .map(AlertMessageDetailsGrpcService::toAlertMessageDetails)
        .collect(toList());
  }

  private static AlertMessageDetails toAlertMessageDetails(MessageDto message) {
    return AlertMessageDetails.newBuilder()
        .setMessageName(message.getMessageName())
        .setPayload(message.getPayload())
        .build();
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
