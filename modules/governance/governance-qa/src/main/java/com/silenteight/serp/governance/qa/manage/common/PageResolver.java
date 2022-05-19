package com.silenteight.serp.governance.qa.manage.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResolver {

  public static String getNextItem(List<? extends Tokenable> list, Integer pageSize) {
    if (list.isEmpty() || list.size() < pageSize)
      return null;

    return list.get(list.size() - 1).getToken();
  }
}
