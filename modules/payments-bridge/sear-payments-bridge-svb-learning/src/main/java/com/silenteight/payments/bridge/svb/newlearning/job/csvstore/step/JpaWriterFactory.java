package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.persistence.EntityManagerFactory;

@Component
@Slf4j
@RequiredArgsConstructor
public class JpaWriterFactory {

  private final EntityManagerFactory entityManagerFactory;

  @Nonnull
  public <T> ItemWriter<T> createJpaWriter() {
    var writer = new JpaItemWriter<T>();
    writer.setEntityManagerFactory(entityManagerFactory);
    writer.setUsePersist(true);
    return writer;
  }
}
