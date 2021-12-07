package com.silenteight.registration.api.library.v1;

import lombok.Getter;

import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.util.MutableHandlerRegistry;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.UUID;

public class GrpcServerExtension implements BeforeEachCallback, AfterEachCallback {

  @Getter
  private ManagedChannel channel;
  private Server server;
  private MutableHandlerRegistry serviceRegistry;

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    serviceRegistry = new MutableHandlerRegistry();

    var serverName = UUID.randomUUID().toString();

    server = InProcessServerBuilder.forName(serverName)
        .fallbackHandlerRegistry(serviceRegistry)
        .directExecutor()
        .build();
    server.start();

    channel = InProcessChannelBuilder.forName(serverName)
        .directExecutor()
        .build();
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    serviceRegistry = null;
    channel.shutdown();
    server.shutdown();
  }

  public void addService(BindableService bindableService) {
    serviceRegistry.addService(bindableService);
  }
}
