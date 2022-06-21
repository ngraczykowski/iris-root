package com.silenteight.warehouse.alert.rest.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.authorization.RoleAccessor;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.indexer.alert.AlertRepository;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;

import org.jetbrains.annotations.NotNull;

import java.util.*;

@AllArgsConstructor
@Slf4j
public class AlertProvider {

  private final CountryPermissionService countryPermissionService;
  private final RoleAccessor roleAccessor;
  private final AlertSecurityProperties alertSecurityProperties;
  private final AlertRepository alertRepository;

  public Collection<Map<String, String>> getMultipleAlertsAttributes(
      List<String> fields, List<String> alertNameList) {

    List<AlertDto> alertDtoList;

    if (alertSecurityProperties.isEnabled()) {
      List<String> countryList = getSecurityParametersList();
      countryList = countryList.isEmpty() ? List.of("") : countryList;
      alertDtoList = alertRepository.fetchAlertsByNamesAndCountries(alertNameList, countryList);
    } else {
      alertDtoList = alertRepository.fetchAlertsByNames(alertNameList);
    }

    return alertDtoList.stream().map(e -> getStringStringMap(e, fields)).toList();
  }

  @NotNull
  private static Map<String, String> getStringStringMap(AlertDto alertDto, List<String> fields) {
    Map<String, String> filteredPayload = new HashMap<>();
    filteredPayload.put("name", alertDto.getName());

    if (fields.contains("discriminator")) {
      filteredPayload.put("discriminator", alertDto.getDiscriminator());
    }

    for (String field : fields) {
      if (alertDto.getPayload().containsKey(field)) {
        filteredPayload.put(field, alertDto.getPayload().get(field));
      }
    }

    return filteredPayload;
  }

  private List<String> getSecurityParametersList() {
    Set<String> roles = roleAccessor.getRoles();
    log.info("Current roles: {}", String.join(", ", roles));
    return List.copyOf(countryPermissionService.getCountries(roles));
  }
}
