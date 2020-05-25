package com.silenteight.serp.common.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.common.grpc.GrpcExceptionHandler.Callback;

import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@GrpcGlobalServerInterceptor
public class GlobalExceptionHandlingServerInterceptor implements ServerInterceptor {

  private final List<GrpcExceptionHandler> exceptionHandlers;

  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler) {

    ServerCall.Listener<ReqT> listener = serverCallHandler.startCall(serverCall, metadata);
    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(listener) {

      @Override
      public void onMessage(ReqT message) {
        try {
          super.onMessage(message);
        } catch (RuntimeException e) {
          handleException(e);
        }
      }

      @Override
      public void onHalfClose() {
        try {
          super.onHalfClose();
        } catch (RuntimeException e) {
          handleException(e);
        }
      }

      @Override
      public void onCancel() {
        try {
          super.onCancel();
        } catch (RuntimeException e) {
          handleException(e);
        }
      }

      @Override
      public void onComplete() {
        try {
          super.onComplete();
        } catch (RuntimeException e) {
          handleException(e);
        }
      }

      @Override
      public void onReady() {
        try {
          super.onReady();
        } catch (RuntimeException e) {
          handleException(e);
        }
      }

      private void handleException(RuntimeException exception) {
        var closed = new AtomicBoolean(false);

        Callback callback = (status, trailers) -> {
          if (!closed.compareAndSet(false, true))
            return;

          Metadata closingMetadata = trailers;
          if (closingMetadata == null)
            closingMetadata = metadata;
          if (closingMetadata == null)
            closingMetadata = new Metadata();
          serverCall.close(status, closingMetadata);
        };

        for (GrpcExceptionHandler exceptionHandler : exceptionHandlers) {
          exceptionHandler.handleException(exception, metadata, callback);
          if (closed.get())
            break;
        }

        if (!closed.get())
          callback.closeWithStatus(Status.UNKNOWN, metadata);
      }
    };
  }
}
