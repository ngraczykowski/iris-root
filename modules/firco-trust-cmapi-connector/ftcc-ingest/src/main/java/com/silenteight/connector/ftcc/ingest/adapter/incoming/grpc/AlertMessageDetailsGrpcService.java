package com.silenteight.connector.ftcc.ingest.adapter.incoming.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.ingest.common.BatchResource;
import com.silenteight.connector.ftcc.ingest.common.MessageResource;
import com.silenteight.connector.ftcc.ingest.domain.MessageDetailsQuery;
import com.silenteight.connector.ftcc.ingest.domain.dto.MessageDetailsDto;
import com.silenteight.proto.fab.api.v1.*;

import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.Collection;
import java.util.List;

import static com.google.rpc.Code.INTERNAL_VALUE;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class AlertMessageDetailsGrpcService
    extends AlertMessageDetailsServiceGrpc.AlertMessageDetailsServiceImplBase {

  private static final String ALERT_DETAILS_ERROR =
      "Unhandled error occurred in Firco Trust CMAPI Connector while calling 'alertsDetails'.";

  @NonNull
  private final MessageDetailsQuery messageDetailsQuery;

  @Override
  public void alertsDetails(
      AlertMessagesDetailsRequest request,
      StreamObserver<AlertMessagesDetailsResponse> responseObserver) {

    try {
      Collection<MessageDetailsDto> messages = listMessages(request.getAlertsList());
      AlertMessagesDetailsResponse response = AlertMessagesDetailsResponse.newBuilder()
          .addAllAlerts(toAlertMessageDetails(messages))
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      handleException(responseObserver, e, INTERNAL_VALUE, ALERT_DETAILS_ERROR);
    }
  }

  private Collection<MessageDetailsDto> listMessages(List<AlertMessageHeader> headers) {
    return headers
        .stream()
        .map(this::messageDetails)
        .collect(toList());
  }

  private MessageDetailsDto messageDetails(AlertMessageHeader header) {
    log.info(
        "Getting message details for batchName={} and messageName={}",
        header.getBatchName(),
        header.getMessageName());

    return messageDetailsQuery.details(
        BatchResource.fromResourceName(header.getBatchName()),
        MessageResource.fromResourceName(header.getMessageName()));
  }

  private static List<AlertMessageDetails> toAlertMessageDetails(
      Collection<MessageDetailsDto> messages) {

    return messages
        .stream()
        .map(AlertMessageDetailsGrpcService::toAlertMessageDetails)
        .collect(toList());
  }

  private static AlertMessageDetails toAlertMessageDetails(MessageDetailsDto message) {
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
