package com.silenteight.sens.webapp.common.time;

import java.time.temporal.TemporalAccessor;

public interface DateFormatter {

  String format(TemporalAccessor value);
}
