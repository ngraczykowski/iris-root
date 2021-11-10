package com.silenteight.warehouse.retention.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.query.streaming.AllDataProvider;
import com.silenteight.warehouse.indexer.query.streaming.FetchAllDataRequest;
import com.silenteight.warehouse.indexer.query.streaming.FetchedDocument;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static java.util.List.of;

@RequiredArgsConstructor
@Slf4j
class AllDocumentsInIndexProcessor {

  @NonNull
  AllDataProvider dataProvider;

  public void processAllAlertsIds(String index, Consumer<Collection<String>> documentsIdConsumer) {
    log.debug("Clearing PII data from index `{}`.", index);
    InnerConsumer innerConsumer = new InnerConsumer(documentsIdConsumer);
    FetchAllDataRequest request = FetchAllDataRequest
        .builder()
        .indexes(of(index))
        .fields(of(DISCRIMINATOR))
        .build();
    dataProvider.fetchData(request, innerConsumer);
  }

  @RequiredArgsConstructor
  private static class InnerConsumer implements Consumer<Collection<FetchedDocument>> {

    @NonNull
    private final Consumer<Collection<String>> consumer;

    @Override
    public void accept(Collection<FetchedDocument> documents) {
      log.debug("Received {} documents to clear PII data", documents.size());
      List<String> discriminators = documents
          .stream()
          .map(document -> document.getFieldValue(DISCRIMINATOR))
          .collect(Collectors.toList());
      consumer.accept(discriminators);
    }
  }
}
