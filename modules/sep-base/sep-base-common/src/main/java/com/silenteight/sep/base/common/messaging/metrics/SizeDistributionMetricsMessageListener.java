package com.silenteight.sep.base.common.messaging.metrics;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.messaging.ReceiveMessageListener;
import com.silenteight.sep.base.common.messaging.SendMessageListener;
import com.silenteight.sep.base.common.messaging.metrics.Size.SizeUnit;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.amqp.core.Message;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Slf4j
class SizeDistributionMetricsMessageListener
    implements ReceiveMessageListener, SendMessageListener, MeterBinder {

  @NonNull
  private final SizeDistributionMetrics receivedMessageMetrics;
  @NonNull
  private final SizeDistributionMetrics sentMessageMetrics;

  @Override
  public void onReceived(Message message) {
    getSize(message).ifPresent(receivedMessageMetrics::record);
  }

  @Override
  public void onSent(Message message) {
    getSize(message).ifPresent(sentMessageMetrics::record);
  }

  private static Optional<Size> getSize(Message message) {
    byte[] body = message.getBody();
    if (body == null)
      return empty();

    return of(new Size(SizeUnit.BYTES, body.length));
  }

  @Override
  public void bindTo(MeterRegistry registry) {
    log.debug("Binding message size distribution metrics");

    receivedMessageMetrics.bindTo(registry);
    sentMessageMetrics.bindTo(registry);
  }
}
