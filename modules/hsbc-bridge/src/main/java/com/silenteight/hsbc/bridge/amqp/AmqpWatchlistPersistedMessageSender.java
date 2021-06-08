package com.silenteight.hsbc.bridge.amqp;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.watchlist.WatchlistIdentifier;
import com.silenteight.hsbc.bridge.watchlist.WorldCheckNotifier;
import com.silenteight.worldcheck.api.v1.MultipleWatchlistPersisted;
import com.silenteight.worldcheck.api.v1.WatchlistPersisted;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Builder
class AmqpWatchlistPersistedMessageSender implements WorldCheckNotifier {

  private final AmqpTemplate amqpTemplate;
  private final MessageConverter messageConverter;
  private final Configuration configuration;

  @Override
  public void notify(Set<WatchlistIdentifier> identifiers) {
    var convertedMessage =
        messageConverter.toMessage(prepareMessage(identifiers), new MessageProperties());

    amqpTemplate.convertAndSend(
        configuration.getExchangeName(), configuration.getRoutingKey(), convertedMessage);
  }

  private static MultipleWatchlistPersisted prepareMessage(Set<WatchlistIdentifier> identifiers) {
    var builder = MultipleWatchlistPersisted.newBuilder();

    identifiers.stream()
        .map(AmqpWatchlistPersistedMessageSender::toWatchlistPersisted)
        .forEach(builder::addWatchlists);

    return builder.build();
  }

  private static WatchlistPersisted toWatchlistPersisted(WatchlistIdentifier identifier) {
    return WatchlistPersisted.newBuilder()
        .setWatchlistType(identifier.getType())
        .setWatchlistUri(identifier.getUri())
        .build();
  }

  @Value
  @Builder
  static class Configuration {

    String exchangeName;
    String routingKey;
  }
}
