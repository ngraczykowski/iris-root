package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import com.silenteight.datasource.categories.api.v2.*;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class InsertCategoryValueQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO uds_category_value(category_id, match_name, category_value)\n"
          + " VALUES (:category_id, :match_name, :category_value)\n"
          + " ON CONFLICT DO NOTHING\n"
          + " RETURNING category_value_id, category_id, match_name";

  private final BatchSqlUpdate batchSqlUpdate;

  InsertCategoryValueQuery(JdbcTemplate jdbcTemplate) {
    batchSqlUpdate = new BatchSqlUpdate();

    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);

    batchSqlUpdate.declareParameter(new SqlParameter("category_id", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("match_name", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("category_value", Types.VARCHAR));
    batchSqlUpdate.setReturnGeneratedKeys(true);

    batchSqlUpdate.compile();
  }

  List<CreatedCategoryValue> execute(List<CreateCategoryValuesRequest> categoryValues) {
    List<Map<String, Object>> keyList = new ArrayList<>();
    var keyHolder = new GeneratedKeyHolder();
    for (CreateCategoryValuesRequest c : categoryValues) {
      for (CategoryValue categoryValue : c.getCategoryValuesList()) {
        update(c.getCategory(), categoryValue, keyHolder);
        keyList.addAll(keyHolder.getKeyList());
      }
    }

    batchSqlUpdate.flush();

    return getCreatedCategoryValues(keyList);
  }

  private void update(
      String category, CategoryValue categoryValue, GeneratedKeyHolder keyHolder) {
    var paramMap =
        Map.of("category_id", category,
            "match_name", categoryValue.getMatch(),
            "category_value", categoryValue.getSingleValue());
    batchSqlUpdate.updateByNamedParam(paramMap, keyHolder);
  }

  private List<CreatedCategoryValue> getCreatedCategoryValues(List<Map<String, Object>> keyList) {
    List<CreatedCategoryValue> categoryValueList = new ArrayList<>();

    for (Map<String, Object> it : keyList) {
      CreatedCategoryValue build = CreatedCategoryValue
          .newBuilder()
          .setName(it.get("category_id").toString() + "/values/" + it.get("category_value_id"))
          .setMatch(it.get("match_name").toString())
          .build();
      categoryValueList.add(build);
    }
    return categoryValueList;
  }
}
