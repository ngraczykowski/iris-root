package com.silenteight.adjudication.engine.common.grpc.logging;

import io.grpc.*;
import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.ServerCall.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Map;

final class LoggingServerInterceptor implements ServerInterceptor {

  private static final Marker ACCESS = MarkerFactory.getMarker("ACCESS");
  private static final String MDC_GRPC_TRAILERS = "grpc.trailers";
  private static final String MDC_GRPC_MESSAGE_TYPE = "grpc.message.type";

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

    var context = new GrpcRequestContext<>(call);
    var loggingCall = new LoggingServerCall<>(call, context);
    var nextListener = next.startCall(loggingCall, headers);

    return new LoggingServerCallListener<>(nextListener, context);
  }

  private final class LoggingServerCall<ReqT, RespT> extends
      SimpleForwardingServerCall<ReqT, RespT> {

    private final GrpcRequestContext<ReqT, RespT> context;

    LoggingServerCall(ServerCall<ReqT, RespT> delegate, GrpcRequestContext<ReqT, RespT> context) {

      super(delegate);
      this.context = context;
    }

    @Override
    public void request(int numMessages) {
      try (var ignored = context.newScope()) {
        if (log.isTraceEnabled(ACCESS)) {
          log.trace(ACCESS, "Server request: method={}, numMessages={}",
              context.getFullMethodName(), numMessages);
        }

        delegate().request(numMessages);
      }
    }

    @Override
    public void sendHeaders(Metadata headers) {
      try (var ignored = context.newScope()) {
        if (log.isTraceEnabled(ACCESS)) {
          log.trace(ACCESS, "Server headers: method={}, headers={}",
              context.getFullMethodName(), headers.keys());
        }

        delegate().sendHeaders(headers);
      }
    }

    @Override
    public void sendMessage(RespT message) {
      var type = message.getClass().getName();

      try (var ignored = context.newScope(Map.of(MDC_GRPC_MESSAGE_TYPE, type))) {
        if (log.isInfoEnabled(ACCESS)) {
          log.info(ACCESS, "Server message: method={}, type={}", context.getFullMethodName(), type);
        }

        delegate().sendMessage(message);
      }
    }

    @Override
    public void close(Status status, Metadata trailers) {
      try (var ignored = context.newScope(Map.of(MDC_GRPC_TRAILERS, trailers.toString()))) {
        if (log.isInfoEnabled(ACCESS)) {
          log.info(ACCESS, "Server close: method={}, code={}, trailers={}",
              context.getFullMethodName(), status.getCode(), trailers.keys());
        }

        delegate().close(status, trailers);
      }
    }
  }

  private final class LoggingServerCallListener<ReqT, RespT>
      extends SimpleForwardingServerCallListener<ReqT> {

    private final GrpcRequestContext<ReqT, RespT> context;

    protected LoggingServerCallListener(
        Listener<ReqT> delegate, GrpcRequestContext<ReqT, RespT> context) {

      super(delegate);
      this.context = context;
    }

    @Override
    public void onMessage(ReqT message) {
      var type = message.getClass().getName();

      try (var ignored = context.newScope(Map.of(MDC_GRPC_MESSAGE_TYPE, type))) {
        if (log.isInfoEnabled(ACCESS)) {
          log.info(ACCESS, "Client message: method={}, type={}", context.getFullMethodName(), type);
        }

        delegate().onMessage(message);
      }
    }

    @Override
    public void onHalfClose() {
      try (var ignored = context.newScope()) {
        if (log.isTraceEnabled(ACCESS)) {
          log.trace(ACCESS, "Client half close: method={}", context.getFullMethodName());
        }

        delegate().onHalfClose();
      }
    }

    @Override
    public void onCancel() {
      try (var ignored = context.newScope()) {
        if (log.isTraceEnabled(ACCESS)) {
          log.trace(ACCESS, "Client cancel: method={}", context.getFullMethodName());
        }

        delegate().onCancel();
      }
    }

    @Override
    public void onComplete() {
      try (var ignored = context.newScope()) {

        if (log.isTraceEnabled(ACCESS)) {
          log.trace(ACCESS, "Client complete: method={}", context.getFullMethodName());
        }

        delegate().onComplete();
      }
    }

    @Override
    public void onReady() {
      try (var ignored = context.newScope()) {
        if (log.isTraceEnabled(ACCESS)) {
          log.trace(ACCESS, "Client ready: method={}", context.getFullMethodName());
        }

        delegate().onReady();
      }
    }
  }
}
