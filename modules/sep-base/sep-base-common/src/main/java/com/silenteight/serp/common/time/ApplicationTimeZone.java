package com.silenteight.serp.common.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationTimeZone {

  public static final TimeZone TIME_ZONE = TimeZone.getDefault();
}
