/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.GnsPartyIdentifications;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.AlertParserUtils;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import static java.util.Collections.singletonList;

class GnsRtAlertedPartyCreator {

  static AlertedParty createAlertedParty(ScreenableData data, String recordId) {
    var gnsPartyIdentification = GnsPartyIdentifications.fromScreenableData(data);

    AlertedParty.AlertedPartyBuilder builder = AlertedParty.builder()
        .id(AlertParserUtils.makeAlertedPartyId(recordId, ""))
        .apId(data.getCustomerIdentificationNo())
        .apGenderFromName(getGenderFromName(data))
        .apDocNationalIds(gnsPartyIdentification.collectNationalIds())
        .apNameSynonyms(GnsRtFieldsMapper.getAlternateNames(data))
        .apOriginalCnNames(GnsRtFieldsMapper.getOriginalChineseNames(data))
        .apDocPassports(gnsPartyIdentification.collectPassportNumbers())
        .apNationalitySynonyms(GnsRtFieldsMapper.getNationalities(data))
        .apResidenceSynonyms(GnsRtFieldsMapper.getResidencies(data))
        .apResidentialAddresses(GnsRtFieldsMapper.getResidentialAddresses(data))
        .apDocOthers(GnsRtFieldsMapper.getIdentifications(data));

    AlertParserUtils.mapString(data.getSourceSystemIdentifier(), builder::apSrcSysId);
    AlertParserUtils.mapString(data.getFullLegalName(), builder::apName);
    AlertParserUtils.mapString(data.getClientType(), builder::custType);
    AlertParserUtils.mapString(data.getDateOfBirthOrRegistration(), builder::apDobDoi);
    AlertParserUtils.mapString(data.getAmlCountry(), builder::apDbCountry);
    AlertParserUtils.mapString(data.getAmlCountry(), builder::apBookingLocation);
    AlertParserUtils.mapString(data.getNationalityAll(), builder::apNationality);
    AlertParserUtils.mapString(data.getCustomerStatus(), builder::apCustStatus);
    AlertParserUtils.mapString(data.getGender(), builder::apGender);
    AlertParserUtils.mapString(data.getRegisteredOrResidentialAddressCountry(), builder::apResidence);

    return builder.build();
  }

  private static String getGenderFromName(ScreenableData data) {
    var names =
        AlertParserUtils.expand(
            singletonList(data.getFullLegalName()), GnsRtFieldsMapper.getAlternateNames(data));
    return GenderDetector.determineApGenderFromName(data.getClientType(), names);
  }
}
