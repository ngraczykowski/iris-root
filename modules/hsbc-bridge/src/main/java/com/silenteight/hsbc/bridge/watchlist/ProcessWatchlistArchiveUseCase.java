package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.file.SaveResourceUseCase;
import com.silenteight.hsbc.bridge.unpacker.FileUnzipper;
import com.silenteight.worldcheck.api.v1.WatchlistType;

import java.util.List;

@RequiredArgsConstructor
class ProcessWatchlistArchiveUseCase {

  private final SaveResourceUseCase saveFileUseCase;
  private final FileUnzipper unzipper;
  private final WorldCheckNotifierServiceClient worldCheckNotifier;

  public void process(RetrievedWatchlist retrievedWatchlist) {

    var core = unzipper.unzip(retrievedWatchlist.getCore());
    var aliases = unzipper.unzip(retrievedWatchlist.getAliases());

    var savedCore = saveFileUseCase.save(core.getInputStream(), core.getName());
    var savedAliases = saveFileUseCase.save(aliases.getInputStream(), aliases.getName());

    var watchlistCollection = List.of(
        new WatchlistIdentifier(savedCore.getUri(), WatchlistType.WORLD_CHECK),
        new WatchlistIdentifier(savedAliases.getUri(), WatchlistType.WORLD_CHECK_ALIASES),
        new WatchlistIdentifier(retrievedWatchlist.getKeywordsUri(), WatchlistType.WORLD_CHECK_KEYWORDS)
    );

    worldCheckNotifier.notify(watchlistCollection);
  }
}
