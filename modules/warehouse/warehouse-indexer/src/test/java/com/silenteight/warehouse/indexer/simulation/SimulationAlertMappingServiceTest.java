package com.silenteight.warehouse.indexer.simulation;

import com.silenteight.warehouse.indexer.alert.indexing.MapWithIndex;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapper;
import com.silenteight.warehouse.indexer.match.mapping.MatchMapper;
import com.silenteight.warehouse.indexer.query.single.AlertSearchService;
import com.silenteight.warehouse.indexer.query.single.ProductionSearchRequestBuilder;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.indexer.simulation.SimulationAlertFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationAlertMappingServiceTest {

  @InjectMocks
  private SimulationAlertMappingService underTest;

  @Mock
  private AlertMapper alertMapper;

  @Mock
  private MatchMapper matchMapper;

  @Mock
  private RestHighLevelClient restHighLevelClient;

  @Mock
  private AlertSearchService alertSearchService;

  @Mock
  ProductionSearchRequestBuilder productionSearchRequestBuilder;

  @Test
  void shouldMapFieldsForAlertWithNoMatches() {
    // given
    when(alertSearchService.searchForAlerts(any(), any())).thenReturn(List.of(PRODUCTION_DATA));
    when(alertMapper.convertAlertToAttributes(any())).thenReturn(ALERT_DATA);

    // when
    List<MapWithIndex> result = underTest.mapFields(List.of(ALERT_WITH_NO_MATCHES), INDEX_NAME);

    // then
    assertThat(result).isNotEmpty();
    MapWithIndex mapWithIndex = result.get(0);
    assertThat(result).isNotEmpty();
    assertThat(mapWithIndex.getIndexName()).isEqualTo(INDEX_NAME);
    assertThat(mapWithIndex.getDocumentId()).isEqualTo(ALERT_NAME);
    assertThat(mapWithIndex.getPayload()).isEqualTo(
        Map.of(
            COUNTRY_KEY, COUNTRY_VALUE,
            RECOMMENDATION_KEY, RECOMMENDATION_VALUE,
            STATUS_KEY, STATUS_VALUE));
  }

  @Test
  void shouldMapFieldsForAlertWithOneMatch() {
    // given
    when(alertSearchService.searchForAlerts(any(), any())).thenReturn(List.of(PRODUCTION_DATA));
    when(alertMapper.convertAlertToAttributes(any())).thenReturn(ALERT_DATA);
    when(matchMapper.convertMatchToAttributes(any(), any())).thenReturn(MATCH_DATA);

    // when
    List<MapWithIndex> result = underTest.mapFields(List.of(ALERT_WITH_ONE_MATCH), INDEX_NAME);

    // then
    assertThat(result).isNotEmpty();
    MapWithIndex mapWithIndex = result.get(0);
    assertThat(result).isNotEmpty();
    assertThat(mapWithIndex.getIndexName()).isEqualTo(INDEX_NAME);
    assertThat(mapWithIndex.getDocumentId()).isEqualTo(ALERT_NAME);
    assertThat(mapWithIndex.getPayload()).isEqualTo(
        Map.of(
            COUNTRY_KEY, COUNTRY_VALUE,
            RECOMMENDATION_KEY, RECOMMENDATION_VALUE,
            STATUS_KEY, STATUS_VALUE,
            ANALYST_DECISION_KEY, ANALYST_DECISION_1_VALUE));
  }

  @Test
  void shouldMapFieldsForAlertWithTwoMatches() {
    // given
    when(alertSearchService.searchForAlerts(any(), any())).thenReturn(List.of(PRODUCTION_DATA));
    when(alertMapper.convertAlertToAttributes(any())).thenReturn(ALERT_DATA);
    when(matchMapper.convertMatchToAttributes(any(), any())).thenReturn(MATCH_DATA);

    // when
    List<MapWithIndex> result = underTest.mapFields(List.of(ALERT_WITH_TWO_MATCHES), INDEX_NAME);

    // then
    assertThat(result).isNotEmpty();
    MapWithIndex mapWithIndex = result.get(0);
    assertThat(result).isNotEmpty();
    assertThat(mapWithIndex.getIndexName()).isEqualTo(INDEX_NAME);
    assertThat(mapWithIndex.getDocumentId()).isEqualTo(ALERT_NAME);
    assertThat(mapWithIndex.getPayload()).isEqualTo(
        Map.of(
            COUNTRY_KEY, COUNTRY_VALUE,
            RECOMMENDATION_KEY, RECOMMENDATION_VALUE,
            STATUS_KEY, STATUS_VALUE,
            ANALYST_DECISION_KEY, ANALYST_DECISION_1_VALUE));
  }
}
