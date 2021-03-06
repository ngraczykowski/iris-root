package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.common.database.partition.DatabasePartitionConfiguration;
import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto.Body;
import com.silenteight.connector.ftcc.common.dto.output.FircoAuthenticationDto;
import com.silenteight.connector.ftcc.common.dto.output.ReceiveDecisionDto;
import com.silenteight.connector.ftcc.common.dto.output.ReceiveDecisionMessageDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

import static com.silenteight.connector.ftcc.common.dto.output.AckDto.ok;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(
    classes = { DatabasePartitionConfiguration.class, CallbackTestConfiguration.class })
class CallbackRequestServiceTest extends BaseDataJpaTest {

  @MockBean
  private RestTemplate restTemplate;

  @MockBean
  private MessageDetailsService messageDetailsService;

  @SpyBean
  @Autowired
  private CallbackRequestRepository callbackRequestRepository;

  @Autowired
  private PartitionCreator partitionCreator;

  @Autowired
  private Clock clock;

  @DisplayName("Save 2 callback request details, should be stored in DB")
  @Test
  void verifyStoreCallbackDetails() {
    var callbackRequestService = new CallbackRequestService(
        callbackRequestRepository, partitionCreator, clock);
    var batchName = BatchResource.toResourceName(UUID.randomUUID());
    var clientRequestDto = buildClientRequestDto();

    callbackRequestService.save(batchName, clientRequestDto, "http://localhost:8080/callback",
        ok(), 100);
    callbackRequestService.save(batchName, clientRequestDto, "http://localhost:8080/callback",
        ok(), 200);

    var all = callbackRequestRepository.findAll();
    assertThat(all)
        .hasSize(2)
        .allMatch(callbackRequestEntity -> callbackRequestEntity.getCode() == 200
            || callbackRequestEntity.getCode() == 100);
  }

  @DisplayName("When exception occurred, there should be no side effect")
  @Test
  void exceptionShouldProcessWithoutStorage() {
    var callbackRequestService = new CallbackRequestService(
        callbackRequestRepository, partitionCreator, clock);
    var batchName = BatchResource.toResourceName(UUID.randomUUID());
    var clientRequestDto = buildClientRequestDto();

    doThrow(new DataAccessResourceFailureException("DB connection"))
        .when(this.callbackRequestRepository)
        .save(any());

    callbackRequestService.save(batchName, clientRequestDto, "http://localhost:8080/callback",
        ok(), 100);
    var all = callbackRequestRepository.findAll();
    assertThat(all).isEmpty();
  }

  private ClientRequestDto buildClientRequestDto() {
    var receiveDecisionDto = ReceiveDecisionDto.builder();
    List<ReceiveDecisionMessageDto> decisionMessageDtos = List.of();
    receiveDecisionDto.messages(decisionMessageDtos);

    FircoAuthenticationDto authentication = FircoAuthenticationDto.builder()
        .continuityLogin("login")
        .continuityPassword("password")
        .build();

    receiveDecisionDto.authentication(authentication);

    return ClientRequestDto.builder()
        .body(new Body(receiveDecisionDto.build()))
        .build();
  }
}
