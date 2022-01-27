package com.silenteight.warehouse.indexer.query.single;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.alert.AlertColumnName;
import com.silenteight.warehouse.indexer.alert.AlertRepository;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;
import com.silenteight.warehouse.indexer.query.MultiValueEntry;

import com.google.common.collect.ListMultimap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableListMultimap.toImmutableListMultimap;
import static com.silenteight.warehouse.indexer.alert.AlertColumnName.CREATE_AT;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_PREFIX;

@RequiredArgsConstructor
public class RandomPostgresSearchAlertQueryService implements RandomAlertService {

  private static final Map<String, AlertColumnName> MAPPING_COLUMN_NAME =
      Map.of("index_timestamp", CREATE_AT);

  private static final List<String> ALERT_NAME_PROPERTY_NAME = List.of("s8_alert_name");

  private static final Map<String, String> MAPPING_PAYLOAD_PROPERTY_NAME =
      Map.of("s8_discriminator", "discriminator", "s8_country", "s8_lobCountryCode");

  @Autowired
  private final AlertRepository alertRepository;

  @Override
  public List<String> getRandomAlertNameByCriteria(AlertSearchCriteria alertSearchCriteria) {

    AlertColumnName alertColumnName = MAPPING_COLUMN_NAME.getOrDefault(
        alertSearchCriteria.getTimeFieldName(),
        CREATE_AT);

    // Data in alert table are stored mostly in jsonb and the most filtering is done there, however
    // there are properties which are kept as separate column e.g. alert name. Filters in alert
    // repository only use payload for filtration se we need to extract all the data which is
    // in columns
    ListMultimap<String, List<String>> filtersWithoutAlertName = alertSearchCriteria.getFilter()
        .stream()
        .filter(multiValueEntry -> !ALERT_NAME_PROPERTY_NAME.contains(multiValueEntry.getField()))
        .collect(toImmutableListMultimap(
            multiValueEntry -> replacePayloadPropertyName(multiValueEntry.getField()),
            MultiValueEntry::getValues));

    List<String> alertNames = alertSearchCriteria.getFilter().stream()
        .filter(entry -> ALERT_NAME_PROPERTY_NAME.contains(entry.getField()))
        .findFirst().map(MultiValueEntry::getValues).orElse(List.of());

    ListMultimap<String, List<String>> filterWithoutAlertPrefix =
        filtersWithoutAlertName.entries().stream().collect(toImmutableListMultimap(
            entry -> removeAlertPrefix(entry.getKey()), Entry::getValue));

    return alertRepository
        .fetchRandomAlerts(
            alertColumnName,
            alertSearchCriteria.getTimeRangeFrom(),
            alertSearchCriteria.getTimeRangeTo(),
            alertSearchCriteria.getAlertLimit(),
            filterWithoutAlertPrefix, alertNames)
        .stream()
        .map(AlertDto::getName)
        .collect(toImmutableList());
  }

  private static String replacePayloadPropertyName(String propertyName) {
    return MAPPING_PAYLOAD_PROPERTY_NAME.getOrDefault(propertyName, propertyName);
  }

  private static String removeAlertPrefix(String propertyName) {
    if (propertyName.startsWith(ALERT_PREFIX)) {
      return propertyName.replace(ALERT_PREFIX, "");
    }
    return propertyName;
  }
}
