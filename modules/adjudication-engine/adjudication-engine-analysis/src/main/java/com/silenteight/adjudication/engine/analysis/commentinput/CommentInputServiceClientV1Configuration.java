package com.silenteight.adjudication.engine.analysis.commentinput;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.comments.api.v1.CommentInputServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(CommentInputServiceClientProperties.class)
@RequiredArgsConstructor
@Profile("datasourcev1")
class CommentInputServiceClientV1Configuration {

  @Valid
  private final CommentInputServiceClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("datasource"))
  private Channel dataSourceChannel;

  @Bean
  CommentInputClient commentInputServiceClient() {
    var stub = CommentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new CommentInputServiceClientV1(stub, properties.getTimeout());
  }
}
