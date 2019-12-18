package com.company;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * @AUTHOR xukele
 * @SINCE 2019/12/17 15:54
 */
public class KeleTest {

	public static void main(String[] args) throws Exception {
		//使用jdbc获取数据链接并,获取表名,表字段等信息;
		String url = "jdbc:mysql://localhost:3306/keledb?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true";

		Connection connection = DriverManager.getConnection(url, "xukele", "jialele11");

		String catalog = connection.getCatalog();
		String schema = connection.getSchema();

		DatabaseMetaData metaData = connection.getMetaData();
		String productName = metaData.getDatabaseProductName();

		System.out.println(productName);

		ResultSet tables = metaData.getTables(catalog, schema, "%", null);

		while (tables.next()) {
			System.out.println("==================");
			String table_name = tables.getString("TABLE_NAME");
			System.out.println(table_name);
			ResultSet columns = metaData.getColumns(catalog, schema, table_name, "%");

			while (columns.next()) {
				System.out.println(columns.getString("COLUMN_NAME"));
			}

		}

		connection.close();

	}
}
