package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.domain.ObjectPath;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class InsertNonExistingFileNamesQuery {

  private static final String SQL =
      "INSERT INTO pb_learning_file (file_name, bucket_name, status)\n"
          + "VALUES (:fileName, :bucketName, 'NEW')\n"
          + "ON CONFLICT DO NOTHING \n"
          + "RETURNING file_name";

  private final JdbcTemplate jdbcTemplate;

  List<String> execute(List<ObjectPath> fileNames) {
    var sql = createQuery();
    var keyHolder = new GeneratedKeyHolder();
    var savedNames = new ArrayList<String>();
    fileNames.forEach(fm -> {
      var paramMap =
          Map.of("fileName", fm.getName(),
              "bucketName", fm.getBucket());
      sql.updateByNamedParam(paramMap, keyHolder);
      savedNames.addAll(extractValuesFromKeyHolder(keyHolder));
    });
    sql.flush();
    return savedNames;
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(SQL);
    sql.declareParameter(new SqlParameter("fileName", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("bucketName", Types.VARCHAR));
    sql.setReturnGeneratedKeys(true);

    sql.compile();

    return sql;
  }

  private static List<String> extractValuesFromKeyHolder(GeneratedKeyHolder keyHolder) {
    return keyHolder
        .getKeyList()
        .stream()
        .map(Map::values)
        .flatMap(Collection::stream)
        .map(Object::toString)
        .collect(Collectors.toList());
  }
}
