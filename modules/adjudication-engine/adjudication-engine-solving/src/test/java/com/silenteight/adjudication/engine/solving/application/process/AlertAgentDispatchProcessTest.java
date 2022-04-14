package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.MatchesPublisher;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDao;
import com.silenteight.adjudication.engine.solving.data.MatchFeaturesFacade;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class AlertAgentDispatchProcessTest {

  @Mock
  private RabbitTemplate rabbitTemplate;
  @Mock
  private MatchFeaturesFacade matchFeaturesFacade;
  private AlertAgentDispatchProcess alertAgentDispatchProcess;
  private AlertSolvingRepository alertSolvingRepository = new InMemoryAlertSolvingRepository();

  @BeforeEach
  void setUp() {
    var matchesPublisher = new MatchesPublisher(rabbitTemplate);
    alertAgentDispatchProcess =
        new AlertAgentDispatchProcess(
            matchesPublisher, matchFeaturesFacade, alertSolvingRepository);
  }

  @Test
  void shouldCreateAndSendAlertSolving() {
    Mockito
        .when(matchFeaturesFacade.findAnalysisFeatures(
            Set.of(1L, 2L),
            Set.of(1L, 2L, 3L, 4L)))
        .thenReturn(List.of(
            new MatchFeatureDao(1, 1, 1, "features/name",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(1, 1, 1000, "features/nameMatchedText",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(2, 2, 1, "features/name",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(2, 2, 1000, "features/nameMatchedText",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(2, 3, 1, "features/name",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(2, 3, 1000, "features/nameMatchedText",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(3, 4, 1, "features/name",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(3, 5, 1, "features/name",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(3, 5, 1000, "features/nameMatchedText",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(3, 6, 1, "features/name",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(3, 6, 1000, "features/nameMatchedText",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(4, 7, 1, "features/name",
                "agents/name/versions/1.0.0/configs/1"),
            new MatchFeatureDao(4, 7, 1000, "features/nameMatchedText",
                "agents/name/versions/1.0.0/configs/1")));
    alertAgentDispatchProcess.handle(AnalysisAlertsAdded
        .newBuilder()
        .addAnalysisAlerts("analysis/1/alerts/1")
        .addAnalysisAlerts("analysis/1/alerts/2")
        .addAnalysisAlerts("analysis/2/alerts/3")
        .addAnalysisAlerts("analysis/2/alerts/4")
        .build());

    Assertions
        .assertThat(alertSolvingRepository.get(1L))
        .extracting("matches")
        .asInstanceOf(InstanceOfAssertFactories.MAP)
        .containsOnlyKeys(1L);
    Assertions
        .assertThat(alertSolvingRepository.get(2L))
        .extracting("matches")
        .asInstanceOf(InstanceOfAssertFactories.MAP)
        .containsOnlyKeys(2L, 3L);
    Assertions
        .assertThat(alertSolvingRepository.get(3L))
        .extracting("matches")
        .asInstanceOf(InstanceOfAssertFactories.MAP)
        .containsOnlyKeys(4L, 5L, 6L);
    Assertions
        .assertThat(alertSolvingRepository.get(4L))
        .extracting("matches")
        .asInstanceOf(InstanceOfAssertFactories.MAP)
        .containsOnlyKeys(7L);
  }
}
