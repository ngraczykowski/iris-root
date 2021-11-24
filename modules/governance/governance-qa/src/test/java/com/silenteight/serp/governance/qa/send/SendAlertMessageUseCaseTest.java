package com.silenteight.serp.governance.qa.send;

import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.data.api.v2.QaDataIndexRequest;
import com.silenteight.serp.governance.qa.send.amqp.AlertMessageGateway;
import com.silenteight.serp.governance.qa.send.dto.AlertDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.serp.governance.qa.AlertFixture.generateAlertName;
import static com.silenteight.serp.governance.qa.DecisionFixture.COMMENT_OK;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.PASSED;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendAlertMessageUseCaseTest {

  @Mock
  private AlertMessageGateway alertMessageGateway;

  private SendAlertMessageUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new SendAlertMessageConfiguration().sendAlertMessageUseCase(alertMessageGateway);
  }

  @Test
  void activateShouldSendQaDataIndexRequestMessageWithOneAlert() {
    //given
    ArgumentCaptor<QaDataIndexRequest> messageCaptor = ArgumentCaptor
        .forClass(QaDataIndexRequest.class);
    AlertDto alertDto = getAlertDto();
    SendAlertMessageCommand sendAlertMessageCommand = SendAlertMessageCommand.of(of(alertDto));
    //when
    underTest.activate(sendAlertMessageCommand);
    //then
    verify(alertMessageGateway, times(1)).send(messageCaptor.capture());
    assertThat(messageCaptor.getValue().getAlertsCount()).isEqualTo(1);
    assertThat(messageCaptor.getValue().getRequestId()).isEmpty();
    QaAlert capturedAlert = messageCaptor.getValue().getAlerts(0);
    assertThat(capturedAlert.getName()).isEqualTo(alertDto.getAlertName());
    assertThat(capturedAlert.getLevel()).isEqualTo(alertDto.getLevel().getValue());
    assertThat(capturedAlert.getState().name()).isEqualTo(alertDto.getState().name());
    assertThat(capturedAlert.getComment()).isEqualTo(alertDto.getComment());
  }

  private AlertDto getAlertDto() {
    return AlertDto.builder()
        .alertName(generateAlertName())
        .level(ANALYSIS)
        .state(PASSED)
        .comment(COMMENT_OK)
        .build();
  }
}
