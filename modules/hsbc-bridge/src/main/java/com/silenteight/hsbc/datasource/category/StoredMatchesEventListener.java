package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent;
import com.silenteight.hsbc.datasource.category.command.StoreMatchCategoriesCommand;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class StoredMatchesEventListener {

  private final StoreMatchCategoriesUseCase storeMatchCategoriesUseCase;

  @EventListener
  @Async
  public void onStoredMatchesEvent(StoredMatchesEvent event) {
    var command = new StoreMatchCategoriesCommand(event.getMatchComposites());

    storeMatchCategoriesUseCase.storeMatchCategories(command);
  }
}
