package com.silenteight.hsbc.datasource.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent;
import com.silenteight.hsbc.datasource.category.command.StoreMatchCategoriesCommand;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class StoredMatchesEventListener {

  private final StoreMatchCategoriesUseCase storeMatchCategoriesUseCase;

  @EventListener
  public void onStoredMatchesEvent(StoredMatchesEvent event) {
    log.debug("OnStoredMatchesEvent handled.");
    var command = new StoreMatchCategoriesCommand(event.getMatchComposites());

    storeMatchCategoriesUseCase.storeMatchCategories(command);
  }
}
