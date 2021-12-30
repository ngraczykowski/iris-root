package com.silenteight.bridge.core.recommendation.infrastructure.db;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.ArrayList;

@Slf4j
@Configuration
class JdbcConverterConfiguration extends AbstractJdbcConfiguration {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Bean
  public @NotNull JdbcCustomConversions jdbcCustomConversions() {
    var converters = new ArrayList<Converter<?, ?>>();
    converters.add(EntityWritingConverter.INSTANCE);
    converters.add(EntityReadingConverter.INSTANCE);
    return new JdbcCustomConversions(converters);
  }

  @WritingConverter
  enum EntityWritingConverter implements Converter<RecommendationMetadata, PGobject> {
    INSTANCE;

    @Override
    public PGobject convert(@NotNull RecommendationMetadata source) {
      var jsonObject = new PGobject();
      jsonObject.setType("jsonb");

      Try.run(() -> jsonObject.setValue(OBJECT_MAPPER.writeValueAsString(source)))
          .onFailure(
              e -> log.error("Could not write RecommendationMetadata object to database", e));
      return jsonObject;
    }
  }

  @ReadingConverter
  enum EntityReadingConverter implements Converter<PGobject, RecommendationMetadata> {
    INSTANCE;

    @Override
    public RecommendationMetadata convert(PGobject pgObject) {
      var source = pgObject.getValue();

      return Try.of(() -> OBJECT_MAPPER.readValue(source, RecommendationMetadata.class))
          .onFailure(
              e -> log.error("Could not read RecommendationMetadata object from database", e))
          .getOrNull();
    }
  }
}
