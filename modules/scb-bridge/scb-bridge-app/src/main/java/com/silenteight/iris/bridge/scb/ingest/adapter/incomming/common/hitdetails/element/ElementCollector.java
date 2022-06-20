/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.element;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.lexer.LexerEventListener;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Attribute;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Synonym;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ElementCollector implements LexerEventListener {

  @NonNull
  private final ElementCollectorEventListener listener;
  private final List<Synonym> tempSynonyms = new ArrayList<>();
  private String key;
  private String text;
  private boolean hasSynonyms;
  private boolean hasListPrefix;
  private boolean isActive;

  @Override
  public void onStart() {
    // Do nothing
  }

  @Override
  public void onEmptyLine() {
    // Do nothing
  }

  @Override
  public void onFinish() {
    commit();
  }

  @Override
  public void onKey(int lineIndex, int positionIndex, @NonNull String key) {
    if (isEqualsIgnoreCase(key, "Synonyms")) {
      hasSynonyms = true;
    } else {
      commit();
      this.key = key;
    }
  }

  private static boolean isEqualsIgnoreCase(@NonNull String key, String t) {
    return StringUtils.equalsIgnoreCase(key, t);
  }

  @Override
  public void onText(int lineIndex, int positionIndex, @NonNull String text) {
    if (isEqualsIgnoreCase(text, "none"))
      return;

    if (hasSynonyms && hasListPrefix) {
      tempSynonyms.add(new Synonym(text, isActive));
      hasListPrefix = false;
    } else {
      if (this.text != null) {
        commit();
      }
      this.text = text;
    }
  }

  @Override
  public void onSectionDivider(int lineIndex, int positionIndex) {
    commit();
    listener.onDivider();
  }

  @Override
  public void onListPrefix(int lineIndex, int positionIndex) {
    isActive = false;
    hasListPrefix = true;
  }

  @Override
  public void onActiveListPrefix(int lineIndex, int positionIndex) {
    isActive = true;
    hasListPrefix = true;
  }

  @Override
  public void onError(int lineIndex, int positionIndex, @NonNull String text) {
    commit();
  }

  private void commit() {
    if (key != null) {
      listener.onAttribute(new Attribute(key, text));
      if (hasSynonyms)
        tempSynonyms.forEach(listener::onSynonym);
    } else if (text != null) {
      listener.onText(text);
    }
    reset();
  }

  private void reset() {
    key = null;
    text = null;
    hasSynonyms = false;
    isActive = false;
    hasListPrefix = false;
    tempSynonyms.clear();
  }
}
