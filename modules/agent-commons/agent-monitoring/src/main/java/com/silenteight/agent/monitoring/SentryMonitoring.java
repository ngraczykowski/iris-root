package com.silenteight.agent.monitoring;

import io.sentry.Sentry;

class SentryMonitoring implements Monitoring {

  @Override
  public void captureException(Throwable t) {
    Sentry.captureException(t);
  }
}
