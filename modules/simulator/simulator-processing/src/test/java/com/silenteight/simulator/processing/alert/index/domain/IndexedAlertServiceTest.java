package com.silenteight.simulator.processing.alert.index.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.ALERT_COUNT;
import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.ANALYSIS_NAME;
import static com.silenteight.simulator.processing.alert.index.domain.IndexedAlertFixtures.REQUEST_ID;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { IndexedAlertTestConfiguration.class})
class IndexedAlertServiceTest extends BaseDataJpaTest {

  @Autowired
  IndexedAlertRepository repository;

  @Autowired
  IndexedAlertService underTest;

  @Test
  void shouldSaveAsSend() {
    //when
    underTest.saveAsSent(REQUEST_ID, ANALYSIS_NAME, ALERT_COUNT);

    //then
    Optional<IndexedAlertEntity> result = repository.findByAnalysisName(ANALYSIS_NAME);

    assertThat(result).isPresent();
    IndexedAlertEntity indexedAlert = result.get();
    assertThat(indexedAlert.getRequestId()).isEqualTo(REQUEST_ID);
    assertThat(indexedAlert.getAnalysisName()).isEqualTo(ANALYSIS_NAME);
    assertThat(indexedAlert.getAlertCount()).isEqualTo(ALERT_COUNT);
  }
}
