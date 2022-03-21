package com.silenteight.payments.bridge.datasource.category.infrastructure.values;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CreateCategoriesValuesProperties.class)
class CreateCategoriesValuesConfiguration {

  private static final int CORE_POOL_SIZE = 2;
  private static final int MAXIMUM_POOL_SIZE = 50;
  private static final int THREAD_POOL_CAPACITY = 250;
  private static final long KEEP_ALIVE_TIME = 0L;

  @Valid
  private final CreateCategoriesValuesProperties properties;

  @Setter(onMethod_ = @GrpcClient("datasource"))
  private Channel categoriesDataChannel;

  @Bean
  DatasourceCategoryValueClient datasourceCategoryValueClient() {
    var stub = CategoryValueServiceGrpc
        .newBlockingStub(categoriesDataChannel)
        .withWaitForReady();

    return new DatasourceCategoryValueClient(stub, properties.getTimeout(), threadPool());
  }

  private ExecutorService threadPool() {
    final ThreadFactory threadFactory = new ThreadFactoryBuilder()
        .setNameFormat("uds-io-%d")
        .setDaemon(true)
        .build();
    return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
        KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(THREAD_POOL_CAPACITY),
        threadFactory);
  }
}
