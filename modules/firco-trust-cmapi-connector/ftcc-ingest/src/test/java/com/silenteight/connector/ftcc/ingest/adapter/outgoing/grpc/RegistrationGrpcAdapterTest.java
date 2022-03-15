package com.silenteight.connector.ftcc.ingest.adapter.outgoing.grpc;

import com.silenteight.connector.ftcc.common.dto.input.RequestBodyDto;
import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.common.dto.input.RequestSendMessageDto;
import com.silenteight.proto.registration.api.v1.RegisterBatchRequest;
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub;
import com.silenteight.registration.api.library.v1.RegistrationServiceGrpcAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.silenteight.connector.ftcc.ingest.adapter.outgoing.grpc.RegistrationFixtures.BATCH;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationGrpcAdapterTest {

  private static final long DEADLINE_IN_SECONDS = 30L;

  @Mock
  private RegistrationServiceBlockingStub registrationStub;

  private RegistrationGrpcAdapter underTest;

  @BeforeEach
  void setUp() {
    underTest = new RegistrationGrpcAdapter(
        new RegistrationServiceGrpcAdapter(registrationStub, DEADLINE_IN_SECONDS));
  }

  @Test
  void registerBatch() {
    // given
    when(registrationStub.withDeadlineAfter(DEADLINE_IN_SECONDS, TimeUnit.SECONDS))
        .thenReturn(registrationStub);
    RequestDto request = makeRequest();

    // when
    underTest.registerBatch(BATCH);

    // then
    verify(registrationStub).registerBatch(RegisterBatchRequest
        .newBuilder()
        .setBatchId(BATCH.getBatchId())
        .setAlertCount(BATCH.getAlertsCount())
        .setBatchMetadata("")
        .build());
  }

  private static RequestDto makeRequest() {
    JsonNode message1 = new TextNode("message1");
    JsonNode message2 = new TextNode("message2");

    RequestSendMessageDto sendMessage = new RequestSendMessageDto();
    sendMessage.setMessages(List.of(message1, message2));

    RequestBodyDto body = new RequestBodyDto();
    body.setSendMessageDto(sendMessage);

    RequestDto request = new RequestDto();
    request.setBody(body);

    return request;
  }
}
