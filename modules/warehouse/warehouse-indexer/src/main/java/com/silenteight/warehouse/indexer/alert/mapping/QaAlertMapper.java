package com.silenteight.warehouse.indexer.alert.mapping;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.QaAlert;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.format;

@RequiredArgsConstructor
public class QaAlertMapper {

  public static final String STATE = "state";
  public static final String COMMENT = "comment";
  public static final String LEVEL = "level";

  public static Map<String, Object> convertAlertToAttributes(QaAlert alert) {
    Map<String, Object> documentAttributes = new LinkedHashMap<>();
    documentAttributes.put(getFieldName(alert.getLevel(), STATE), alert.getStateValue());
    documentAttributes.put(getFieldName(alert.getLevel(), COMMENT), alert.getComment());
    return documentAttributes;
  }

  private static String getFieldName(int level, String fieldName) {
    return format("qa.level-%d.%s", level, fieldName);
  }
}
