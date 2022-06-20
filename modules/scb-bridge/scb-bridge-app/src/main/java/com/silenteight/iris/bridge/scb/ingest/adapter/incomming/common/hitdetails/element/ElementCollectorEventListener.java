/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.element;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Attribute;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Synonym;

public interface ElementCollectorEventListener {

  void onDivider();

  void onAttribute(Attribute attribute);

  void onSynonym(Synonym synonym);

  void onText(String text);
}
