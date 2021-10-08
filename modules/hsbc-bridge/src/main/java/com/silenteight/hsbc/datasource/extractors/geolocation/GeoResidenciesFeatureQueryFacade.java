package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.geolocation.GeoResidencyFeatureQuery;

import java.util.Optional;

import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.SignType.SPACE;
import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.mergeFields;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
public class GeoResidenciesFeatureQueryFacade implements GeoResidencyFeatureQuery {

  private final MatchData matchData;

  @Override
  public String getApIndividualsGeoResidencies() {
    return Optional.ofNullable(matchData.getCustomerIndividuals())
        .map(entities ->
            entities.stream()
                .flatMap(ent -> of(ent.getAddress(), ent.getProfileFullAddress()))
                .map(GeoLocationExtractor::stripAndUpper)
                .distinct()
                .collect(joining(SPACE.getSign()))).orElse("");
  }

  @Override
  public String getApEntitiesGeoResidencies() {
    return Optional.ofNullable(matchData.getCustomerEntities())
        .map(entities ->
            entities.stream()
                .flatMap(ent -> of(ent.getAddress(), ent.getProfileFullAddress()))
                .map(GeoLocationExtractor::stripAndUpper)
                .distinct()
                .collect(joining(SPACE.getSign()))).orElse("");
  }

  @Override
  public String getMpIndividualsGeoResidencies() {
    var worldCheckIndividualsGeoResidencies = worldCheckIndividualsGeoResidencies();
    var privateListIndividualsGeoResidencies = privateListIndividualsGeoResidencies();
    var ctrpScreeningIndividualsGeoResidencies = ctrpScreeningIndividualsGeoResidencies();

    var fields = of(
        worldCheckIndividualsGeoResidencies,
        privateListIndividualsGeoResidencies,
        ctrpScreeningIndividualsGeoResidencies);

    return fields.distinct().collect(joining(" "));
  }

  @Override
  public String getMpEntitiesGeoResidencies() {
    var ctrpScreeningEntitiesGeoResidencies = ctrpScreeningEntitiesGeoResidencies();
    var fields = of(ctrpScreeningEntitiesGeoResidencies);
    return fields.distinct().collect(joining(" "));
  }

  private String worldCheckIndividualsGeoResidencies() {
    var worldCheckIndividuals = matchData.getWorldCheckIndividuals();
    var fields = new WorldCheckIndividualsResidenciesExtractor(worldCheckIndividuals).extract();
    return mergeFields(fields);
  }

  private String privateListIndividualsGeoResidencies() {
    var privateListIndividuals = matchData.getPrivateListIndividuals();
    var fields = new PrivateListIndividualsResidenciesExtractor(privateListIndividuals).extract();
    return mergeFields(fields);
  }

  private String ctrpScreeningIndividualsGeoResidencies() {
    var ctrpScreeningIndividuals = matchData.getCtrpScreeningIndividuals();
    var fields = new CtrpScreeningExtractor(ctrpScreeningIndividuals).extract();
    return mergeFields(fields);
  }

  private String ctrpScreeningEntitiesGeoResidencies() {
    var ctrpScreeningEntities = matchData.getCtrpScreeningEntities();
    var fields = new CtrpScreeningExtractor(ctrpScreeningEntities).extract();
    return mergeFields(fields);
  }
}
