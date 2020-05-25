package com.silenteight.serp.common.testing.grpc;

import io.grpc.BindableService;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public abstract class BaseRpcClientTest<S extends BindableService> {

  protected abstract S getBindableService();

  @Rule
  public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private ManagedChannel channel;

  @Before
  public void givenRpcChannel() throws IOException {
    String serverName = InProcessServerBuilder.generateName();

    Server server = InProcessServerBuilder
        .forName(serverName)
        .directExecutor()
        .addService(getBindableService())
        .build()
        .start();

    grpcCleanup.register(server);

    channel = grpcCleanup.register(
        InProcessChannelBuilder.forName(serverName).directExecutor().build());

    setUpClient(channel);
  }

  protected void setUpClient(Channel channel) {
  }
}
