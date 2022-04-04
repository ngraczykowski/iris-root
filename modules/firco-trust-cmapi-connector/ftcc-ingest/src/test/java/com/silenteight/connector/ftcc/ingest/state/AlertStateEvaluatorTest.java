package com.silenteight.connector.ftcc.ingest.state;

import com.silenteight.connector.ftcc.ingest.state.exception.UnknownAlertStateException;
import com.silenteight.connector.ftcc.request.status.MessageCurrentStatusQuery;
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.connector.ftcc.ingest.state.AlertStateFixtures.BATCH_ID;
import static com.silenteight.connector.ftcc.ingest.state.AlertStateFixtures.FALSE_POSITIVE_STATE;
import static com.silenteight.connector.ftcc.ingest.state.AlertStateFixtures.FALSE_POSITIVE_STATUS;
import static com.silenteight.connector.ftcc.ingest.state.AlertStateFixtures.MESSAGE_ID;
import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.STATE_UNSPECIFIED;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertStateEvaluatorTest {

  @InjectMocks
  private AlertStateEvaluator underTest;

  @Mock
  private MessageCurrentStatusQuery messageCurrentStatusQuery;
  @Mock
  private AlertStateMapper alertStateMapper;

  @Test
  void shouldEvaluate() {
    // given
    when(messageCurrentStatusQuery.currentStatus(BATCH_ID, MESSAGE_ID))
        .thenReturn(FALSE_POSITIVE_STATUS);
    when(alertStateMapper.map(FALSE_POSITIVE_STATUS)).thenReturn(FALSE_POSITIVE_STATE);

    // when
    State state = underTest.evaluate(BATCH_ID, MESSAGE_ID);

    // then
    assertThat(state).isEqualTo(FALSE_POSITIVE_STATE);
  }

  @Test
  void shouldThrowExceptionIfUnspecifiedStateEvaluated() {
    // given
    String unknownState = "UNKNOWN";
    when(messageCurrentStatusQuery.currentStatus(BATCH_ID, MESSAGE_ID)).thenReturn(unknownState);
    when(alertStateMapper.map(unknownState)).thenReturn(STATE_UNSPECIFIED);

    // then
    assertThatThrownBy(
        () -> underTest.evaluate(BATCH_ID, MESSAGE_ID))
        .isInstanceOf(UnknownAlertStateException.class)
        .hasMessage(format("Unknown alert state for status: %s.", unknownState));
  }
}
