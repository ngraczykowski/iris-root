package com.silenteight.warehouse.statistics;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.common.domain.RecommendationMapper;
import com.silenteight.warehouse.common.properties.RecommendationProperties;
import com.silenteight.warehouse.indexer.alert.AlertRepository;
import com.silenteight.warehouse.statistics.Annotations.Daily;
import com.silenteight.warehouse.statistics.aggregators.AlertAggregator;
import com.silenteight.warehouse.statistics.computers.AlertRecommendationComputer;
import com.silenteight.warehouse.statistics.extractors.AlertDataExtractor;
import com.silenteight.warehouse.statistics.get.DailyRecommendationStatisticsRepository;
import com.silenteight.warehouse.statistics.persistance.DailyRecommendationPersistence;
import com.silenteight.warehouse.statistics.properties.DailyStatisticsProperties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableJpaRepositories
@EntityScan
@EnableScheduling
@EnableConfigurationProperties(DailyStatisticsProperties.class)
@ConditionalOnProperty(value = "warehouse.statistics.daily.recommendation-enabled")
public class StatisticsConfiguration {

  @Bean
  AlertRecommendationComputer alertRecommendationComputer(
      RecommendationProperties recommendationProperties) {
    return new AlertRecommendationComputer(new RecommendationMapper(recommendationProperties));
  }

  @Bean
  AlertDataExtractor alertDataExtractor(
      AlertRepository repository) {
    return new AlertDataExtractor(repository);
  }

  @Bean
  DailyRecommendationPersistence dailyRecommendationPersistance(
      DailyRecommendationStatisticsRepository repository) {
    return new DailyRecommendationPersistence(repository);
  }

  @Bean
  AlertAggregator alertDtoAggregator() {
    return new AlertAggregator(AggregationPeriod.DAILY);
  }

  @Bean
  @Daily
  StatisticsCollector statisticsCollector(
      AlertRecommendationComputer computer,
      AlertDataExtractor extractor,
      DailyRecommendationPersistence persistance,
      AlertAggregator aggregator,
      TimeSource timeSource,
      DailyStatisticsProperties properties) {
    return new StatisticsCollectorImpl<>(timeSource,
        extractor, aggregator, computer, persistance, properties.getRecalculationPeriod());
  }
}
