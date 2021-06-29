package com.silenteight.serp.governance.qa.manage.check;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.parse;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ViewingDecisionHandlerJobTest {

  private static final OffsetDateTime NOW = parse("2021-06-16T08:09:25.481564Z");
  private DecisionService decisionService;
  private ViewingDecisionHandlerJob underTest;
  private final Integer maxMs = 45000;
  private final TimeSource timeSource = mock(TimeSource.class);

  @BeforeEach
  void setUp() {
    decisionService = mock(DecisionService.class);
    underTest = new ViewingDecisionHandlerJob(maxMs, decisionService, timeSource);
  }

  @Test
  void handlerJobShouldRestartDecision() {
    //given
    ArgumentCaptor<OffsetDateTime> olderThan = ArgumentCaptor.forClass(OffsetDateTime.class);
    ArgumentCaptor<Integer> limit = ArgumentCaptor.forClass(Integer.class);
    doNothing().when(decisionService).restartViewingDecisions(any(), any());
    doReturn(NOW).when(timeSource).offsetDateTime();
    //when
    underTest.run();
    //then
    verify(decisionService, times(1))
        .restartViewingDecisions(any(), any());
    verify(decisionService).restartViewingDecisions(olderThan.capture(), limit.capture());
    assertThat(olderThan.getValue()).isEqualTo(NOW.minus(maxMs, MILLIS));
    assertThat(limit.getValue()).isEqualTo(50);
  }
}