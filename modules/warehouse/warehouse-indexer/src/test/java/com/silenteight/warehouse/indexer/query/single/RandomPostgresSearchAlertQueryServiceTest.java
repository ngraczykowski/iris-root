package com.silenteight.warehouse.indexer.query.single;

import com.silenteight.warehouse.indexer.alert.AlertRepository;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;
import com.silenteight.warehouse.indexer.query.MultiValueEntry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.warehouse.indexer.alert.AlertColumnName.CREATED_AT;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RandomPostgresSearchAlertQueryServiceTest {

  private static final AlertDto ALERT_DTO = AlertDto
      .builder()
      .id(1L)
      .name("alertName")
      .discriminator("test_desc")
      .createdAt(
          Date.from(Instant.now()))
      .recommendationDate(Date.from(Instant.now()))
      .payload("{}")
      .build();

  private static final AlertSearchCriteria.AlertSearchCriteriaBuilder BUILDER = AlertSearchCriteria
      .builder()
      .alertLimit(2)
      .timeFieldName("timeFieldName")
      .timeRangeTo("timeTo")
      .timeRangeFrom("timeFrom");

  @Mock
  AlertRepository alertRepository;

  @InjectMocks
  private RandomPostgresSearchAlertQueryService service;

  static Stream<Arguments> getAlertSearchCriteria() {
    return Stream.of(
        Arguments.of(
            buildAlertSearchCriteria(
                ImmutableList.of()), ImmutableListMultimap.of(), ImmutableList.of()),
        Arguments.of(
            buildAlertSearchCriteria(
                ImmutableList.of(
                    new MultiValueEntry("alert_randomField", ImmutableList.of("randomValue")))),
            ImmutableListMultimap.of("randomField", ImmutableList.of("randomValue")),
            ImmutableList.of()),
        Arguments.of(
            buildAlertSearchCriteria(
                ImmutableList.of(
                    new MultiValueEntry("alert_withPrefix", ImmutableList.of("randomValue")))),
            ImmutableListMultimap.of("withPrefix", ImmutableList.of("randomValue")),
            ImmutableList.of()),
        Arguments.of(
            buildAlertSearchCriteria(
                ImmutableList.of(
                    new MultiValueEntry("randomField", ImmutableList.of("randomValue")),
                    new MultiValueEntry("s8_discriminator", ImmutableList.of("discriminator")),
                    new MultiValueEntry("s8_country", ImmutableList.of("PL")))),
            ImmutableListMultimap.of(
                "randomField", ImmutableList.of("randomValue"), "discriminator",
                ImmutableList.of("discriminator"), "s8_lobCountryCode",
                ImmutableList.of("PL")),
            ImmutableList.of()),
        Arguments.of(
            buildAlertSearchCriteria(
                ImmutableList.of(
                    new MultiValueEntry("randomField", ImmutableList.of("randomValue")),
                    new MultiValueEntry(
                        "s8_alert_name", ImmutableList.of("alertName1", "alertName2")))),
            ImmutableListMultimap.of(
                "randomField", ImmutableList.of("randomValue")),
            ImmutableList.of("alertName1", "alertName2"))
    );
  }

  private static AlertSearchCriteria buildAlertSearchCriteria(List<MultiValueEntry> filters) {
    return BUILDER.filter(filters).build();
  }

  @BeforeEach
  void setUp() {
    when(alertRepository.fetchRandomAlerts(any(), anyString(), anyString(), anyInt(), any(),
        any())).thenReturn(
        ImmutableList.of(ALERT_DTO));
  }

  @ParameterizedTest
  @MethodSource("getAlertSearchCriteria")
  void shouldPropertyInvokeRepositoryBasedOnCriteria(
      AlertSearchCriteria alertSearchCriteria,
      ImmutableListMultimap<String, List<String>> expectedFilters,
      List<String> expectedAlertNames) {

    // When
    List<String> alerts = service.getRandomAlertNameByCriteria(alertSearchCriteria);

    // Then
    assertThat(alerts.size()).isEqualTo(1);
    assertThat(alerts.get(0)).isEqualTo("alertName");
    verify(alertRepository).fetchRandomAlerts(
        CREATED_AT, "timeFrom", "timeTo", 2,
        expectedFilters, expectedAlertNames);
  }
}
