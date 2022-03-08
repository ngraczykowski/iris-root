package com.silenteight.scb.ingest.adapter.incomming.common.protocol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.alert.Party;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import com.google.protobuf.Any;
import com.google.protobuf.Message;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
public class PartyWrapper {

  @NonNull
  @Getter
  private final Party party;

  public <T extends Message> Optional<T> unpackDetails(Class<T> type) {
    return getDetails().flatMap(any -> AnyUtils.maybeUnpack(any, type));
  }

  public Optional<Any> getDetails() {
    return party.hasDetails() ? of(party.getDetails()) : empty();
  }
}