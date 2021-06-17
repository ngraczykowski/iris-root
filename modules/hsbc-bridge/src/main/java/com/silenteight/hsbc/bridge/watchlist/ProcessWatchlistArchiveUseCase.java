package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.unpacker.FileUnzipper;
import com.silenteight.worldcheck.api.v1.WatchlistType;

import java.util.Set;

@RequiredArgsConstructor
class ProcessWatchlistArchiveUseCase {

  private final WatchlistSaver saveFileUseCase;
  private final FileUnzipper unzipper;
  private final WorldCheckNotifier worldCheckNotifier;

  public void process(RetrievedWatchlist retrievedWatchlist) {

    var core = unzipper.unzip(retrievedWatchlist.getCore());
    var aliases = unzipper.unzip(retrievedWatchlist.getAliases());

    var savedCoreUri = saveFileUseCase.save(core.getInputStream(), core.getName());
    var savedAliasesUri = saveFileUseCase.save(aliases.getInputStream(), aliases.getName());

    var watchlistCollection = Set.of(
        new WatchlistIdentifier(savedCoreUri.toString(), WatchlistType.WORLD_CHECK),
        new WatchlistIdentifier(savedAliasesUri.toString(), WatchlistType.WORLD_CHECK_ALIASES),
        new WatchlistIdentifier(
            retrievedWatchlist.getKeywordsUri(), WatchlistType.WORLD_CHECK_KEYWORDS)
    );

    worldCheckNotifier.notify(watchlistCollection);
  }
}
