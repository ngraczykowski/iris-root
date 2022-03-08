package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.NonNull;

import com.silenteight.proto.serp.scb.v1.ScbAlertedPartyDetails;
import com.silenteight.proto.serp.v1.alert.Party;
import com.silenteight.proto.serp.v1.alert.Party.Source;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.SupplementaryInformationHelper;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils;
import com.silenteight.scb.ingest.adapter.incomming.common.validation.ChineseCharactersValidator;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.scb.ingest.adapter.incomming.common.indicator.RecordSignCreator.fromRecord;
import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.makeAlertedPartyId;

class AlertedPartyCreator {

  Party makeAlertedParty(
      @NonNull AlertRecord alertRecord,
      @NonNull GnsParty party) {

    List<String> allNames = new ArrayList<>();
    party.getName().ifPresent(allNames::add);
    allNames.addAll(party.getAlternateNames());

    String genderFromName =
        GenderDetector.determineApGenderFromName(alertRecord.getTypeOfRec(), allNames);

    ScbAlertedPartyDetails.Builder builder = ScbAlertedPartyDetails
        .newBuilder()
        .setApId(alertRecord.getSystemId())
        .setApGenderFromName(genderFromName)
        .addAllApDocNationalIds(party.getNationalIds())
        .addAllApNameSynonyms(party.getAlternateNames())
        .addAllApOriginalCnNames(getChineseNames(party));

    party.getName().ifPresent(builder::setApName);

    AlertParserUtils.mapString(party.getSourceSystemIdentifier(), builder::setApSrcSysId);

    party
        .mapString("nationalityAll", builder::setApNationality)
        .mapString("registOrResidenAddCntry", builder::setApResidence)
        .mapString("customerStatus", builder::setApCustStatus)
        .mapString("gender", builder::setApGender)
        .mapString("bookingLocation", builder::setApBookingLocation)
        .mapCollection("nationalities", builder::addAllApNationalitySynonyms)
        .mapCollection("residencies", builder::addAllApResidenceSynonyms)
        .mapCollection("residentialAddresses", builder::addAllApResidentialAddresses)
        .mapCollection("passportNumbers", builder::addAllApDocPassports)
        .mapCollection("identifications", builder::addAllApDocOthers)
        .mapString("clientType", builder::setCustType)
        .mapString("dateOfBirthOrRegis", builder::setApDobDoi)
        .mapString("bookingLocation", builder::setApDbCountry);

    return Party
        .newBuilder()
        .setId(makePartyId(alertRecord))
        .setSource(Source.SOURCE_CONFIDENTIAL)
        .setDetails(AnyUtils.pack(builder.build()))
        .build();
  }

  private static ObjectId makePartyId(AlertRecord alertRecord) {
    String recordSignature = fromRecord(alertRecord.getRecord(), alertRecord.getCharSep());
    return makeAlertedPartyId(nullToEmpty(alertRecord.getRecordId()), recordSignature);
  }

  private Set<String> getChineseNames(GnsParty party) {
    var chineseNames = new HashSet<String>();

    new SupplementaryInformationHelper(party).getChineseNameFromSupplementaryInformation1()
        .ifPresent(chineseNames::add);

    party.getName().filter(ChineseCharactersValidator::isValid)
        .ifPresent(chineseNames::add);

    party.getAlternateNames().stream()
        .filter(ChineseCharactersValidator::isValid)
        .forEach(chineseNames::add);

    return chineseNames;
  }
}
