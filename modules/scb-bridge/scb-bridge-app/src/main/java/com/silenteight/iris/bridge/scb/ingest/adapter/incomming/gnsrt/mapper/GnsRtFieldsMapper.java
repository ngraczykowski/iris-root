/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper;

import lombok.NoArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.ChineseNamesResolver;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@NoArgsConstructor
class GnsRtFieldsMapper {

  static Set<String> getOriginalChineseNames(ScreenableData data) {
    return ChineseNamesResolver.getChineseNames(data);
  }

  static List<String> getAlternateNames(ScreenableData data) {
    return collectNotBlankElements(asList(
        data.getFullLegalName(),
        data.getAlternateName1(),
        data.getAlternateName2(),
        data.getAlternateName3(),
        data.getAlternateNameRest(),
        data.getFullTradingName(),
        data.getLegalParentOrGroupName(),
        data.getExecutiveManagementNames(),
        data.getSupplementaryCardName()));
  }

  private static List<String> collectNotBlankElements(List<String> list) {
    return list.stream()
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
  }

  static List<String> getNationalities(ScreenableData data) {
    return collectNotBlankElements(asList(
        data.getNationalityAll(),
        data.getCountryOfBirthOrRegistration())
    );
  }

  static List<String> getResidencies(ScreenableData data) {
    return collectNotBlankElements(asList(
        data.getRegisteredOrResidentialAddressCountry(),
        data.getMailingOrCommunicationAddressCountry(),
        data.getOperatingOrOfficialAddressCountry(),
        data.getOtherAddressCountry(),
        data.getRegisteredAddressCountryOfHeadOffice(),
        data.getRegisteredAddressCountryOfParentCompany(),
        data.getEstablishmentCountryOfHO(),
        data.getEstablishmentCountryOfParentCompany())
    );
  }

  static List<String> getResidentialAddresses(ScreenableData data) {
    return collectNotBlankElements(asList(
        data.getRegisteredOrResidentialAddress(),
        data.getMailingOrCommunicationAddress(),
        data.getOperatingOrOfficialAddress(),
        data.getOtherAddress(),
        data.getRegisteredAddressOfHeadOffice(),
        data.getRegisteredAddressOfParentCompany(),
        data.getNameOfAuthority())
    );
  }

  static List<String> getIdentifications(ScreenableData data) {
    return collectNotBlankElements(asList(
        data.getIdentificationNumber1(),
        data.getIdentificationNumber2(),
        data.getIdentificationNumber3(),
        data.getIdentificationNumber4(),
        data.getIdentificationNumberRest())
    );
  }
}
