package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.scb.v1.ScbMatchDetails;
import com.silenteight.proto.serp.scb.v1.ScbWatchlistPartyDetails;
import com.silenteight.proto.serp.scb.v1.WatchlistName;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Alert.Flags;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.alert.Party;
import com.silenteight.proto.serp.v1.alert.Party.Source;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.scb.ingest.adapter.incomming.common.WlName;
import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import java.util.Collection;
import java.util.List;
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
      Party party = makeWatchlistParty(s, screenableData.getClientType());
      ScbMatchDetails details = s.makeMatchDetails(
          s.getMergedMatchingTexts(),
          screenableData.getFullLegalName(),
          GnsRtFieldsMapper.getAlternateNames(screenableData));

      return Match
          .newBuilder()
          .setId(party.getId().toBuilder().setId(Uuids.random()))
          .setMatchedParty(party)
          .setFlags(Flags.FLAG_NONE_VALUE)
          .setIndex(counter.getAndIncrement())
          .setDetails(AnyUtils.pack(details))
          .build();
    }).collect(toList());
  }

  private Party makeWatchlistParty(Suspect suspect, String typeOfRec) {
    List<String> wlNationalIds = AlertParserUtils.expand(
        singletonList(suspect.getNationalId()), suspect.getSearchCodes().asList());
    List<String> names = AlertParserUtils.expand(
        singletonList(suspect.getName()), suspect.getNameSynonyms().asListOfNames());

    ScbWatchlistPartyDetails.Builder detailsBuilder = ScbWatchlistPartyDetails
        .newBuilder()
        .setWlGenderFromName(genderDetector.determineWlGenderFromName(typeOfRec, names))
        .addAllWlNameSynonyms(suspect.getNameSynonyms().asListOfNames())
        .addAllWlHitNames(suspect.getActiveNames().stream().map(WlName::getName).collect(toList()))
        .addAllWlNames(mapWatchlistNames(suspect.getActiveNames()))
        .addAllWlOriginalCnNames(mapWatchlistNames(suspect.getOriginalChineseNames()))
        .addAllWlSearchCodes(suspect.getSearchCodes().asList())
        .addAllWlNationalIds(wlNationalIds)
        .addAllWlBicCodes(suspect.getBicCodes().asList())
        .addAllWlHitType(suspect.getTags());

    mapString(
        AlertParserUtils.determineApType(typeOfRec, suspect.getType()), detailsBuilder::setApType);
    mapString(suspect.getNotes().getOrDefault("gender", EMPTY), detailsBuilder::setWlGender);
    mapString(suspect.getOfacId(), detailsBuilder::setWlId);
    mapString(suspect.getName(), detailsBuilder::setWlName);
    mapString(suspect.getType(), detailsBuilder::setWlType);
    mapString(suspect.getBirthDate(), detailsBuilder::setWlDob);
    mapString(
        suspect.getNotes().getOrDefault("nationality", EMPTY), detailsBuilder::setWlNationality);
    mapString(suspect.getCountry(), detailsBuilder::setWlResidence);
    mapString(suspect.getNationalId(), detailsBuilder::setWlNationalId);
    mapString(suspect.getPassport(), detailsBuilder::setWlPassport);
    mapString(suspect.getCountry(), detailsBuilder::setWlCountry);
    mapString(suspect.getDesignation(), detailsBuilder::setWlDesignation);
    mapString(suspect.getNotes().getOrDefault("title", EMPTY), detailsBuilder::setWlTitle);

    return Party
        .newBuilder()
        .setId(AlertParserUtils.makeWatchlistPartyId(suspect.getOfacId(), suspect.getBatchId()))
        .setSource(Source.SOURCE_CONFIDENTIAL)
        .setDetails(AnyUtils.pack(detailsBuilder.build()))
        .build();
  }

  private static List<WatchlistName> mapWatchlistNames(List<WlName> watchlistNames) {
    return watchlistNames
        .stream()
        .map(name -> WatchlistName.newBuilder()
            .setName(name.getName())
            .setType(name.getType().name())
            .build())
        .collect(Collectors.toList());
  }
}
