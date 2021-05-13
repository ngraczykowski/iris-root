package com.silenteight.warehouse.indexer.alert;

import lombok.SneakyThrows;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.DATA_INDEX_REQUEST_WITHOUT_MATCHES;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.DATA_INDEX_REQUEST_WITH_ALERTS;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.INDEX_NAME;
import static org.assertj.core.api.Assertions.*;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

class AlertServiceTest {

  private RestHighLevelClient restHighLevelClient;
  private AlertMapper alertMapper;
  private AlertService underTest;

  @BeforeEach
  public void init() {
    restHighLevelClient = mock(RestHighLevelClient.class);
    alertMapper = mock(AlertMapper.class);
    underTest = new AlertService(restHighLevelClient, alertMapper);
  }

  @Test
  void shouldThrowExceptionWhenAlertHasZeroMatches() {
    assertThrows(
        ZeroMatchesException.class,
        () -> underTest.indexAlert(DATA_INDEX_REQUEST_WITHOUT_MATCHES, INDEX_NAME));
  }

  @SneakyThrows
  @Test
  @Disabled("WEB-1106")
  void shouldCreateSingleDocumentForEachMatch() {
    ArgumentCaptor<BulkRequest> argumentCaptor = forClass(BulkRequest.class);
    doNothing().when(restHighLevelClient).bulk(argumentCaptor.capture(), eq(DEFAULT));

    underTest.indexAlert(DATA_INDEX_REQUEST_WITH_ALERTS, INDEX_NAME);

    int operationsCount = argumentCaptor.getValue().numberOfActions();
    assertThat(operationsCount).isEqualTo(4);
  }
}
