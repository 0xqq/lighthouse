package com.huya.lighthouse.dataswitch.output;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.huya.lighthouse.dataswitch.util.DataSourceProvider;

public class JdbcOutput extends DataSourceProvider implements Output {

	private String sql;
	private String beforeSql;
	private String afterSql;

	private transient boolean isInit = false;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getBeforeSql() {
		return beforeSql;
	}

	public void setBeforeSql(String beforeSql) {
		this.beforeSql = beforeSql;
	}

	public String getAfterSql() {
		return afterSql;
	}

	public void setAfterSql(String afterSql) {
		this.afterSql = afterSql;
	}

	public void init() {
		executeWithSemicolonComma(getDataSource(), beforeSql);
	}

	@Override
	public void write(final List<Object> rows) {
		if (!isInit) {
			isInit = true;
			init();
		}

		TransactionTemplate tt = new TransactionTemplate(new DataSourceTransactionManager(getDataSource()));
		tt.execute(new TransactionCallback<int[]>() {
			@Override
			public int[] doInTransaction(TransactionStatus status) {
				SqlParameterSource[] batchArgs = newSqlParameterSource(rows);
				return new NamedParameterJdbcTemplate(getDataSource()).batchUpdate(sql, batchArgs);
			}
		});

	}

	private static SqlParameterSource[] newSqlParameterSource(final List<Object> rows) {
		SqlParameterSource[] batchArgs = new SqlParameterSource[rows.size()];
		int i = 0;
		for (Object row : rows) {
			if (row instanceof Map) {
				batchArgs[i] = new MapSqlParameterSource((Map) row);
			} else {
				batchArgs[i] = new BeanPropertySqlParameterSource(row);
			}
			i++;
		}
		return batchArgs;
	}

	private static void executeWithSemicolonComma(DataSource ds, String sql) {
		if (StringUtils.isNotBlank(sql)) {
			String[] sqls = sql.split(";");
			for (String s : sqls) {
				if (StringUtils.isNotBlank(s)) {
					new JdbcTemplate(ds).execute(s);
				}
			}
		}
	}

	@Override
	public void close() {
		executeWithSemicolonComma(getDataSource(), afterSql);
	}

}
