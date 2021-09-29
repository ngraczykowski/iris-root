package com.silenteight.warehouse.indexer.indextracking;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.indextracking.IndexTrackingFixtures.DISCRIMINATOR_NEW;
import static com.silenteight.warehouse.indexer.indextracking.IndexTrackingFixtures.DISCRIMINATOR_OLD;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = IndexTrackingTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
@Transactional
class ProductionAlertTrackingServiceTest {

  @Autowired
  ProductionAlertTrackingService underTest;

  @Autowired
  ProductionAlertRepository productionAlertRepository;

  @Test
  void shouldHandleGracefullyEmptyList() {
    Map<String, String> alertDateByDiscriminator =
        underTest.getIndexNameByDiscriminator(emptyList());

    assertThat(alertDateByDiscriminator).isEmpty();
  }

  @Test
  void shouldReturnCurrentDateForNewAlerts() {
    Map<String, String> alertDateByDiscriminator =
        underTest.getIndexNameByDiscriminator(List.of(DISCRIMINATOR_NEW));

    assertThat(alertDateByDiscriminator).containsExactlyInAnyOrderEntriesOf(Map.of(
        DISCRIMINATOR_NEW, "itest_production.2021-07-22"
    ));
  }

  @Test
  void shouldReturnCurrentDateForNewAlertsAndKeepOldDatesForOldAlerts() {
    productionAlertRepository.updateIfNotExists(DISCRIMINATOR_OLD, "old");

    Map<String, String> indexNameByDiscriminator =
        underTest.getIndexNameByDiscriminator(List.of(DISCRIMINATOR_OLD, DISCRIMINATOR_NEW));

    assertThat(indexNameByDiscriminator).containsExactlyInAnyOrderEntriesOf(Map.of(
        DISCRIMINATOR_NEW, "itest_production.2021-07-22",
        DISCRIMINATOR_OLD, "old"
    ));
  }
}
