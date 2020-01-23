package com.silenteight.sens.webapp.common.time;

import java.time.Instant;

public interface DateFormatter {

  String format(Instant value);
}
