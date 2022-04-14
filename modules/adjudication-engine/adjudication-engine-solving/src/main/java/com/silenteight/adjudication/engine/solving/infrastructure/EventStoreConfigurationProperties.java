package com.silenteight.adjudication.engine.solving.infrastructure;


class EventStoreConfigurationProperties {
  private String exchange;
  private String routingKey;

  public EventStoreConfigurationProperties(final String exchange, final String routingKey) {
    this.exchange = exchange;
    this.routingKey = routingKey;
  }

  String getExchange() {
    return exchange;
  }

  String getRoutingKey() {
    return routingKey;
  }
}
