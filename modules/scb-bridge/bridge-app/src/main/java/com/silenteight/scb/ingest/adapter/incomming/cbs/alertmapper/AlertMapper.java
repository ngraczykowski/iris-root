package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.alert.Alert.Flags;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.AlertRecordComposite;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.DateConverter;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.RecordParser;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.AlertBuilder;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.State;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper.Option.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.function.Predicate.not;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
public class AlertMapper {

  private static final String NO_NEW_MATCHES_WARNING = "No new matches found in alert: {}";
  private final DateConverter dateConverter;
  private final MatchCollector matchCollector;
  private final SuspectsCollector suspectsCollector;

  private static boolean atLeastOneSuspectHasNeoFlag(Collection<Suspect> suspects) {
    return suspects.stream().anyMatch(Suspect::hasNeoFlag);
  }

  private static Predicate<Alert> shouldAlertBeProcessed(Option[] options) {
    return a -> !ArrayUtils.contains(options, ONLY_UNSOLVED) || hasAnyNewMatches(a);
  }

  private static boolean hasAnyNewMatches(Alert alert) {
    return !hasOnlySolvedMatches(alert.matches());
  }

  private static boolean hasOnlySolvedMatches(List<Match> matches) {
    return matches.stream()
        .noneMatch(Match::isNew);
  }

  private static void markAlertAsDamaged(Alert.AlertBuilder builder) {
    builder.state(State.STATE_DAMAGED);
    builder.flags(Flags.FLAG_NONE_VALUE);
  }

  private static AlertBuilder initializeBuilder() {
    return Alert.builder()
        .receivedAt(Instant.now())
        .state(Alert.State.STATE_CORRECT);
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

  private static AlertedParty makeAlertedParty(AlertRecord alertRecord, GnsParty gnsParty) {
    return new AlertedPartyCreator().makeAlertedParty(alertRecord, gnsParty);
  }

  @Nullable
  private static Instant getDiscriminator(
      AlertRecordComposite composite, @Nullable Instant filtered) {

    return composite.getLastResetDecisionDate().orElse(filtered);
  }

  private static AlertDetails createDetails(AlertRecord alertRecord, String watchlistId) {
    return AlertDetails
        .builder()
        .batchId(nullToEmpty(alertRecord.getBatchId()))
        .unit(nullToEmpty(alertRecord.getUnit()))
        .account(nullToEmpty(alertRecord.getDbAccount()))
        .systemId(alertRecord.getSystemId())
        .watchlistId(watchlistId)
        .build();
  }

  private static ObjectId makeId(
      @NonNull String systemId,
      @Nullable String watchlistId,
      @Nullable Instant lastResetDecisionDate) {
    return ObjectId
        .builder()
        .id(UUID.randomUUID())
        .sourceId(makeSourceId(systemId, watchlistId))
        .discriminator(String.valueOf(lastResetDecisionDate))
        .build();
  }

  private static String makeSourceId(@NonNull String systemId, @Nullable String watchlistId) {
    return isNotEmpty(watchlistId) ?
           String.join(AlertParserUtils.SPLIT_ID_CHARACTER, systemId, watchlistId) :
           systemId;
  }

  private static GnsParty makeGnsParty(AlertRecord alertRow) {
    return RecordParser.parse(
        alertRow.getSystemId(),
        alertRow.getCharSep(),
        alertRow.getFmtName(),
        alertRow.getRecord());
  }

  private static int getFlags(Option... options) {
    int flags = Flag.NONE.getValue();

    if (ArrayUtils.contains(options, FOR_RECOMMENDATION))
      flags |= Flag.RECOMMEND.getValue() | Flag.PROCESS.getValue();

    if (ArrayUtils.contains(options, FOR_LEARNING))
      flags |= Flag.LEARN.getValue();

    if (ArrayUtils.contains(options, ATTACH_ALERT))
      flags |= Flag.ATTACH.getValue();

    return flags;
  }

  public List<Alert> fromAlertRecordComposite(
      @NonNull AlertRecordComposite alertRecordComposite, Option... options) {
    return ArrayUtils.contains(options, WATCHLIST_LEVEL) ?
           mapAsWatchlistLevel(alertRecordComposite, options) :
           mapAsAlertLevel(alertRecordComposite, options);
  }

  private List<Alert> mapAsWatchlistLevel(
      AlertRecordComposite alertRecordComposite, Option[] options) {
    var suspects = getSuspects(alertRecordComposite);

    if (ArrayUtils.contains(options, ONLY_UNSOLVED) && atLeastOneSuspectHasNeoFlag(suspects))
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

    if (ArrayUtils.contains(options, ONLY_UNSOLVED) && hasOnlySolvedMatches(alert.matches())) {
      log.warn(NO_NEW_MATCHES_WARNING, alert.id().sourceId());
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
    String watchlistId =
        ArrayUtils.contains(options, WATCHLIST_LEVEL) ? getWatchlistId(suspects) : "";
    List<Match> matches = collectMatches(suspects, alertContext);

    Alert.AlertBuilder builder = initializeBuilder()
        .matches(matches)
        .alertedParty(makeAlertedParty(alertRecord, gnsParty))
        .decisionGroup(nullToEmpty(alertRecord.getUnit()))
        .details(createDetails(alertRecord, watchlistId))
        .flags(getFlags(options))
        .generatedAt(filtered)
        .id(makeId(alertRecord.getSystemId(), watchlistId, discriminator))
        .securityGroup(alertRecord.getSystemId().substring(0, 2));

    if (ArrayUtils.contains(options, FOR_LEARNING)) {
      addAllDecisions(alertRecordComposite.getDecisions(), builder);
    } else
      lastDecision.ifPresent(decision -> builder.decisions(Collections.singletonList(decision)));

    if (suspects.isEmpty() || gnsParty.isEmpty()) {
      log.debug("No suspects found or invalid record in alert: {}", alertRecord.getSystemId());
      markAlertAsDamaged(builder);
    }

    return builder.build();
  }

  private void addAllDecisions(List<DecisionRecord> decisionRecords, Alert.AlertBuilder builder) {
    builder.decisions(decisionRecords.stream()
        .map(DecisionRecord::toDecision)
        .collect(Collectors.toList()));
  }

  @Nonnull
  private Collection<Suspect> getSuspects(AlertRecordComposite composite) {
    return suspectsCollector.collect(composite.getHitsDetails(), composite.getCbsHitDetails());
  }

  private List<Match> collectMatches(Collection<Suspect> suspects, AlertContext alertContext) {
    return matchCollector.collectMatches(suspects, alertContext);
  }

  public enum Option {
    WATCHLIST_LEVEL,
    ATTACH_ALERT,
    FOR_LEARNING,
    FOR_RECOMMENDATION,
    ONLY_UNSOLVED,
  }
}
