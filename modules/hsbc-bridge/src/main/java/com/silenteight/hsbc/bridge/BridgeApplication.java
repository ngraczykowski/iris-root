package com.silenteight.hsbc.bridge;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationModule;
import com.silenteight.hsbc.bridge.alert.AlertModule;
import com.silenteight.hsbc.bridge.amqp.AmqpModule;
import com.silenteight.hsbc.bridge.analysis.AnalysisModule;
import com.silenteight.hsbc.bridge.aws.AwsModule;
import com.silenteight.hsbc.bridge.bulk.BulkModule;
import com.silenteight.hsbc.bridge.file.FileModule;
import com.silenteight.hsbc.bridge.grpc.GrpcModule;
import com.silenteight.hsbc.bridge.http.security.SecurityModule;
import com.silenteight.hsbc.bridge.jenkins.JenkinsModule;
import com.silenteight.hsbc.bridge.json.JsonModule;
import com.silenteight.hsbc.bridge.match.MatchModule;
import com.silenteight.hsbc.bridge.model.Model;
import com.silenteight.hsbc.bridge.recommendation.RecommendationModule;
import com.silenteight.hsbc.bridge.report.ReportModule;
import com.silenteight.hsbc.bridge.retention.DataRetentionModule;
import com.silenteight.hsbc.bridge.transfer.TransferModule;
import com.silenteight.hsbc.bridge.unpacker.UnpackerModule;
import com.silenteight.hsbc.bridge.watchlist.WatchlistModule;
import com.silenteight.hsbc.bridge.worldcheck.WorldCheckModule;
import com.silenteight.hsbc.datasource.category.DataSourceCategoryModule;
import com.silenteight.hsbc.datasource.comment.DataSourceCommentModule;
import com.silenteight.hsbc.datasource.grpc.DataSourceApiGrpcModule;
import com.silenteight.hsbc.datasource.provider.DataSourceProviderModule;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan(basePackageClasses = {
    AdjudicationModule.class,
    AlertModule.class,
    AnalysisModule.class,
    AmqpModule.class,
    AwsModule.class,
    GrpcModule.class,
    FileModule.class,
    BulkModule.class,
    DataRetentionModule.class,
    JenkinsModule.class,
    JsonModule.class,
    MatchModule.class,
    Model.class,
    RecommendationModule.class,
    ReportModule.class,
    SecurityModule.class,
    TransferModule.class,
    UnpackerModule.class,
    WatchlistModule.class,
    WorldCheckModule.class,

    DataSourceApiGrpcModule.class,
    DataSourceCategoryModule.class,
    DataSourceCommentModule.class,
    DataSourceProviderModule.class,
})
@EnableAsync
@EnableAutoConfiguration
@EnableJpaRepositories("com.silenteight.hsbc")
@EnableRabbit
@EnableRetry
@EnableScheduling
@EnableTransactionManagement
@EntityScan(basePackages = { "com.silenteight.hsbc" })
public class BridgeApplication {

  public static void main(String[] args) {
    SpringApplication.run(BridgeApplication.class, args);
  }
}
