package com.silenteight.customerbridge.common.hitdetails.element;

import com.silenteight.customerbridge.common.hitdetails.model.Attribute;
import com.silenteight.customerbridge.common.hitdetails.model.Synonym;

public interface ElementCollectorEventListener {

  void onDivider();

  void onAttribute(Attribute attribute);

  void onSynonym(Synonym synonym);

  void onText(String text);
}
