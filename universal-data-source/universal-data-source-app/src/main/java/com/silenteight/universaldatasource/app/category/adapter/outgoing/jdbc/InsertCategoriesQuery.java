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
          + " ON CONFLICT DO NOTHING\n"
          + " RETURNING category_id, category_display_name,\n"
          + " category_type, allowed_values, multi_value";


  private final BatchSqlUpdate batchSqlUpdate;

  InsertCategoriesQuery(JdbcTemplate jdbcTemplate) {
    batchSqlUpdate = new BatchSqlUpdate();

    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter("category_id", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("category_display_name", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("category_type", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("allowed_values", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("multi_value", Types.BOOLEAN));
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
        Map.of("category_id", category.getName(),
            "category_display_name", getDisplayName(category),
            "category_type", category.getType(),
            "allowed_values", String.join(",", category.getAllowedValuesList()),
            "multi_value", category.getMultiValue());

    batchSqlUpdate.updateByNamedParam(paramMap, keyHolder);
  }

  private static List<Category> getCategories(List<Map<String, Object>> keyList) {
    return keyList
        .stream()
        .map(it -> Category
            .newBuilder()
            .setName(it.get("category_id").toString())
            .setDisplayName(it.get("category_display_name").toString())
            .setType(CategoryType.valueOf(it.get("category_type").toString()))
            .addAllAllowedValues(List.of(it.get("allowed_values").toString().split(",")))
            .setMultiValue((boolean) it.get("multi_value"))
            .build()).collect(Collectors.toList());
  }

  private static String getDisplayName(Category category) {
    if (StringUtils.isBlank(category.getDisplayName()))
      return category.getName();

    return category.getDisplayName();
  }
}
