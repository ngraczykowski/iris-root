package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.WlName;
import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils.mapString;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@RequiredArgsConstructor
public class GnsRtRequestToAlertMapper {

  private final HitDetailsParser hitDetailsParser;
  private final GenderDetector genderDetector;
  private GnsRtRequestToAlertMapperHelper gnsRtRequestToAlertMapperHelper =
      new GnsRtRequestToAlertMapperHelper();

  public List<Alert> map(GnsRtRecommendationRequest request) {
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
        .map(alert -> createAlert(request, alert, screenableData))
        .collect(toList());
  }

  @Nonnull
  private Alert createAlert(
      GnsRtRecommendationRequest request, GnsRtAlert alert, ScreenableData screenableData) {
    Collection<Suspect> suspects = extractSuspects(alert);
    List<Match> matches = makeMatches(suspects, screenableData);

    return gnsRtRequestToAlertMapperHelper.createAlert(request, alert, matches);
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
    }).collect(toList());
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
        .wlHitNames(suspect.getActiveNames().stream().map(WlName::getName).collect(toList()))
        .wlNames(mapWatchlistNames(suspect.getActiveNames()))
        .wlOriginalCnNames(mapWatchlistNames(suspect.getOriginalChineseNames()))
        .wlSearchCodes(suspect.getSearchCodes().asList())
        .wlNationalIds(wlNationalIds)
        .wlBicCodes(suspect.getBicCodes().asList())
        .wlHitType(suspect.getTags());

    mapString(
        AlertParserUtils.determineApType(typeOfRec, suspect.getType()), builder::apType);
    mapString(suspect.getNotes().getOrDefault("gender", EMPTY), builder::wlGender);
    mapString(suspect.getOfacId(), builder::wlId);
    mapString(suspect.getName(), builder::wlName);
    mapString(suspect.getType(), builder::wlType);
    mapString(suspect.getBirthDate(), builder::wlDob);
    mapString(
        suspect.getNotes().getOrDefault("nationality", EMPTY), builder::wlNationality);
    mapString(suspect.getCountry(), builder::wlResidence);
    mapString(suspect.getNationalId(), builder::wlNationalId);
    mapString(suspect.getPassport(), builder::wlPassport);
    mapString(suspect.getCountry(), builder::wlCountry);
    mapString(suspect.getDesignation(), builder::wlDesignation);
    mapString(suspect.getNotes().getOrDefault("title", EMPTY), builder::wlTitle);

    return builder.build();
  }

  private static List<MatchedParty.WatchlistName> mapWatchlistNames(List<WlName> watchlistNames) {
    return watchlistNames
        .stream()
        .map(name -> new MatchedParty.WatchlistName(name.getName(), name.getType().name()))
        .collect(Collectors.toList());
  }
}
