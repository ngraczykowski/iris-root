package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.NonNull;

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.SupplementaryInformationHelper;
import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty.AlertedPartyBuilder;
import com.silenteight.scb.ingest.adapter.incomming.common.validation.ChineseCharactersValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.scb.ingest.adapter.incomming.common.indicator.RecordSignCreator.fromRecord;
import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.makeAlertedPartyId;
import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.mapString;

class AlertedPartyCreator {

  AlertedParty makeAlertedParty(
      @NonNull AlertRecord alertRecord,
      @NonNull GnsParty party) {
    List<String> allNames = new ArrayList<>();
    party.getName().ifPresent(allNames::add);
    allNames.addAll(party.getAlternateNames());

    String genderFromName =
        GenderDetector.determineApGenderFromName(alertRecord.getTypeOfRec(), allNames);

    AlertedPartyBuilder builder = AlertedParty.builder()
        .id(makePartyId(alertRecord))
        .apId(alertRecord.getSystemId())
        .apGenderFromName(genderFromName)
        .apDocNationalIds(party.getNationalIds())
        .apNameSynonyms(party.getAlternateNames())
        .apOriginalCnNames(getChineseNames(party));

    party.getName().ifPresent(builder::apName);
    mapString(party.getSourceSystemIdentifier(), builder::apSrcSysId);
    party
        .mapString("nationalityAll", builder::apNationality)
        .mapString("registOrResidenAddCntry", builder::apResidence)
        .mapString("customerStatus", builder::apCustStatus)
        .mapString("gender", builder::apGender)
        .mapString("bookingLocation", builder::apBookingLocation)
        .mapCollection("nationalities", builder::apNationalitySynonyms)
        .mapCollection("residencies", builder::apResidenceSynonyms)
        .mapCollection("residentialAddresses", builder::apResidentialAddresses)
        .mapCollection("passportNumbers", builder::apDocPassports)
        .mapCollection("identifications", builder::apDocOthers)
        .mapString("clientType", builder::custType)
        .mapString("dateOfBirthOrRegis", builder::apDobDoi)
        .mapString("bookingLocation", builder::apDbCountry);

    return builder.build();
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
