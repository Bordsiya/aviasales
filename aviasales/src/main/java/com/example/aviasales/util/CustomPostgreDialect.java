package com.example.aviasales.util;

import com.vladmihalcea.hibernate.type.array.LongArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.dialect.PostgreSQL10Dialect;

public class CustomPostgreDialect extends PostgreSQL10Dialect {
    public CustomPostgreDialect() {
        super();
        this.registerHibernateType(2003, LongArrayType.class.getName());
    }
}
