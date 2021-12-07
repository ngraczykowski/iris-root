package com.silenteight.universaldatasource.api.library.transaction.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.transaction.v1.TransactionFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class TransactionFeatureInputOut implements Feature {

  String feature;

  @Builder.Default
  List<String> transactionMessages = List.of();

  @Builder.Default
  List<WatchlistNameOut> watchlistNames = List.of();

  WatchlistTypeOut watchlistType;

  @Builder.Default
  List<String> matchingTexts = List.of();

  static TransactionFeatureInputOut createFrom(TransactionFeatureInput input) {
    return TransactionFeatureInputOut.builder()
        .feature(input.getFeature())
        .transactionMessages(input.getTransactionMessagesList())
        .watchlistNames(input.getWatchlistNamesList().stream()
            .map(WatchlistNameOut::createFrom)
            .collect(Collectors.toList()))
        .watchlistType(WatchlistTypeOut.valueOf(input.getWatchlistType().name()))
        .matchingTexts(input.getMatchingTextsList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
