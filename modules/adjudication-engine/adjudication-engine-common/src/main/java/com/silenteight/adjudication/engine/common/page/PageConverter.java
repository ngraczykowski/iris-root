package com.silenteight.adjudication.engine.common.page;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageConverter {

  private static final int DEFAULT_PAGE = 0;

  public static int fromPageToken(String pageToken) {
    if (StringUtils.isNoneBlank(pageToken)) {
      return Integer.parseInt(pageToken);
    }
    return DEFAULT_PAGE;
  }
}
