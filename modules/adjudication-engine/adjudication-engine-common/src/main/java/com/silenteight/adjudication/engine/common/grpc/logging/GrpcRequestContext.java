package com.silenteight.adjudication.engine.common.grpc.logging;

import lombok.RequiredArgsConstructor;

import io.grpc.ServerCall;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
final class GrpcRequestContext<ReqT, RespT> {

  private static final AtomicLong CALL_ID_GENERATOR = new AtomicLong(1);
  private static final String MDC_EVENT_CATEGORY = "event.category";
  private static final String MDC_EVENT_DATASET = "event.dataset";
  private static final String MDC_EVENT_SEQUENCE = "event.sequence";
  private static final String MDC_GRPC_METHOD = "grpc.method";
  private static final String MDC_GRPC_CALL_ID = "grpc.call_id";

  private final long callId = CALL_ID_GENERATOR.getAndIncrement();
  private final AtomicLong sequenceNumber = new AtomicLong(1);

  private final ServerCall<ReqT, RespT> call;

  Scope newScope() {
    return new Scope();
  }

  Scope newScope(Map<String, String> context) {
    return new Scope(context);
  }

  String getFullMethodName() {
    return call.getMethodDescriptor().getFullMethodName();
  }

  final class Scope implements AutoCloseable {

    private final List<String> additionalKeys;

    Scope() {
      init();
      additionalKeys = emptyList();
    }

    Scope(Map<String, String> context) {
      init();
      additionalKeys = new ArrayList<>(context.size());
      context.forEach((k, v) -> {
        MDC.put(k, v);
        additionalKeys.add(k);
      });
    }

    private void init() {
      MDC.put(MDC_EVENT_CATEGORY, "web");
      MDC.put(MDC_EVENT_DATASET, "grpc.access");
      MDC.put(MDC_EVENT_SEQUENCE, Long.toString(sequenceNumber.getAndIncrement()));
      MDC.put(MDC_GRPC_METHOD, getFullMethodName());
      MDC.put(MDC_GRPC_CALL_ID, Long.toString(callId));
    }

    @Override
    public void close() {
      MDC.remove(MDC_EVENT_CATEGORY);
      MDC.remove(MDC_EVENT_DATASET);
      MDC.remove(MDC_EVENT_SEQUENCE);
      MDC.remove(MDC_GRPC_METHOD);
      MDC.remove(MDC_GRPC_CALL_ID);
      additionalKeys.forEach(MDC::remove);
    }
  }
}
