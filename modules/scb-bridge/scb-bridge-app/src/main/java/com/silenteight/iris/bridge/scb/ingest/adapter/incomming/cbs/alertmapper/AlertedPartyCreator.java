/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.NonNull;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.ChineseNamesResolver;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty.GnsParty;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.indicator.RecordSignCreator;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.AlertParserUtils;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertedParty.AlertedPartyBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Strings.nullToEmpty;

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
    AlertParserUtils.mapString(party.getSourceSystemIdentifier(), builder::apSrcSysId);
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
    String recordSignature = RecordSignCreator.fromRecord(alertRecord.getRecord(), alertRecord.getCharSep());
    return AlertParserUtils.makeAlertedPartyId(nullToEmpty(alertRecord.getRecordId()), recordSignature);
  }

  private Set<String> getChineseNames(GnsParty party) {
    return ChineseNamesResolver.getChineseNames(party);
  }
}
