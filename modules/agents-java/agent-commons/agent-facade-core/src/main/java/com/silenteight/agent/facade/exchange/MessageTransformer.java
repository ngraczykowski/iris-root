package com.silenteight.agent.facade.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.agent.monitoring.Monitoring;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.integration.transformer.GenericTransformer;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class MessageTransformer implements GenericTransformer<Any, AgentExchangeRequest> {

  private final Monitoring monitoring;

  @Override
  public AgentExchangeRequest transform(Any any) {
    try {
      return any.unpack(AgentExchangeRequest.class);
    } catch (InvalidProtocolBufferException e) {
      log.error(format("Cannot transform any message: %s", any), e);
      monitoring.captureException(e);
      throw new InvalidProtobufMessageException(any, e);
    }
  }

}
