package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent;
import com.silenteight.hsbc.datasource.category.command.StoreMatchCategoriesCommand;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class StoredMatchesEventListener {

  private final StoreMatchCategoriesUseCase storeMatchCategoriesUseCase;

  @EventListener
  public void onStoredMatchesEvent(StoredMatchesEvent event) {

    storeMatchCategoriesUseCase.storeMatchCategories(StoreMatchCategoriesCommand.builder()
        .matchComposites(event.getMatchComposites())
        .build());
  }
}
