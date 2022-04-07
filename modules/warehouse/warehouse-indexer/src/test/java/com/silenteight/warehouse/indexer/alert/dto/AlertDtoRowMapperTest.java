package com.silenteight.warehouse.indexer.alert.dto;

import com.silenteight.warehouse.indexer.alert.AlertDtoRowMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertDtoRowMapperTest {

  private static final Timestamp RECOMMENDATION_TIMESTAMP =
      Timestamp.valueOf("2020-09-01 09:01:15");
  private static final Timestamp CREATE_TIMESTAMP = Timestamp.valueOf("2020-09-02 09:01:15");

  private final RowMapper<AlertDto> mapper = new AlertDtoRowMapper();
  @Mock
  private ResultSet resultSet;

  @Test
  void crateAlertDto_payloadAsJson_mapCreatedWithValues() throws Exception {
    // Given
    mockResultSet("{\"propertyKey\":\"propertyValue\"}");

    // When
    AlertDto result = mapper.mapRow(resultSet, 1);

    // Then
    assertThat(result).isEqualTo(buildAlertDto(
        Map.of("propertyKey", "propertyValue")));
  }

  private void mockResultSet(String payload) throws Exception {
    when(resultSet.getLong("id")).thenReturn(1L);
    when(resultSet.getString("name")).thenReturn("alertName");
    when(resultSet.getString("discriminator")).thenReturn("alertDiscriminator");
    when(resultSet.getTimestamp("recommendation_date")).thenReturn(RECOMMENDATION_TIMESTAMP);
    when(resultSet.getTimestamp("created_at")).thenReturn(CREATE_TIMESTAMP);
    when(resultSet.getString("payload")).thenReturn(payload);
  }

  private AlertDto buildAlertDto(Map<String, String> payload) {
    return AlertDto
        .builder()
        .id(1L)
        .name("alertName")
        .discriminator("alertDiscriminator")
        .recommendationDate(
            RECOMMENDATION_TIMESTAMP)
        .createdAt(CREATE_TIMESTAMP)
        .payload(payload)
        .build();
  }

  @Test
  void crateAlertDto_payloadIsNotJson_emptyMapCreated() throws Exception {
    // Given
    mockResultSet("wrongPayload");

    // When
    AlertDto result = mapper.mapRow(resultSet, 1);

    // Then
    assertThat(result).isEqualTo(buildAlertDto(Map.of()));
  }

  @Test
  void crateAlertDto_payloadAsEmptyString_emptyMapCreated() throws Exception {
    // Given
    mockResultSet("");

    // When
    AlertDto result = mapper.mapRow(resultSet, 1);

    // Then
    assertThat(result).isEqualTo(buildAlertDto(Map.of()));
  }
}