package com.silenteight.hsbc.datasource.grpc;

import lombok.extern.slf4j.Slf4j;

import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
class DatasourceGrpcInterceptor implements ServerInterceptor {

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    var listener = next.startCall(call, headers);
    return new ErrorHandlingCallListener<>(listener, call, headers);
  }

  class ErrorHandlingCallListener<ReqT, RespT> extends SimpleForwardingServerCallListener<ReqT> {

    private ServerCall<ReqT, RespT> call;
    private Metadata headers;

    private final GrpcErrorHandler grpcErrorHandler = new GrpcErrorHandler();

    protected ErrorHandlingCallListener(Listener delegate, ServerCall<ReqT, RespT> serverCall, Metadata headers) {
      super(delegate);
      this.call = serverCall;
      this.headers = headers;
    }

    @Override
    public void onMessage(ReqT message) {
      try {
        super.onMessage(message);
      } catch (RuntimeException e) {
        handleException(e, headers);
      }
    }

    @Override
    public void onHalfClose() {
      try {
        super.onHalfClose();
      } catch (RuntimeException ex) {
        handleException(ex, headers);
      }
    }

    @Override
    public void onReady() {
      try {
        super.onReady();
      } catch (RuntimeException ex) {
        handleException(ex, headers);
      }
    }

    @Override
    public void onCancel() {
      try {
        super.onCancel();
      } catch (RuntimeException e) {
        handleException(e, headers);
      }
    }

    @Override
    public void onComplete() {
      try {
        super.onComplete();
      } catch (RuntimeException e) {
        handleException(e, headers);
      }
    }

    void handleException(RuntimeException ex, Metadata headers) {
      var status = grpcErrorHandler.handle(ex);
      call.close(status, Optional.ofNullable(headers).orElse(new Metadata()));
    }
  }
}
