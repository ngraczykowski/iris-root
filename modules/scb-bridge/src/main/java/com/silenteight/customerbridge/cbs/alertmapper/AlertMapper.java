package com.silenteight.customerbridge.cbs.alertmapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.cbs.alertrecord.AlertRecordComposite;
import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.alertrecord.DecisionRecord;
import com.silenteight.customerbridge.common.batch.DateConverter;
import com.silenteight.customerbridge.common.gnsparty.GnsParty;
import com.silenteight.customerbridge.common.gnsparty.RecordParser;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;
import com.silenteight.customerbridge.common.protocol.MatchWrapper;
import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Alert.Builder;
import com.silenteight.proto.serp.v1.alert.Alert.Flags;
import com.silenteight.proto.serp.v1.alert.Alert.State;
import com.silenteight.proto.serp.v1.alert.Decision;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.alert.Party;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.customerbridge.cbs.alertmapper.AlertMapper.Option.*;
import static com.silenteight.customerbridge.common.util.AlertParserUtils.SPLIT_ID_CHARACTER;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestampOrDefault;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.function.Predicate.not;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
public class AlertMapper {

  private final DateConverter dateConverter;
  private final MatchCollector matchCollector;
  private final SuspectsCollector suspectsCollector;

  private static final String NO_NEW_MATCHES_WARNING = "No new matches found in alert: {}";

  public List<Alert> fromAlertRecordComposite(
      @NonNull AlertRecordComposite alertRecordComposite, Option... options) {
    return contains(options, WATCHLIST_LEVEL) ?
           mapAsWatchlistLevel(alertRecordComposite, options) :
           mapAsAlertLevel(alertRecordComposite, options);
  }

  private List<Alert> mapAsWatchlistLevel(
      AlertRecordComposite alertRecordComposite, Option[] options) {
    var suspects = getSuspects(alertRecordComposite);

    if (contains(options, ONLY_UNSOLVED) && atLeastOneSuspectHasNeoFlag(suspects))
      suspects.removeIf(not(Suspect::hasNeoFlag));

    var alerts = suspects.stream()
        .map(s -> doMap(singletonList(s), alertRecordComposite, options))
        .filter(shouldAlertBeProcessed(options))
        .collect(Collectors.toList());

    if (alerts.isEmpty())
      log.warn(NO_NEW_MATCHES_WARNING, alertRecordComposite.getSystemId());

    return alerts;
  }

  private List<Alert> mapAsAlertLevel(AlertRecordComposite alertRecordComposite, Option[] options) {
    var suspects = getSuspects(alertRecordComposite);
    var alert = doMap(suspects, alertRecordComposite, options);

    if (contains(options, ONLY_UNSOLVED) && hasOnlySolvedMatches(alert.getMatchesList())) {
      log.warn(NO_NEW_MATCHES_WARNING, alert.getId().getSourceId());
      return emptyList();
    }
    return singletonList(alert);
  }

  private Alert doMap(
      Collection<Suspect> suspects,
      AlertRecordComposite alertRecordComposite,
      Option... options) {

    AlertRecord alertRecord = alertRecordComposite.getAlert();
    Optional<Decision> lastDecision = alertRecordComposite.getLastDecision();
    Instant filtered = dateConverter.convert(alertRecord.getFilteredString()).orElse(null);
    GnsParty gnsParty = makeGnsParty(alertRecord);
    Instant discriminator = getDiscriminator(alertRecordComposite, filtered);
    AlertContext alertContext =
        buildAlertContext(alertRecord, gnsParty, lastDecision.isPresent());
    String watchlistId = contains(options, WATCHLIST_LEVEL) ? getWatchlistId(suspects) : "";
    List<Match> matches = collectMatches(suspects, alertContext);

    Builder builder = initializeBuilder()
        .addAllMatches(matches)
        .setAlertedParty(makeAlertedParty(alertRecord, gnsParty))
        .setDecisionGroup(nullToEmpty(alertRecord.getUnit()))
        .setDetails(createDetails(alertRecord, watchlistId))
        .setFlags(getFlags(options))
        .setGeneratedAt(toTimestampOrDefault(filtered, Timestamp.getDefaultInstance()))
        .setId(makeId(alertRecord.getSystemId(), watchlistId, discriminator))
        .setSecurityGroup(alertRecord.getSystemId().substring(0, 2));

    if (contains(options, FOR_LEARNING)) {
      addAllDecisions(alertRecordComposite.getDecisions(), builder);
    } else
      lastDecision.ifPresent(builder::addDecisions);

    if (suspects.isEmpty() || gnsParty.isEmpty()) {
      log.debug("No suspects found or invalid record in alert: {}", alertRecord.getSystemId());
      markAlertAsDamaged(builder);
    }

    return builder.build();
  }

