package com.silenteight.warehouse.indexer.query.grouping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.alert.AlertColumnName;
import com.silenteight.warehouse.indexer.alert.AlertRepository;
import com.silenteight.warehouse.indexer.alert.dto.AlertGroupingDto;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants;
import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;

import com.google.common.collect.ListMultimap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableListMultimap.toImmutableListMultimap;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.removeAlertPrefix;

@RequiredArgsConstructor
public class GroupingQueryPostgresService implements GroupingQueryService {

  @NonNull
  private final AlertRepository alertRepository;

  private Map<String, String> prefixNonPrefixFieldsMap;

  @Override
  public FetchGroupedDataResponse generate(
      FetchGroupedTimeRangedDataRequest fetchGroupedDataRequest) {
    ListMultimap<String, List<String>> filters =
        fetchGroupedDataRequest.getQueryFilters().stream().collect(
            toImmutableListMultimap(queryFilter ->
                removeAlertPrefix(queryFilter.getField()), QueryFilter::getAllowedValues));

    // This is needed to have backward compatibility as grouping fields can contain alert prefix,
    // and we are expecting as response also get it with prefix. However, sql database is not aware
    // about these prefixes, so we need to get rid of them before asking repository for data.
    prefixNonPrefixFieldsMap = fetchGroupedDataRequest
        .getFields()
        .stream()
        .collect(Collectors.toMap(AlertMapperConstants::removeAlertPrefix, Function.identity()));

    List<String> groupingFieldsWithoutPrefix = fetchGroupedDataRequest.getFields().stream().map(
        AlertMapperConstants::removeAlertPrefix).collect(Collectors.toList());

    List<AlertGroupingDto> alertGroupings = alertRepository.fetchGroupedAlerts(
        AlertColumnName.RECOMMENDATION_DATE, fetchGroupedDataRequest.getFrom(),
        fetchGroupedDataRequest.getTo(), filters, groupingFieldsWithoutPrefix);

    List<Row> rows = alertGroupings
        .stream()
        .map((alertGroupingDto -> Row
            .builder()
            .count(alertGroupingDto.getCount())
            .data(alertGroupingDto
                .getGroupedNameValueFields()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                    // Return to the original field with prefix
                    entry -> prefixNonPrefixFieldsMap.get(entry.getKey()),
                    Entry::getValue)))
            .build())).collect(Collectors.toList());

    return FetchGroupedDataResponse.builder().rows(rows).build();
  }
}
