package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;
import com.silenteight.hsbc.datasource.feature.geolocation.GeoPlaceOfBirthFeatureQuery;

import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.joinFields;
import static com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.mergeFields;
import static java.util.List.of;
import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
public class GeoPlaceOfBirthFeatureQueryFacade implements GeoPlaceOfBirthFeatureQuery {

  private final IndividualComposite individualComposite;

  @Override
  public String getApGeoPlacesOfBirth() {
    var customerIndividuals = individualComposite.getCustomerIndividual();

    var joinFields = joinFields(
        customerIndividuals.getCountryOfBirth(),
        customerIndividuals.getStateProvinceOrCountyOfBirth(),
        customerIndividuals.getTownOfBirth());

    var placeOfBirth = customerIndividuals.getPlaceOfBirth();

    var fields = of(joinFields, placeOfBirth);

    return mergeFields(fields);
  }

  @Override
  public String getMpGeoPlaceOfBirth() {
    var worldCheckIndividualsGeoPlaceOfBirth = worldCheckIndividualsGeoPlaceOfBirth();
    var privateListIndividualsGeoPlaceOfBirth = privateListIndividualsGeoPlaceOfBirth();
    var ctrpScreeningIndividualsGeoPlaceOfBirth = ctrpScreeningIndividualsGeoPlaceOfBirth();

    var fields = Stream.of(
        worldCheckIndividualsGeoPlaceOfBirth,
        privateListIndividualsGeoPlaceOfBirth,
        ctrpScreeningIndividualsGeoPlaceOfBirth);

    return fields.distinct().collect(joining(" "));
  }

  private String worldCheckIndividualsGeoPlaceOfBirth() {
    var worldCheckIndividuals = individualComposite.getWorldCheckIndividuals();
    var fields = new WorldCheckIndividualsPlaceOfBirthExtractor(worldCheckIndividuals).extract();
    return mergeFields(fields);
  }

  private String privateListIndividualsGeoPlaceOfBirth() {
    var privateListIndividuals = individualComposite.getPrivateListIndividuals();
    var fields = new PrivateListIndividualsPlaceOfBirthExtractor(privateListIndividuals).extract();
    return mergeFields(fields);
  }

  private String ctrpScreeningIndividualsGeoPlaceOfBirth() {
    var ctrpScreeningIndividuals = individualComposite.getCtrpScreeningIndividuals();
    var fields = new CtrpScreeningExtractor(ctrpScreeningIndividuals).extract();
    return mergeFields(fields);
  }
}
