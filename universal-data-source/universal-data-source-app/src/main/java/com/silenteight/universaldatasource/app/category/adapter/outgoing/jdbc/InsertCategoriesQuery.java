package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.datasource.categories.api.v2.CategoryType;

import org.apache.commons.lang3.StringUtils;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InsertCategoriesQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO uds_category(category_id, category_display_name,\n"
          + " category_type, allowed_values, multi_value)\n"
          + " VALUES (:category_id, :category_display_name,\n"
          + " :category_type, :allowed_values, :multi_value)\n"
          + " ON CONFLICT(category_id) DO UPDATE\n"
          + " SET category_display_name = :category_display_name,\n"
          + " category_type = :category_type,\n"
          + " allowed_values = :allowed_values,\n"
          + " multi_value = :multi_value\n"
          + " RETURNING category_id, category_display_name,\n"
          + " category_type, allowed_values, multi_value";

  private static final String CATEGORY_ID = "category_id";
  private static final String CATEGORY_DISPLAY_NAME = "category_display_name";
  private static final String CATEGORY_TYPE = "category_type";
  private static final String ALLOWED_VALUES = "allowed_values";
  private static final String MULTI_VALUE = "multi_value";

  private final BatchSqlUpdate batchSqlUpdate;

  InsertCategoriesQuery(JdbcTemplate jdbcTemplate) {
    batchSqlUpdate = new BatchSqlUpdate();

    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(CATEGORY_ID, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(CATEGORY_DISPLAY_NAME, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(CATEGORY_TYPE, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(ALLOWED_VALUES, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(MULTI_VALUE, Types.BOOLEAN));
    batchSqlUpdate.setReturnGeneratedKeys(true);

    batchSqlUpdate.compile();
  }

  List<Category> execute(List<Category> matchCategoryValueCollection) {
    List<Map<String, Object>> keyList = new ArrayList<>();
    var keyHolder = new GeneratedKeyHolder();
    for (Category category : matchCategoryValueCollection) {
      update(category, keyHolder);
      keyList.addAll(keyHolder.getKeyList());
    }

    batchSqlUpdate.flush();

    return getCategories(keyList);
  }

  private void update(Category category, KeyHolder keyHolder) {
    var paramMap =
        Map.of(CATEGORY_ID, category.getName(),
            CATEGORY_DISPLAY_NAME, getDisplayName(category),
            CATEGORY_TYPE, category.getType(),
            ALLOWED_VALUES, String.join(",", category.getAllowedValuesList()),
            MULTI_VALUE, category.getMultiValue());

    batchSqlUpdate.updateByNamedParam(paramMap, keyHolder);
  }

  private static List<Category> getCategories(List<Map<String, Object>> keyList) {
    return keyList
        .stream()
        .map(it -> Category
            .newBuilder()
            .setName(it.get(CATEGORY_ID).toString())
            .setDisplayName(it.get(CATEGORY_DISPLAY_NAME).toString())
            .setType(CategoryType.valueOf(it.get(CATEGORY_TYPE).toString()))
            .addAllAllowedValues(List.of(it.get(ALLOWED_VALUES).toString().split(",")))
            .setMultiValue((boolean) it.get(MULTI_VALUE))
            .build()).collect(Collectors.toList());
  }

  private static String getDisplayName(Category category) {
    if (StringUtils.isBlank(category.getDisplayName()))
      return category.getName();

    return category.getDisplayName();
  }
}
