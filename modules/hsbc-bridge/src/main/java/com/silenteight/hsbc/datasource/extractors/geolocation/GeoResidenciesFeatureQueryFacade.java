package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.geolocation.GeoResidencyFeatureQuery;

import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.mergeFields;
import static java.util.List.of;
import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
public class GeoResidenciesFeatureQueryFacade implements GeoResidencyFeatureQuery {

  private final MatchData matchData;

  @Override
  public String getApIndividualsGeoResidencies() {
    var customerIndividuals = matchData.getCustomerIndividual();
    var fields = of(customerIndividuals.getAddress(), customerIndividuals.getProfileFullAddress());
    return mergeFields(fields);
  }

  @Override
  public String getApEntitiesGeoResidencies() {
    var customerEntity = matchData.getCustomerEntity();
    var fields = of(customerEntity.getAddress(), customerEntity.getProfileFullAddress());
    return mergeFields(fields);
  }

  @Override
  public String getMpIndividualsGeoResidencies() {
    var worldCheckIndividualsGeoResidencies = worldCheckIndividualsGeoResidencies();
    var privateListIndividualsGeoResidencies = privateListIndividualsGeoResidencies();
    var ctrpScreeningIndividualsGeoResidencies = ctrpScreeningIndividualsGeoResidencies();

    var fields = Stream.of(
        worldCheckIndividualsGeoResidencies,
        privateListIndividualsGeoResidencies,
        ctrpScreeningIndividualsGeoResidencies);

    return fields.distinct().collect(joining(" "));
  }

  @Override
  public String getMpEntitiesGeoResidencies() {
    var ctrpScreeningEntitiesGeoResidencies = ctrpScreeningEntitiesGeoResidencies();
    var fields = Stream.of(ctrpScreeningEntitiesGeoResidencies);
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
