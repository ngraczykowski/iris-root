package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.unpacker.FileManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class WatchlistUseCaseConfiguration {

  private final WatchlistSaver watchlistSaver;
  private final WatchlistLoader watchlistLoader;
  private final WorldCheckNotifier worldCheckNotifier;
  private final FileManager unzipper;

  @Bean
  WatchlistEventListener watchlistEventListener() {
    return new WatchlistEventListener(watchlistLoader, processWatchlistArchiveUseCase());
  }

  @Bean
  ProcessWatchlistArchiveUseCase processWatchlistArchiveUseCase() {
    return new ProcessWatchlistArchiveUseCase(watchlistSaver, unzipper, worldCheckNotifier);
  }
}
