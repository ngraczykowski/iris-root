package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;

import com.silenteight.hsbc.bridge.alert.dto.MetadataKey;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation;
import com.silenteight.hsbc.bridge.json.external.model.CustomerEntity;
import com.silenteight.hsbc.bridge.json.external.model.CustomerIndividual;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

class AlertMetadataCollector {

  public static final String UK_COUNTRY_NAME = "UK";
  public static final String GB_COUNTRY_NAME = "GB";

  Collection<AlertMetadata> collectFromAlertData(@NonNull AlertData alertData) {

    var metadataCollection = new HashSet<>(collect(alertData.getCaseInformation()));
    collectFromEntity(alertData.getCustomerEntities()).ifPresent(metadataCollection::add);
    collectFromIndividual(alertData.getCustomerIndividuals()).ifPresent(metadataCollection::add);

    return metadataCollection;
  }

  private Optional<AlertMetadata> collectFromIndividual(List<CustomerIndividual> individuals) {
    return individuals.stream()
        .map(a -> new AlertMetadata(
            MetadataKey.S8_LOB_COUNTRY_CODE,
            mapSpecialCountry(a.getEdqLobCountryCode())))
        .findFirst();
  }

  private Optional<AlertMetadata> collectFromEntity(List<CustomerEntity> entities) {
    return entities.stream()
        .map(a -> new AlertMetadata(
            MetadataKey.S8_LOB_COUNTRY_CODE,
            mapSpecialCountry(a.getEdqLobCountryCode())))
        .findFirst();
  }

  private static List<AlertMetadata> collect(CaseInformation info) {
    return List.of(
        new AlertMetadata(MetadataKey.DISCRIMINATOR, info.getKeyLabel() + "_" + info.getFlagKey()),
        new AlertMetadata(MetadataKey.EXTENDED_ATTRIBUTE_5, info.getExtendedAttribute5()),
        new AlertMetadata(MetadataKey.TRACKING_ID, info.getFlagKey())
    );
  }

  private String mapSpecialCountry(String countryName) {
    if (countryName.equals(UK_COUNTRY_NAME)) {
      return GB_COUNTRY_NAME;
    }
    return countryName;
  }
}
