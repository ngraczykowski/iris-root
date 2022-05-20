package com.silenteight.warehouse.indexer.alert;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.alert.dto.AlertDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Mapper which translates data from warehouse_alert into {@code AlertDto}
 */
@Slf4j
@NoArgsConstructor
public final class AlertDtoRowMapper implements RowMapper<AlertDto> {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final TypeReference<Map<String, String>> MAP_TYPE_REFERENCE
      = new TypeReference<>() {};

  @Override
  public AlertDto mapRow(ResultSet rs, int rowNum) throws SQLException {

    Map<String, String> payload;
    String rawPayload = rs.getString("payload");
    try {
      payload = OBJECT_MAPPER.readValue(rawPayload, MAP_TYPE_REFERENCE);
    } catch (JsonProcessingException e) {
      log.error(
          "Warehouse alert payload: {} can not be mapped to Map<String, String>", rawPayload, e);
      payload = Map.of();
    }
    return AlertDto
        .builder()
        .id(rs.getLong("id"))
        .name(rs.getString("name"))
        .discriminator(rs.getString("discriminator"))
        .recommendationDate(rs.getTimestamp("recommendation_date"))
        .createdAt(rs.getTimestamp(AlertColumnName.CREATED_AT.getName()))
        .payload(payload)
        .build();
  }
}
