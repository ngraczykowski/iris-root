/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty;

import com.silenteight.commons.collections.MapBuilder;

import java.util.List;
import java.util.Map;

class GnsPartyGroupFourCreator extends AbstractGnsPartyCreator {

  private static final Map<String, String> FIELD_MAP = MapBuilder.from(
      "isinCode", "customerIdentificationNo",
      "assetType", "clientType",
      "address", "registOrResidentialAddress",
      "instrumentCountryOfIssue", "registOrResidenAddCntry",
      "dateOfIncorporation", "dateOfBirthOrRegis",
      "dateOfLastTransaction", "dateTimeStamp");

  private static final List<String> FIELDS = List.of(
      "sourceSystemIdentifier",
      "isinCode",
      "cinsCusipCode",
      "sedolCode",
      "applicationName",
      "uniqueId",
      "assetType",
      "assetSubtype",
      "fullInstrumentName",
      "fullNameOfIssuer",
      "instrumentCountryOfIssue",
      "issuerCountryOfIncorp",
      "issuerCountryOfOperations",
      "address",
      "dateOfIncorporation",
      "dateOfLastTransaction",
      "issuerShareholders",
      "shareholderCountryOfIncorp",
      "shareholderCountryOfOper",
      "directorsExecutives",
      "depositoryBank",
      "fundManagersOrDistributors",
      "additionalInformation1",
      "additionalInformation2",
      "additionalInformation3",
      "gnsUploadDate");

  GnsPartyGroupFourCreator() {
    super(FIELDS, FIELD_MAP);
  }

  @Override
  protected GnsParty createAdditionalFields(GnsParty result) {
    result.createList(
        "nationalities",
        List.of(
            "registOrResidenAddCntry",
            "issuerCountryOfIncorp",
            "issuerCountryOfOperations",
            "registOrResidentialAddress",
            "shareholderCountryOfIncorp",
            "shareholderCountryOfOper"));

    result.createList(
        GnsPartyCommonFields.ALTERNATE_NAMES,
        List.of(
            "sedolCode",
            "fullInstrumentName",
            "fullNameOfIssuer",
            "issuerShareholders",
            "directorsExecutives",
            "depositoryBank",
            "fundManagersOrDistributors"));

    result.createList(
        "residencies",
        List.of(
            "registOrResidenAddCntry",
            "issuerCountryOfIncorp",
            "issuerCountryOfOperations",
            "shareholderCountryOfIncorp",
            "shareholderCountryOfOper"));

    result.createList("residentialAddresses", List.of("registOrResidentialAddress"));

    result.createList(
        "identifications",
        List.of(
            "sedolCode",
            "depositoryBank"));

    return result;
  }
}
