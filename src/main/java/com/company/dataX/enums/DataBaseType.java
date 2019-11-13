package com.company.dataX.enums;

import com.alibaba.druid.util.JdbcConstants;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
public enum DataBaseType {
	MYSQL("mysqlreader", "mysqlwriter", JdbcConstants.MYSQL),

	POSTGRESQL("postgresqlreader", "postgresqlwriter", JdbcConstants.POSTGRESQL),

	ORACLE("oraclereader", "oraclewriter", JdbcConstants.ORACLE),

	EMPTY(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);//空字符防止空指针;

	private String reader;
	private String writer;
	private String dbType;

	public static DataBaseType getType(String database) {

		for (DataBaseType type : DataBaseType.values()) {
			if (type.name().equalsIgnoreCase(database)) {
				return type;
			}
		}
		return EMPTY;
	}

	public String getReader() {
		return reader;
	}

	public String getWriter() {
		return writer;
	}

	public String getDbType() {
		return dbType;
	}
}
