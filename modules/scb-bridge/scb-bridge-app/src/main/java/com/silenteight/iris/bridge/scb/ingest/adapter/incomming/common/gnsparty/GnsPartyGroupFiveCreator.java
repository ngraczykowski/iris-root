/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

class GnsPartyGroupFiveCreator extends GnsPartyGroupTwoCreator {

  private static final List<String> ADDITIONAL_FIELDS = asList(
      "alternateId1Description",
      "alternateId1",
      "alternateId2Description",
      "alternateId2",
      "supplementaryInformation1",
      "supplementaryInformation2",
      "supplementaryInformation3",
      "supplementaryInformation4",
      "supplementaryInformation5",
      "supplementaryInformation6",
      "supplementaryInformation7",
      "supplementaryInformation8",
      "supplementaryInformation9",
      "supplementaryInformation10");

  GnsPartyGroupFiveCreator() {
    super(ADDITIONAL_FIELDS);
  }

  @Override
  protected GnsParty createAdditionalFields(GnsParty result) {
    result = super.createAdditionalFields(result);

    result.createList(
        "supplementaryInformations",
        asList(
            "supplementaryInformation1",
            "supplementaryInformation2",
            "supplementaryInformation3",
            "supplementaryInformation4",
            "supplementaryInformation5",
            "supplementaryInformation6",
            "supplementaryInformation7",
            "supplementaryInformation8",
            "supplementaryInformation9",
            "supplementaryInformation10"));

    return result;
  }

  @Override
  protected List<String> getIdentificationList() {
    List<String> identifications = new ArrayList<>(super.getIdentificationList());
    identifications.addAll(asList("alternateId1", "alternateId2"));
    return identifications;
  }
}
