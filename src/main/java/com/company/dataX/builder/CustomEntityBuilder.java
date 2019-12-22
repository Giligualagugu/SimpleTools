package com.company.dataX.builder;

import com.company.dataX.feilds.SingleFeild;

import java.sql.JDBCType;
import java.util.*;

/**
 * @AUTHOR xukele
 * @SINCE 2019/12/22 21:22
 */
public class CustomEntityBuilder {

	static String end = "; ";
	static String first = "private ";
	static String nextline = "\n ";
	static String nextTable = "====================";
	private List<SingleFeild> tablesAndColumns;

	public CustomEntityBuilder(List<SingleFeild> tablesAndColumns) {
		this.tablesAndColumns = tablesAndColumns;
	}

	public String build() {

		Map<String, Object> map = new HashMap<>();

		Set<String> tablename = new HashSet<>();
		for (SingleFeild tablesAndColumn : tablesAndColumns) {
			tablename.add(tablesAndColumn.getBelongTable());
		}

		StringBuilder sb = new StringBuilder();

		for (String name : tablename) {
			sb.append(nextline).append(nextTable).append(nextline).append(name).append(nextline).append(nextline);
			for (SingleFeild tablesAndColumn : tablesAndColumns) {
				if (name.equalsIgnoreCase(tablesAndColumn.getBelongTable())) {
					sb.append(generateLine(tablesAndColumn)).append(nextline);
				}
			}
		}

		return sb.toString();
	}

	private String generateLine(SingleFeild tablesAndColumn) {
		String feildname = tablesAndColumn.getFeildname();
		JDBCType jdbcType = tablesAndColumn.getJdbcType();

		String underscoreName = generateName(feildname);

		String javaType = generateType(jdbcType);

		return first + javaType + underscoreName + end;
	}

	private String generateType(JDBCType jdbcType) {

		switch (jdbcType) {
			case INTEGER:
				return "Integer ";
			case CHAR:
				return "String ";
			case VARCHAR:
				return "String ";
			case DECIMAL:
				return "Double ";
			case TIME:
				return "Date ";
			case TIMESTAMP:
				return "Date ";
			case BIGINT:
				return "Long ";
			case BIT:
				return "Integer ";
			case DATE:
				return "Date ";
			case FLOAT:
				return "Float ";
			case BOOLEAN:
				return "Boolean ";
			default:
				return "Undefined ";
		}

	}

	//处理驼峰命名
	private String generateName(String feildname) {
		if (feildname.contains("_")) {
			String[] s = feildname.split("_");
			String name = "";
			for (int i = 0; i < s.length; i++) {
				String s1 = s[i].toLowerCase();
				if (i > 0) {
					s1 = Character.toUpperCase(s1.charAt(0)) + s1.substring(1, s1.length());
				}
				name += s1;
			}
			return name + " ";
		} else {
			return feildname.toLowerCase() + " ";
		}
	}

}