  private void addAllDecisions(List<DecisionRecord> decisionRecords, Builder builder) {
    builder.addAllDecisions(decisionRecords.stream()
        .map(DecisionRecord::toDecision)
        .collect(Collectors.toList()));
  }

  private static boolean atLeastOneSuspectHasNeoFlag(Collection<Suspect> suspects) {
    return suspects.stream().anyMatch(Suspect::hasNeoFlag);
  }

  private static Predicate<Alert> shouldAlertBeProcessed(Option[] options) {
    return a -> !contains(options, ONLY_UNSOLVED) || hasAnyNewMatches(a);
  }

  private static boolean hasAnyNewMatches(Alert alert) {
    return !hasOnlySolvedMatches(alert.getMatchesList());
  }

  private static boolean hasOnlySolvedMatches(List<Match> matches) {
    return matches.stream()
        .map(MatchWrapper::new)
        .noneMatch(MatchWrapper::isNew);
  }

  private static void markAlertAsDamaged(Builder builder) {
    builder.setState(State.STATE_DAMAGED);
    builder.setFlags(Flags.FLAG_NONE_VALUE);
  }

  private static Builder initializeBuilder() {
    return Alert.newBuilder()
        .setReceivedAt(toTimestamp(Instant.now()))
        .setState(State.STATE_CORRECT);
  }

  private static int getFlags(Option... options) {
    int flags = Flags.FLAG_NONE_VALUE;

    if (contains(options, FOR_RECOMMENDATION))
      flags |= Flags.FLAG_RECOMMEND_VALUE | Flags.FLAG_PROCESS_VALUE;

    if (contains(options, FOR_LEARNING))
      flags |= Flags.FLAG_LEARN_VALUE;

    if (contains(options, ATTACH_ALERT))
      flags |= Flags.FLAG_ATTACH_VALUE;

    return flags;
  }

  private static AlertContext buildAlertContext(
      AlertRecord alertRecord, GnsParty gnsParty, boolean hasLastDecision) {
    Set<String> alternateNames = gnsParty.getAlternateNames();

    var builder = AlertContext.builder();

    gnsParty.getName().ifPresent(builder::partyName);

    return builder
        .lastDecisionPresent(hasLastDecision)
        .lastDecBatchId(alertRecord.getLastDecBatchId())
        .typeOfRec(alertRecord.getTypeOfRec())
        .partyAlternateNames(new ArrayList<>(alternateNames))
        .build();
  }

  private static String getWatchlistId(Collection<Suspect> suspects) {
    return suspects.iterator().next().getOfacId();
  }

  private static Party makeAlertedParty(AlertRecord alertRecord, GnsParty gnsParty) {
    return new AlertedPartyCreator().makeAlertedParty(alertRecord, gnsParty);
  }

  @Nullable
  private static Instant getDiscriminator(
      AlertRecordComposite composite, @Nullable Instant filtered) {

    return composite.getLastResetDecisionDate().orElse(filtered);
  }

  @Nonnull
  private Collection<Suspect> getSuspects(AlertRecordComposite composite) {
    return suspectsCollector.collect(composite.getHitsDetails(), composite.getCbsHitDetails());
  }

  private static Any createDetails(AlertRecord alertRecord, String watchlistId) {
    ScbAlertDetails.Builder detailsBuilder = ScbAlertDetails
        .newBuilder()
        .setBatchId(nullToEmpty(alertRecord.getBatchId()))
        .setUnit(nullToEmpty(alertRecord.getUnit()))
        .setAccount(nullToEmpty(alertRecord.getDbAccount()))
        .setSystemId(alertRecord.getSystemId())
        .setWatchlistId(watchlistId);

    return AnyUtils.pack(detailsBuilder.build());
  }

  private List<Match> collectMatches(Collection<Suspect> suspects, AlertContext alertContext) {
    return matchCollector.collectMatches(suspects, alertContext);
  }

  private static ObjectId makeId(
      @NonNull String systemId,
      @Nullable String watchlistId,
      @Nullable Instant lastResetDecisionDate) {
    return ObjectId
        .newBuilder()
        .setId(Uuids.random())
        .setSourceId(makeSourceId(systemId, watchlistId))
        .setDiscriminator(String.valueOf(lastResetDecisionDate))
        .build();
  }

  private static String makeSourceId(@NonNull String systemId, @Nullable String watchlistId) {
    return isNotEmpty(watchlistId) ?
           String.join(SPLIT_ID_CHARACTER, systemId, watchlistId) :
           systemId;
  }

  private static GnsParty makeGnsParty(AlertRecord alertRow) {
    return RecordParser.parse(
        alertRow.getSystemId(),
        alertRow.getCharSep(),
        alertRow.getFmtName(),
        alertRow.getRecord());
  }

  public enum Option {
    WATCHLIST_LEVEL,
    ATTACH_ALERT,
    FOR_LEARNING,
    FOR_RECOMMENDATION,
    ONLY_UNSOLVED,
  }
}
