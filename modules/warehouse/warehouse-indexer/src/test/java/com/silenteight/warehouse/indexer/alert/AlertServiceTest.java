package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.indexer.IndexerTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.DATA_INDEX_REQUEST_WITHOUT_MATCHES;
import static org.junit.Assert.*;


@SpringBootTest(classes = IndexerTestConfiguration.class)
@SpringIntegrationTest
class AlertServiceTest {

  @Autowired
  AlertService alertService;

  @Test
  void shouldThrowExceptionWhenAlertHasZeroMatches() {
    assertThrows(
        ZeroMatchesException.class,
        () -> alertService.indexAlert(DATA_INDEX_REQUEST_WITHOUT_MATCHES));
  }
}
