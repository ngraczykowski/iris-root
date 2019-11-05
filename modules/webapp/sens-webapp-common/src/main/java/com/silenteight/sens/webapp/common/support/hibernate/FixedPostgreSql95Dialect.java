package com.silenteight.sens.webapp.common.support.hibernate;

import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.Types;

public class FixedPostgreSql95Dialect extends PostgreSQL95Dialect {

  public FixedPostgreSql95Dialect() {
    super();
    registerColumnType(Types.BLOB, "bytea");
  }

  @Override
  public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
    if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
      return BinaryTypeDescriptor.INSTANCE;
    }
    return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
  }
}
