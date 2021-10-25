package com.silenteight.warehouse.indexer.production.v2;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.warehouse.indexer.alert.indexing.MapWithIndex;
import com.silenteight.warehouse.indexer.alert.mapping.AlertDefinition;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapper;
import com.silenteight.warehouse.indexer.match.mapping.MatchDefinition;
import com.silenteight.warehouse.indexer.match.mapping.MatchMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ProductionAlertMappingService {

  @NonNull
  private final AlertMapper alertMapper;
  @NonNull
  private final MatchMapper matchMapper;

  List<MapWithIndex> mapFields(List<AlertWithIndex> alerts) {
    return alerts.stream()
        .map(this::toMapWithIndices)
        .flatMap(Collection::stream)
        .collect(toList());
  }

  private List<MapWithIndex> toMapWithIndices(AlertWithIndex alert) {
    Map<String, Object> alertPayload =
        alertMapper.convertAlertToAttributes(toAlertDefinition(alert.getAlert()));

    List<MapWithIndex> mapWithIndices = new ArrayList<>();
    mapWithIndices.add(new MapWithIndex(
        alert.getAlertIndexName(),
        alert.getAlert().getDiscriminator(),
        alertPayload));
    mapWithIndices.addAll(toMapWithIndices(alertPayload, alert.getMatches()));

    return mapWithIndices;
  }

  private static AlertDefinition toAlertDefinition(Alert alert) {
    return AlertDefinition.builder()
        .discriminator(alert.getDiscriminator())
        .name(alert.getName())
        .payload(alert.getPayload())
        .build();
  }

  private List<MapWithIndex> toMapWithIndices(
      Map<String, Object> alertPayload, List<MatchWithIndex> matches) {

    return matches
        .stream()
        .map(match -> this.toMapWithIndex(alertPayload, match))
        .collect(toList());
  }

  private MapWithIndex toMapWithIndex(Map<String, Object> alertPayload, MatchWithIndex match) {
    Map<String, Object> matchPayload =
        matchMapper.convertMatchToAttributes(alertPayload, toMatchDefinition(match.getMatch()));
    String discriminator = match.getMatch().getDiscriminator();
    String indexName = match.getMatchIndexName();

    return new MapWithIndex(indexName, discriminator, matchPayload);
  }

  private static MatchDefinition toMatchDefinition(Match match) {
    return MatchDefinition.builder()
        .discriminator(match.getDiscriminator())
        .name(match.getName())
        .payload(match.getPayload())
        .build();
  }
}
