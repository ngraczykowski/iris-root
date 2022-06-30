package com.silenteight.agent.autoconfigure.grpc.metadata;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@RequiredArgsConstructor
class AgentInformationEnhancingServerInterceptor implements ServerInterceptor {

  @NonNull
  private final AgentInformation agentInformation;

  @NonNull
  private final EnhancingServerInterceptorConfiguration configuration;

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    return next.startCall(new AgentInformationAppendingServerCall<>(call), headers);
  }

  @Value
  static class EnhancingServerInterceptorConfiguration {

    String nameFieldDescriptor;
    String versionFieldDescriptor;
  }

  private class AgentInformationAppendingServerCall<ReqT, ResT>
      extends SimpleForwardingServerCall<ReqT, ResT> {

    protected AgentInformationAppendingServerCall(ServerCall<ReqT, ResT> delegate) {
      super(delegate);
    }

    @Override
    public void sendHeaders(Metadata headers) {
      headers.put(
          Key.of(configuration.getNameFieldDescriptor(), ASCII_STRING_MARSHALLER),
          agentInformation.getName()
      );
      headers.put(
          Key.of(configuration.getVersionFieldDescriptor(), ASCII_STRING_MARSHALLER),
          agentInformation.getVersion()
      );

      super.sendHeaders(headers);
    }
  }
}
