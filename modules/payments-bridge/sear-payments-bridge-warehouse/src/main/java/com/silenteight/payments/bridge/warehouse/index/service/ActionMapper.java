package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ActionMapper {

  static String mapAction(@Nullable String action) {
    return StringUtils.isNotBlank(action) ? action : "ACTION_INVESTIGATE";
  }

  static String mapComment(@Nullable String comment) {
    return StringUtils.isNotBlank(comment)
           ? comment
           : "S8 recommended action: Manual Investigation";
  }
}
