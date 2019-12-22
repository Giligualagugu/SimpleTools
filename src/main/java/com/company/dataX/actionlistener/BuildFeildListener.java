package com.company.dataX.actionlistener;

import com.company.MainView;
import com.company.dataX.builder.CustomEntityBuilder;
import com.company.dataX.feilds.SingleFeild;
import com.company.dataX.jsonInfo.LogUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @AUTHOR xukele
 * @SINCE 2019/12/22 20:42
 */
public class BuildFeildListener implements ActionListener {
	private MainView view;

	public BuildFeildListener(MainView mainView) {
		view = mainView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String url = view.getUrl().getText().trim();
		String username = view.getUname().getText().trim();
		String pwd = view.getPwd().getText().trim();
		String tableName = view.getSourceTable().getText().trim();

		List<SingleFeild> tablesAndColumns = getTablesAndColumns(url, username, pwd, tableName);

		CustomEntityBuilder builder = new CustomEntityBuilder(tablesAndColumns);

		String build = builder.build();

		LogUtils.loginfo(view.getLogs(), build);

	}

	//获取源数据库的表名和对应的字段名
	public List<SingleFeild> getTablesAndColumns(String url, String username, String pwd, String sourceTableName) {
		Connection connection = null;

		if (StringUtils.isBlank(sourceTableName)) {
			sourceTableName = "%";
		}

		try {
			connection = DriverManager.getConnection(url, username, pwd);
			DatabaseMetaData metaData = connection.getMetaData();
			String catalog = connection.getCatalog();
			String schema = connection.getSchema();
			ResultSet records = metaData.getColumns(catalog, schema, sourceTableName, "%");
			List<SingleFeild> list = new ArrayList<>(records.getFetchSize());

			while (records.next()) {
				String belongtable = records.getString("TABLE_NAME");
				String column_name = records.getString("COLUMN_NAME");
				int data_type = records.getInt("DATA_TYPE");
				list.add(new SingleFeild(belongtable, column_name, JDBCType.valueOf(data_type)));
			}

			return list;
		} catch (SQLException sq) {
			LogUtils.loginfo(view.getLogs(), "连接数据库失败:\n" + url + "\n" + username + "\n" + pwd + "\n" + sq.getMessage());
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return Collections.emptyList();

	}

}
