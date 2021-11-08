package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.geolocation.GeoLocationExtractor.SignType;
import com.silenteight.hsbc.datasource.extractors.name.Party;
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer;
import com.silenteight.hsbc.datasource.feature.geolocation.GeoResidencyFeatureQuery;

import one.util.streamex.EntryStream;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class GeoResidenciesFeatureQueryFacade implements GeoResidencyFeatureQuery {

  private final MatchData matchData;
  private final CountryDiscoverer countryDiscoverer;

  @Override
  public String getApIndividualsGeoResidencies(Party party) {
    var addresses = new CustomerIndividualsAddressesExtractor(matchData).extract();
    var countries = new CustomerIndividualsCountriesExtractor(matchData, countryDiscoverer).extract();
    var allNames = party.getAlertedPartyIndividuals();

    return getCustomersWithoutNamesAndWithCountries(addresses, countries, allNames);
  }

  @Override
  public String getApEntitiesGeoResidencies(List<String> entitiesAlertedPartyNames) {
    var addresses = new CustomerEntitiesAddressesExtractor(matchData).extract();
    var countries = new CustomerEntitiesCountriesExtractor(matchData, countryDiscoverer).extract();

    return getCustomersWithoutNamesAndWithCountries(addresses, countries, entitiesAlertedPartyNames);
  }

  @Override
  public String getMpIndividualsGeoResidencies() {
    var worldCheckIndividualsGeoResidencies = worldCheckIndividualsGeoResidencies();
    var privateListIndividualsGeoResidencies = privateListIndividualsGeoResidencies();
    var ctrpScreeningIndividualsGeoResidencies = ctrpScreeningIndividualsGeoResidencies();

    var fields = Stream.of(worldCheckIndividualsGeoResidencies, privateListIndividualsGeoResidencies,
        ctrpScreeningIndividualsGeoResidencies);

    return fields.distinct()
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.joining(SignType.SPACE.getSign()));
  }

  @Override
  public String getMpEntitiesGeoResidencies() {
    var ctrpScreeningEntitiesGeoResidencies = ctrpScreeningEntitiesGeoResidencies();
    var fields = Stream.of(ctrpScreeningEntitiesGeoResidencies);
    return fields.distinct().collect(Collectors.joining(" "));
  }


  private String getCustomersWithoutNamesAndWithCountries(
      List<List<String>> addresses, List<List<String>> countries, List<String> allNames) {
    validateSize(addresses, countries);

    var addressTransformers = EntryStream.of(addresses)
        .mapKeyValue((index, individualAddresses) -> CustomerAddressTransformer.builder()
            .addresses(individualAddresses)
            .countries(countries.get(index))
            .names(allNames)
            .build());

    return addressTransformers.map(CustomerAddressTransformer::getAddressWithoutNamesAndWithCountries)
        .distinct()
        .collect(Collectors.joining(SignType.SPACE.getSign()));
  }

  private void validateSize(List<List<String>> addresses, List<List<String>> countries) {
    if (addresses.size() != countries.size()) {
      throw new IllegalStateException(
          String.format(
              "Can not retrieve residencies for a given customers. Sizes don't match. Addresses size: %s, countries size: %s",
              addresses.size(), countries.size()));
    }
  }

  private String worldCheckIndividualsGeoResidencies() {
    var worldCheckIndividuals = matchData.getWorldCheckIndividuals();
    var fields = new WorldCheckIndividualsResidenciesExtractor(worldCheckIndividuals).extract();
    return GeoLocationExtractor.mergeFields(fields);
  }

  private String privateListIndividualsGeoResidencies() {
    var privateListIndividuals = matchData.getPrivateListIndividuals();
    var addresses = new PrivateListIndividualsAddressExtractor(privateListIndividuals).extract();
    var countries =
        countryDiscoverer.discover(new PrivateListIndividualsCountryExtractor(privateListIndividuals).extract());

    validateSize(addresses, countries);

    var addressTransformers = EntryStream.of(addresses)
        .mapKeyValue((index, address) -> CustomerAddressTransformer.builder()
            .addresses(address)
            .countries(countries.get(index))
            .build());

    return addressTransformers.map(CustomerAddressTransformer::getAddressWithCountry)
        .collect(Collectors.joining(SignType.SPACE.getSign()));
  }

  private String ctrpScreeningIndividualsGeoResidencies() {
    var ctrpScreeningIndividuals = matchData.getCtrpScreeningIndividuals();
    var fields = new CtrpScreeningExtractor(ctrpScreeningIndividuals).extract();
    return GeoLocationExtractor.mergeFields(fields);
  }

  private String ctrpScreeningEntitiesGeoResidencies() {
    var ctrpScreeningEntities = matchData.getCtrpScreeningEntities();
    var fields = new CtrpScreeningExtractor(ctrpScreeningEntities).extract();
    return GeoLocationExtractor.mergeFields(fields);
  }
}
