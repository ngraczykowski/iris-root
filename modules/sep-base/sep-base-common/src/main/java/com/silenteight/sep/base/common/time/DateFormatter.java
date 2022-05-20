package com.silenteight.sep.base.common.time;

import java.time.temporal.TemporalAccessor;

public interface DateFormatter {

  String format(TemporalAccessor value);
}
