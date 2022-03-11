package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsPartyIdentifications;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.ScreenableData;

import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.mapString;
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

    mapString(data.getSourceSystemIdentifier(), builder::apSrcSysId);
    mapString(data.getFullLegalName(), builder::apName);
    mapString(data.getClientType(), builder::custType);
    mapString(data.getDateOfBirthOrRegistration(), builder::apDobDoi);
    mapString(data.getAmlCountry(), builder::apDbCountry);
    mapString(data.getAmlCountry(), builder::apBookingLocation);
    mapString(data.getNationalityAll(), builder::apNationality);
    mapString(data.getCustomerStatus(), builder::apCustStatus);
    mapString(data.getGender(), builder::apGender);
    mapString(data.getRegisteredOrResidentialAddressCountry(), builder::apResidence);

    return builder.build();
  }

  private static String getGenderFromName(ScreenableData data) {
    var names =
        AlertParserUtils.expand(
            singletonList(data.getFullLegalName()), GnsRtFieldsMapper.getAlternateNames(data));
    return GenderDetector.determineApGenderFromName(data.getClientType(), names);
  }
}
