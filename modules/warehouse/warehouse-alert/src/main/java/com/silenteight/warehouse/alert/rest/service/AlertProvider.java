package com.silenteight.warehouse.alert.rest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
@Slf4j
public class AlertProvider {

  private final CountryPermissionService countryPermissionService;
  private final UserAwareTokenProvider userAwareTokenProvider;
  private final ObjectMapper objectMapper;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final AlertSecurityProperties alertSecurityProperties;

  public static final String GROUP_NAME = "kibana-sso";

  private final TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {};

  public Collection<Map<String, String>> getMultipleAlertsAttributes(
      List<String> fields, List<String> alertNameList) {
    List<String> countryList = getSecurityParametersList();
    MapSqlParameterSource parameterSource =
        new MapSqlParameterSource("names", alertNameList);
    String sqlToExecute = "SELECT * FROM warehouse_alert WHERE name IN (:names)";
    if (alertSecurityProperties.isEnabled()) {
      sqlToExecute += " AND (payload ->> 's8_lobCountryCode')  IN (:countries)";
      parameterSource.addValue(
          "countries",
          countryList.isEmpty() ? List.of("") : countryList);
    }
    return namedParameterJdbcTemplate.query(sqlToExecute, parameterSource,
        (rs, rowNum) -> getStringStringMap(fields, rs));
  }

  @NotNull
  private Map<String, String> getStringStringMap(List<String> fields, ResultSet rs) throws
      SQLException {
    Map<String, String> payloadMap;
    try {
      payloadMap = objectMapper.readValue(rs.getString("payload"), typeRef);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Incorrect JSON structure in warehouse_alert.payload!", e);
    }
    payloadMap.put("discriminator", rs.getString("discriminator"));
    Map<String, String> filteredPayload = new HashMap<>();
    for (String field : fields) {
      if (payloadMap.containsKey(field)) {
        filteredPayload.put(field, payloadMap.get(field));
      }
    }
    return filteredPayload;
  }

  private List<String> getSecurityParametersList() {
    Set<String> roles = userAwareTokenProvider.getRolesForGroup(GROUP_NAME);
    log.info("Current roles: {}", String.join(", ", roles));
    return List.copyOf(countryPermissionService.getCountries(roles));
  }
}
