/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.lexer;

public interface LexerEventListener {

  void onStart();

  void onEmptyLine();

  void onFinish();

  void onKey(int lineIndex, int positionIndex, String key);

  void onText(int lineIndex, int positionIndex, String text);

  void onSectionDivider(int lineIndex, int positionIndex);

  void onListPrefix(int lineIndex, int positionIndex);

  void onActiveListPrefix(int lineIndex, int positionIndex);

  void onError(int lineIndex, int positionIndex, String text);
}
