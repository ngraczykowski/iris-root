package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.element;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Attribute;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Synonym;

public interface ElementCollectorEventListener {

  void onDivider();

  void onAttribute(Attribute attribute);

  void onSynonym(Synonym synonym);

  void onText(String text);
}
