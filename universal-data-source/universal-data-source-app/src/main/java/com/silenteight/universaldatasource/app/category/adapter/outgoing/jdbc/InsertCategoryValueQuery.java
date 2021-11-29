package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CreatedCategoryValue;

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
      "INSERT INTO uds_category_value(category_id, alert_name, match_name, category_value)\n"
          + " VALUES (:category_id, :alert_name, :match_name, :category_value)\n"
          + " ON CONFLICT DO NOTHING\n"
          + " RETURNING category_value_id, category_id, match_name";

  private static final String CATEGORY_VALUE_ID = "category_value_id";
  private static final String CATEGORY_ID = "category_id";
  private static final String ALERT_NAME = "alert_name";
  private static final String MATCH_NAME = "match_name";
  private static final String CATEGORY_VALUE = "category_value";

  private final BatchSqlUpdate batchSqlUpdate;

  InsertCategoryValueQuery(JdbcTemplate jdbcTemplate) {
    batchSqlUpdate = new BatchSqlUpdate();

    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);

    batchSqlUpdate.declareParameter(new SqlParameter(CATEGORY_ID, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(ALERT_NAME, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(MATCH_NAME, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(CATEGORY_VALUE, Types.VARCHAR));
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
        Map.of(CATEGORY_ID, category,
            ALERT_NAME, categoryValue.getAlert(),
            MATCH_NAME, categoryValue.getMatch(),
            CATEGORY_VALUE, categoryValue.getSingleValue());
    batchSqlUpdate.updateByNamedParam(paramMap, keyHolder);
  }

  private List<CreatedCategoryValue> getCreatedCategoryValues(List<Map<String, Object>> keyList) {
    List<CreatedCategoryValue> categoryValueList = new ArrayList<>();

    for (Map<String, Object> it : keyList) {
      CreatedCategoryValue build = CreatedCategoryValue
          .newBuilder()
          .setName(it.get(CATEGORY_ID).toString() + "/values/" + it.get(CATEGORY_VALUE_ID))
          .setMatch(it.get(MATCH_NAME).toString())
          .build();
      categoryValueList.add(build);
    }
    return categoryValueList;
  }
}
