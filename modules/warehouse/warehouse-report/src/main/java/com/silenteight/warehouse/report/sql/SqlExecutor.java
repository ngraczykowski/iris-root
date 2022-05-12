package com.silenteight.warehouse.report.sql;

import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;

import java.io.InputStream;
import java.util.function.Consumer;

public interface SqlExecutor {

  void execute(SqlExecutorDto sqlDto, Consumer<InputStream> consumer);
}
