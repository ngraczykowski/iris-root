package com.silenteight.scb.ingest.adapter.incomming.common.model.match;

import lombok.Builder;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;

import java.util.List;
import java.util.Set;

public record MatchedParty(
    ObjectId id,
    String apType,
    String wlId,
    String wlName,
    List<String> wlNameSynonyms,
    List<String> wlHitNames,
    String wlType,
    String wlDob,
    String wlNationality,
    String wlResidence,
    String wlNationalId,
    String wlPassport,
    List<String> wlSearchCodes,
    List<String> wlNationalIds,
    List<String> wlBicCodes,
    Set<String> wlHitType,
    String wlGender,
    String wlGenderFromName,
    String wlCountry,
    String wlDesignation,
    List<WatchlistName> wlNames,
    String wlTitle,
    String wlOtherInformation,
    List<WatchlistName> wlOriginalCnNames) {

  @Builder
  public MatchedParty {}

  public record WatchlistName(
      String name,
      String type) {
  }
}
