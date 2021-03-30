package com.silenteight.hsbc.bridge;

import com.silenteight.hsbc.bridge.adjudication.AdjudicationModule;
import com.silenteight.hsbc.bridge.alert.AlertModule;
import com.silenteight.hsbc.bridge.analysis.AnalysisModule;
import com.silenteight.hsbc.bridge.bulk.BulkModule;
import com.silenteight.hsbc.bridge.match.MatchModule;
import com.silenteight.hsbc.bridge.model.Model;
import com.silenteight.hsbc.bridge.recommendation.RecommendationModule;
import com.silenteight.hsbc.bridge.rest.RestModule;
import com.silenteight.hsbc.datasource.category.DataSourceCategoryModule;
import com.silenteight.hsbc.datasource.grpc.DataSourceApiGrpcModule;
import com.silenteight.hsbc.datasource.provider.DataSourceProviderModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@EnableJpaRepositories("com.silenteight.hsbc")
@EntityScan(basePackages = {"com.silenteight.hsbc"})
@EnableTransactionManagement
@ComponentScan(basePackageClasses = {
    AlertModule.class,
    AdjudicationModule.class,
    AnalysisModule.class,
    BulkModule.class,
    MatchModule.class,
    Model.class,
    RestModule.class,
    RecommendationModule.class,

    DataSourceApiGrpcModule.class,
    DataSourceProviderModule.class,
    DataSourceCategoryModule.class
})
public class BridgeApplication {

  public static void main(String[] args) {
    SpringApplication.run(BridgeApplication.class, args);
  }
}
