package com.silenteight.customerbridge.common.protocol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.alert.FeatureVector;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.alert.Match.Flags;
import com.silenteight.proto.serp.v1.alert.VectorElement;
import com.silenteight.proto.serp.v1.common.ObjectId;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Slf4j
public class MatchWrapper {

  @NonNull
  @Getter
  private final Match match;

  public ObjectId getId() {
    return match.getId();
  }

  public String getSourceId() {
    return match.getId().getSourceId();
  }

  public Optional<Any> getMatchDetails() {
    return match.hasDetails() ? of(match.getDetails()) : empty();
  }

  public Optional<Any> getMatchedPartyDetails() {
    return getMatchedParty().flatMap(PartyWrapper::getDetails);
  }

  public Optional<PartyWrapper> getMatchedParty() {
    return match.hasMatchedParty() ? of(new PartyWrapper(match.getMatchedParty())) : empty();
  }

  public <T extends Message> Optional<T> unpackMatchedPartyDetails(Class<T> detailsType) {
    return getMatchedParty().flatMap(partyWrapper -> partyWrapper.unpackDetails(detailsType));
  }

  public boolean isNew() {
    return !isObsolete() && !isSolved();
  }

  public boolean isObsolete() {
    return 0 != (match.getFlags() & Flags.FLAG_OBSOLETE_VALUE);
  }

  public boolean isSolved() {
    return 0 != (match.getFlags() & Flags.FLAG_SOLVED_VALUE);
  }

  public FeatureVector getFeatureVector() {
    return match.getFeatureVector();
  }

  public VectorElement[] getVectorElements() {
    return match.getFeatureVector().getElementsList().toArray(VectorElement[]::new);
  }

  public ByteString getFeaturesSignature() {
    return match.getFeatureVector().getFeaturesSignature();
  }

  public ByteString getVectorSignature() {
    return match.getFeatureVector().getVectorSignature();
  }
}
