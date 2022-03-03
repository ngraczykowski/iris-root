package com.silenteight.customerbridge.gnsrt.mapper;

import com.silenteight.customerbridge.common.gender.GenderDetector;
import com.silenteight.customerbridge.common.gnsparty.GnsPartyIdentifications;
import com.silenteight.customerbridge.common.util.AlertParserUtils;
import com.silenteight.customerbridge.gnsrt.model.request.ScreenableData;
import com.silenteight.proto.serp.scb.v1.ScbAlertedPartyDetails;
import com.silenteight.proto.serp.v1.alert.Party;
import com.silenteight.proto.serp.v1.alert.Party.Source;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import static com.silenteight.customerbridge.common.util.AlertParserUtils.mapString;
import static java.util.Collections.singletonList;

class GnsRtAlertedPartyCreator {

  static Party createAlertedParty(ScreenableData data, String recordId) {
    var gnsPartyIdentification = GnsPartyIdentifications.fromScreenableData(data);

    var builder = ScbAlertedPartyDetails
        .newBuilder()
        .setApId(data.getCustomerIdentificationNo())
        .setApGenderFromName(getGenderFromName(data))
        .addAllApDocNationalIds(gnsPartyIdentification.collectNationalIds())
        .addAllApNameSynonyms(GnsRtFieldsMapper.getAlternateNames(data))
        .addAllApOriginalCnNames(GnsRtFieldsMapper.getOriginalChineseNames(data))
        .addAllApDocPassports(gnsPartyIdentification.collectPassportNumbers())
        .addAllApNationalitySynonyms(GnsRtFieldsMapper.getNationalities(data))
        .addAllApResidenceSynonyms(GnsRtFieldsMapper.getResidencies(data))
        .addAllApResidentialAddresses(GnsRtFieldsMapper.getResidentialAddresses(data))
        .addAllApDocOthers(GnsRtFieldsMapper.getIdentifications(data));

    mapString(data.getSourceSystemIdentifier(), builder::setApSrcSysId);
    mapString(data.getFullLegalName(), builder::setApName);
    mapString(data.getClientType(), builder::setCustType);
    mapString(data.getDateOfBirthOrRegistration(), builder::setApDobDoi);
    mapString(data.getAmlCountry(), builder::setApDbCountry);
    mapString(data.getAmlCountry(), builder::setApBookingLocation);
    mapString(data.getNationalityAll(), builder::setApNationality);
    mapString(data.getCustomerStatus(), builder::setApCustStatus);
    mapString(data.getGender(), builder::setApGender);
    mapString(data.getRegisteredOrResidentialAddressCountry(), builder::setApResidence);

    return Party
        .newBuilder()
        .setId(AlertParserUtils.makeAlertedPartyId(recordId, ""))
        .setSource(Source.SOURCE_CONFIDENTIAL)
        .setDetails(AnyUtils.pack(builder.build()))
        .build();
  }

  private static String getGenderFromName(ScreenableData data) {
    var names =
        AlertParserUtils.expand(
            singletonList(data.getFullLegalName()), GnsRtFieldsMapper.getAlternateNames(data));
    return GenderDetector.determineApGenderFromName(data.getClientType(), names);
  }
}
