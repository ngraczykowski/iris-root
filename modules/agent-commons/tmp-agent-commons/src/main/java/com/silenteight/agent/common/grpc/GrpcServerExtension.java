package com.silenteight.agent.common.grpc;

import lombok.Getter;

import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerServiceDefinition;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.util.MutableHandlerRegistry;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;

//TODO move to proper module (after fixing dependency hell) (MI6-638)
public class GrpcServerExtension implements BeforeEachCallback, AfterEachCallback {

  @Getter
  private ManagedChannel channel;
  @Getter
  private Server server;
  private String serverName;
  private MutableHandlerRegistry serviceRegistry;
  private boolean useDirectExecutor;

  /**
   * Returns {@code this} configured to use a direct executor for the {@link ManagedChannel} and
   * {@link Server}. This can only be called at the rule instantiation.
   */
  public final GrpcServerExtension directExecutor() {
    checkState(serverName == null, "directExecutor() can only be called at the rule instantiation");
    useDirectExecutor = true;
    return this;
  }

  //TODO not used? (MI6-638)
  public final void addService(ServerServiceDefinition service) {
    serviceRegistry.addService(service);
  }

  //TODO not used? (MI6-638)
  public final void addService(BindableService bindableService) {
    serviceRegistry.addService(bindableService);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws IOException {
    serverName = UUID.randomUUID().toString();

    serviceRegistry = new MutableHandlerRegistry();

    InProcessServerBuilder serverBuilder = InProcessServerBuilder.forName(serverName)
        .fallbackHandlerRegistry(serviceRegistry);

    if (useDirectExecutor)
      serverBuilder.directExecutor();

    server = serverBuilder.build().start();

    InProcessChannelBuilder channelBuilder = InProcessChannelBuilder.forName(serverName);

    if (useDirectExecutor)
      channelBuilder.directExecutor();

    channel = channelBuilder.build();
  }

  @Override
  public void afterEach(ExtensionContext context) {
    serverName = null;
    serviceRegistry = null;

    channel.shutdown();
    server.shutdown();

    try {
      channel.awaitTermination(1, TimeUnit.MINUTES);
      server.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    } finally {
      channel.shutdownNow();
      channel = null;

      server.shutdownNow();
      server = null;
    }
  }
}
