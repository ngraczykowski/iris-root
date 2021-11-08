package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;
import com.silenteight.hsbc.datasource.feature.geolocation.GeoPlaceOfBirthFeatureQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class GeoPlaceOfBirthFeatureQueryFacade implements GeoPlaceOfBirthFeatureQuery {

  private final IndividualComposite individualComposite;

  @Override
  public String getApGeoPlacesOfBirth() {
    var customerIndividuals = individualComposite.getCustomerIndividuals();
    var placesOfBirth = new ArrayList<String>();

    customerIndividuals.forEach(customerIndividual -> {
      var joinFields = GeoLocationExtractor.joinFields(
          customerIndividual.getCountryOfBirth(),
          customerIndividual.getStateProvinceOrCountyOfBirth(),
          customerIndividual.getTownOfBirth());

      var placeOfBirth = customerIndividual.getPlaceOfBirth();

      var fields = List.of(joinFields, placeOfBirth);

      placesOfBirth.add(GeoLocationExtractor.mergeFields(fields));
    });

    return placesOfBirth.stream().distinct().collect(Collectors.joining(" "));
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

    return fields.distinct().collect(Collectors.joining(" "));
  }

  private String worldCheckIndividualsGeoPlaceOfBirth() {
    var worldCheckIndividuals = individualComposite.getWorldCheckIndividuals();
    var fields = new WorldCheckIndividualsPlaceOfBirthExtractor(worldCheckIndividuals).extract();
    return GeoLocationExtractor.mergeFields(fields);
  }

  private String privateListIndividualsGeoPlaceOfBirth() {
    var privateListIndividuals = individualComposite.getPrivateListIndividuals();
    var fields = new PrivateListIndividualsPlaceOfBirthExtractor(privateListIndividuals).extract();
    return GeoLocationExtractor.mergeFields(fields);
  }

  private String ctrpScreeningIndividualsGeoPlaceOfBirth() {
    var ctrpScreeningIndividuals = individualComposite.getCtrpScreeningIndividuals();
    var fields = new CtrpScreeningExtractor(ctrpScreeningIndividuals).extract();
    return GeoLocationExtractor.mergeFields(fields);
  }
}
