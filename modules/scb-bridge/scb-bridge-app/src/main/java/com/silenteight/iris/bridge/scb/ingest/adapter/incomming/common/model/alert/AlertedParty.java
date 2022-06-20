/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert;

import lombok.Builder;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public record AlertedParty(
    ObjectId id,
    String apId,
    String apName,
    Collection<String> apNameSynonyms,
    String custType,
    String apDobDoi,
    String apSrcSysId,
    String apDbCountry,
    String apNationality,
    Collection<String> apNationalitySynonyms,
    String apResidence,
    Collection<String> apResidenceSynonyms,
    String apCustStatus,
    List<String> apDocNationalIds,
    Collection<String> apDocPassports,
    Collection<String> apDocOthers,
    String apGender,
    String apGenderFromName,
    String apBookingLocation,
    Collection<String> apResidentialAddresses,
    Set<String> apOriginalCnNames) {

  @Builder
  public AlertedParty {}
}
