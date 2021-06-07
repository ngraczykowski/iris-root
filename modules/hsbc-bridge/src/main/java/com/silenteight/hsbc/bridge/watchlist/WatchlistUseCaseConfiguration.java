package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.file.DownloadResourceUseCase;
import com.silenteight.hsbc.bridge.file.SaveResourceUseCase;
import com.silenteight.hsbc.bridge.unpacker.FileUnzipper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class WatchlistUseCaseConfiguration {

  private final SaveResourceUseCase saveResourceUseCase;
  private final WorldCheckNotifierServiceClient worldCheckNotifier;
  private final DownloadResourceUseCase downloadFileUseCase;
  private final ApplicationEventPublisher eventPublisher;
  private final FileUnzipper unzipper;

  @Bean
  WatchlistEventListener watchlistEventListener() {
    return new WatchlistEventListener(downloadFileUseCase, processWatchlistArchiveUseCase());
  }

  @Bean
  SaveOriginalWatchlistUseCase saveOriginalWatchlistUseCase() {
    return new SaveOriginalWatchlistUseCase(saveResourceUseCase, eventPublisher);
  }

  @Bean
  ProcessWatchlistArchiveUseCase processWatchlistArchiveUseCase() {
    return new ProcessWatchlistArchiveUseCase(saveResourceUseCase, unzipper, worldCheckNotifier);
  }
}
