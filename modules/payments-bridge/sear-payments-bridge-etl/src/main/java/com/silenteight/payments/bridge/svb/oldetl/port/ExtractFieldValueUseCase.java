package com.silenteight.payments.bridge.svb.oldetl.port;

import com.silenteight.payments.bridge.svb.oldetl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.oldetl.model.ExtractFieldStructureValue;

import java.util.List;

public interface ExtractFieldValueUseCase {

  String extractFieldValue(ExtractFieldStructureValue request);

  List<List<String>> extractFieldValues(AbstractMessageStructure messageStructure);

  List<List<String>> extractFieldValues(
      String sourceSystem, AbstractMessageStructure messageStructure);
}
