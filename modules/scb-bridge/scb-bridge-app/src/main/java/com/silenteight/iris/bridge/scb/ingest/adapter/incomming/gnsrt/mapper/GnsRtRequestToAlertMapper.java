/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.WlName;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.MatchDetails;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.AlertParserUtils;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@RequiredArgsConstructor
public class GnsRtRequestToAlertMapper {

  private final HitDetailsParser hitDetailsParser;
  private final GenderDetector genderDetector;
  private final GnsRtRequestToAlertMapperHelper gnsRtRequestToAlertMapperHelper =
      new GnsRtRequestToAlertMapperHelper();

  public List<Alert> map(GnsRtRecommendationRequest request, String internalBatchId) {
    GnsRtScreenCustomerNameResInfo screenCustomerNameResInfo = request
        .getScreenCustomerNameRes()
        .getScreenCustomerNameResPayload()
        .getScreenCustomerNameResInfo();

    ImmediateResponseData immediateResponseData = screenCustomerNameResInfo
        .getImmediateResponseData();

    ScreenableData screenableData = screenCustomerNameResInfo.getScreenableData();

    return immediateResponseData
        .getAlerts()
        .parallelStream()
        .filter(g -> g.getAlertStatus() == GnsRtAlertStatus.POTENTIAL_MATCH)
        .map(alert -> createAlert(request, alert, internalBatchId, screenableData))
        .toList();
  }

  @Nonnull
  private Alert createAlert(
      GnsRtRecommendationRequest request,
      GnsRtAlert alert,
      String internalBatchId,
      ScreenableData screenableData) {
    Collection<Suspect> suspects = extractSuspects(alert);
    List<Match> matches = makeMatches(suspects, screenableData);

    return gnsRtRequestToAlertMapperHelper.createAlert(request, alert, internalBatchId, matches);
  }

  @Nonnull
  private Collection<Suspect> extractSuspects(GnsRtAlert alert) {
    return hitDetailsParser.parseHitDetailsFromGnsRt(alert.getHitList()).extractUniqueSuspects();
  }

  private List<Match> makeMatches(Collection<Suspect> suspects, ScreenableData screenableData) {
    AtomicInteger counter = new AtomicInteger(0);
    return suspects.stream().map(s -> {
      MatchedParty party = makeWatchlistParty(s, screenableData.getClientType());
      MatchDetails details = s.makeMatchDetails(
          s.getMergedMatchingTexts(),
          screenableData.getFullLegalName(),
          GnsRtFieldsMapper.getAlternateNames(screenableData));

      return Match
          .builder()
          .id(party.id().toBuilder().id(UUID.randomUUID()).build())
          .matchedParty(party)
          .index(counter.getAndIncrement())
          .details(details)
          .build();
    }).toList();
  }

  private MatchedParty makeWatchlistParty(Suspect suspect, String typeOfRec) {
    List<String> wlNationalIds = AlertParserUtils.expand(
        singletonList(suspect.getNationalId()), suspect.getSearchCodes().asList());
    List<String> names = AlertParserUtils.expand(
        singletonList(suspect.getName()), suspect.getNameSynonyms().asListOfNames());

    MatchedParty.MatchedPartyBuilder builder = MatchedParty.builder()
        .id(AlertParserUtils.makeWatchlistPartyId(suspect.getOfacId(), suspect.getBatchId()))
        .wlGenderFromName(genderDetector.determineWlGenderFromName(typeOfRec, names))
        .wlNameSynonyms(suspect.getNameSynonyms().asListOfNames())
        .wlHitNames(suspect.getActiveNames().stream().map(WlName::getName).toList())
        .wlNames(mapWatchlistNames(suspect.getActiveNames()))
        .wlOriginalCnNames(mapWatchlistNames(suspect.getOriginalChineseNames()))
        .wlSearchCodes(suspect.getSearchCodes().asList())
        .wlNationalIds(wlNationalIds)
        .wlBicCodes(suspect.getBicCodes().asList())
        .wlHitType(suspect.getTags());

    AlertParserUtils.mapString(
        AlertParserUtils.determineApType(typeOfRec, suspect.getType()), builder::apType);
    AlertParserUtils.mapString(suspect.getNotes().getOrDefault("gender", EMPTY), builder::wlGender);
    AlertParserUtils.mapString(suspect.getOfacId(), builder::wlId);
    AlertParserUtils.mapString(suspect.getName(), builder::wlName);
    AlertParserUtils.mapString(suspect.getType(), builder::wlType);
    AlertParserUtils.mapString(suspect.getBirthDate(), builder::wlDob);
    AlertParserUtils.mapString(
        suspect.getNotes().getOrDefault("nationality", EMPTY), builder::wlNationality);
    AlertParserUtils.mapString(suspect.getCountry(), builder::wlResidence);
    AlertParserUtils.mapString(suspect.getNationalId(), builder::wlNationalId);
    AlertParserUtils.mapString(suspect.getPassport(), builder::wlPassport);
    AlertParserUtils.mapString(suspect.getCountry(), builder::wlCountry);
    AlertParserUtils.mapString(suspect.getDesignation(), builder::wlDesignation);
    AlertParserUtils.mapString(suspect.getNotes().getOrDefault("title", EMPTY), builder::wlTitle);

    return builder.build();
  }

  private static List<MatchedParty.WatchlistName> mapWatchlistNames(List<WlName> watchlistNames) {
    return watchlistNames
        .stream()
        .map(name -> new MatchedParty.WatchlistName(name.getName(), name.getType().name()))
        .toList();
  }
}
