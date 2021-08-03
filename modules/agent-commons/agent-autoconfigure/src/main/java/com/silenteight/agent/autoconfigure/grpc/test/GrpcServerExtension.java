package com.silenteight.agent.autoconfigure.grpc.test;

import lombok.Getter;

import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerServiceDefinition;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.util.MutableHandlerRegistry;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;

public class GrpcServerExtension implements BeforeEachCallback, AfterEachCallback {

  @Getter
  private ManagedChannel channel;
  @Getter
  private Server server;
  private String serverName;
  private MutableHandlerRegistry serviceRegistry;
  @Nullable
  private GrpcServerConfigurer configurer;
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

  public final GrpcServerExtension configure(GrpcServerConfigurer grpcServerConfigurer) {
    configurer = grpcServerConfigurer;
    return this;
  }

  public final void addService(ServerServiceDefinition service) {
    serviceRegistry.addService(service);
  }

  public final void addService(BindableService bindableService) {
    serviceRegistry.addService(bindableService);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws IOException {
    serverName = UUID.randomUUID().toString();

    serviceRegistry = new MutableHandlerRegistry();

    var serverBuilder = InProcessServerBuilder.forName(serverName)
        .fallbackHandlerRegistry(serviceRegistry);

    if (useDirectExecutor)
      serverBuilder.directExecutor();

    Optional.ofNullable(configurer)
        .ifPresent(configurer -> configurer.accept(serverBuilder));

    server = serverBuilder.build().start();

    var channelBuilder = InProcessChannelBuilder.forName(serverName);

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
