package com.silenteight.scb.ingest.adapter.incomming.common.protocol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Alert.State;
import com.silenteight.proto.serp.v1.alert.Decision;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertUtils;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import com.google.protobuf.*;
import com.google.protobuf.util.Timestamps;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class AlertWrapper {

  @NonNull
  @Getter
  private final Alert alert;

  public boolean isCorrect() {
    return alert.getState() == State.STATE_CORRECT;
  }

  public boolean isWithinThreshold(int matchesThreshold) {
    return alert.getMatchesCount() <= matchesThreshold;
  }

  public Alert toDamagedAlert() {
    return alert.toBuilder().setState(State.STATE_DAMAGED).build();
  }

  public ObjectId getId() {
    return alert.getId();
  }

  public String getSourceId() {
    return alert.getId().getSourceId();
  }

  public Optional<Any> getAlertedPartyDetails() {
    return getAlertedParty().flatMap(PartyWrapper::getDetails);
  }

  public Optional<PartyWrapper> getAlertedParty() {
    return alert.hasAlertedParty() ? Optional.of(new PartyWrapper(alert.getAlertedParty()))
                                   : Optional.empty();
  }

  public <T extends Message> Optional<T> unpackDetails(Class<T> type) {
    return getAlertDetails().flatMap(any -> AnyUtils.maybeUnpack(any, type));
  }

  public Optional<Any> getAlertDetails() {
    return alert.hasDetails() ? Optional.of(alert.getDetails()) : Optional.empty();
  }

  public <T extends Message> Optional<T> unpackAlertedPartyDetails(Class<T> detailsType) {
    return getAlertedParty().flatMap(partyWrapper -> partyWrapper.unpackDetails(detailsType));
  }

  public int getMatchCount() {
    return alert.getMatchesCount();
  }

  public void iterateMatches(BiConsumer<Integer, MatchWrapper> consumer) {
    int matchCount = alert.getMatchesCount();
    for (int i = 0; i < matchCount; i++) {
      Match match = alert.getMatches(i);
      consumer.accept(match.getIndex(), new MatchWrapper(match));
    }
  }

  public Optional<Decision> getLatestAnalystDecision() {
    if (alert.getDecisionsCount() < 1)
      return Optional.empty();

    return alert.getDecisionsList().stream().max(new DecisionComparator());
  }

  public String getDecisionGroup() {
    return alert.getDecisionGroup();
  }

  public ByteString getModelSignature() {
    return alert.getModelSignature();
  }

  public Alert.Builder toBuilder() {
    return alert.toBuilder();
  }

  public String toShortDebugString() {
    return TextFormat.shortDebugString(alert);
  }

  public Timestamp getGeneratedAt() {
    return alert.getGeneratedAt();
  }

  public boolean isLearnFlag() {
    return (alert.getFlags() & Alert.Flags.FLAG_LEARN_VALUE) != 0;
  }

  public boolean isProcessFlag() {
    return (alert.getFlags() & Alert.Flags.FLAG_PROCESS_VALUE) != 0;
  }

  public State getState() {
    return alert.getState();
  }

  public String getDebugId() {
    return AlertUtils.getDebugId(alert);
  }

  public Timestamp getReceiveAt() {
    return alert.getReceivedAt();
  }

  private static final class DecisionComparator implements Comparator<Decision>, Serializable {

    private static final long serialVersionUID = 8438656895103860692L;

    @Override
    public int compare(Decision o1, Decision o2) {
      return Timestamps.compare(o1.getCreatedAt(), o2.getCreatedAt());
    }
  }
}
