package com.silenteight.adjudication.engine.solving.application.publisher;

import com.silenteight.adjudication.engine.solving.domain.TraceEvent;

public interface TracePublisher {

  // TODO: use traceModel from trace-[lib,api] whatever
  void publish(TraceEvent event);

}
