package com.company.dataX.actionlistener;

import com.alibaba.fastjson.JSON;
import com.company.MainView;
import com.company.dataX.builder.SingleJsonBuilder;
import com.company.dataX.enums.DataBaseType;
import com.company.dataX.jsonInfo.LogUtils;
import com.company.dataX.jsonInfo.SingleJson;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/4 10:03
 */
public class BuildWithJdbcListener implements ActionListener {

	private MainView view;

	public BuildWithJdbcListener(MainView mainView) {
		this.view = mainView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		SingleJsonBuilder builder = new SingleJsonBuilder();

		//获取源数据库类型;
		DataBaseType sourceDType = null;
		Enumeration<AbstractButton> sourceDTypes = view.getSourceBtnGroup().getElements();
		while (sourceDTypes.hasMoreElements()) {
			AbstractButton abstractButton = sourceDTypes.nextElement();
			if (abstractButton.isSelected()) {
				sourceDType = DataBaseType.getType(abstractButton.getText());
				builder.setReader(sourceDType.getReader());
				break;
			}
		}

		Enumeration<AbstractButton> targetDTypes = view.getTargetBtnGroup().getElements();
		while (targetDTypes.hasMoreElements()) {
			AbstractButton abstractButton = targetDTypes.nextElement();
			if (abstractButton.isSelected()) {
				builder.setWriter(DataBaseType.getType(abstractButton.getText()).getWriter());
				break;
			}
		}

		checkParams(builder);
		// 设置两边的数据库路径,用户名和密码;
		builder.setSourceUrl(view.getUrl().getText().trim());
		builder.setTargetUrl(view.getUrl2().getText().trim());
		builder.setSourcePwd(view.getPwd().getText().trim());
		builder.setTargetPwd(view.getPwd2().getText().trim());
		builder.setSourceUsername(view.getUname().getText().trim());
		builder.setTargetUseranme(view.getUname2().getText().trim());

		String targetTableName = view.getTargetTable().getText().trim();

		boolean existTargetTableName = StringUtils.isNotBlank(targetTableName);

		Map<String, List<String>> tablesAndColumns = getTablesAndColumns(builder.getSourceUrl(), builder.getSourceUsername(),
				builder.getSourcePwd(), view.getSourceTable().getText().trim());

		if (tablesAndColumns.isEmpty()) {
			LogUtils.loginfo(view.getLogs(), "未找到表...");
			return;
		}

		String absolutePath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
		String dir = "dataXjsonFiles";
		String system = System.getProperty("os.name");
		String line = "/";
		if (system.toLowerCase().startsWith("windows")) {
			line = "\\";
		}
		File dirs = new File(absolutePath + line + dir);
		if (!dirs.exists()) {
			dirs.mkdir();
		}

		String finalLine = line;

		tablesAndColumns.forEach((key, value) -> {

			builder.setSourceTable(key);
			builder.setTargetTabel(key);
			if (existTargetTableName) {
				builder.setTargetTabel(targetTableName);
			}
			builder.setColumn(value);

			try {
				SingleJson singleJson = builder.buildJsonFile();
				String s = JSON.toJSONString(singleJson, true);
				String filename = absolutePath + finalLine + dir + finalLine + "datax_" + key + ".json";
				File file = new File(filename);
				OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
				streamWriter.write(s);
				LogUtils.loginfo(view.getLogs(), "生成文件路径:" + filename);
				streamWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				LogUtils.loginfo(view.getLogs(), "json文件创建失败:" + e);
			}
		});

		LogUtils.loginfo(view.getLogs(), "------------------------------------");

	}

	//获取源数据库的表名和对应的字段名
	public Map<String, List<String>> getTablesAndColumns(String url, String username, String pwd, String sourceTableName) {
		Connection connection = null;

		if (StringUtils.isBlank(sourceTableName)) {
			sourceTableName = "%";
		}

		Map<String, List<String>> tableInfo = new HashMap<>();
		try {
			connection = DriverManager.getConnection(url, username, pwd);
			DatabaseMetaData metaData = connection.getMetaData();
			String catalog = connection.getCatalog();
			String schema = connection.getSchema();
			ResultSet tables = metaData.getTables(catalog, schema, sourceTableName, null);
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				List<String> columnNamesList = new ArrayList<>();
				//获取所有列名
				ResultSet columns = metaData.getColumns(catalog, schema, tableName, "%");
				while (columns.next()) {
					String column_name = columns.getString("COLUMN_NAME");
					columnNamesList.add(column_name);
				}
				tableInfo.put(tableName, columnNamesList);
			}

			return tableInfo;
		} catch (SQLException sq) {
			sq.printStackTrace();
			LogUtils.loginfo(view.getLogs(), "连接数据库失败:\n" + url + "\n" + username + "\n" + pwd);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return tableInfo;

	}

	public Map<String, List<String>> getTablesAndColumns(String url, String username, String pwd) {
		return getTablesAndColumns(url, username, pwd, "%");
	}

	private void checkParams(SingleJsonBuilder builder) {
		if (view.getFileList().isEmpty()) {
			LogUtils.loginfo(view.getLogs(), "ddl.sql文件未导入");
			return;
		}

		String url = view.getUrl().getText();
		String url2 = view.getUrl2().getText();
		String name = view.getUname().getText();
		String name2 = view.getUname2().getText();
		String pwd = view.getPwd().getText();
		String pwd2 = view.getPwd2().getText();

		if (StringUtils.isAnyBlank(url, url2, name, name2, pwd, pwd2)) {
			LogUtils.loginfo(view.getLogs(), "数据库路径,用户名,密码必填...");
			return;
		}

		if (StringUtils.isBlank(builder.getReader())) {
			LogUtils.loginfo(view.getLogs(), "请选择 源数据库类型");
			return;
		}

		if (StringUtils.isBlank(builder.getWriter())) {
			LogUtils.loginfo(view.getLogs(), "请选择 目标数据库类型");
			return;
		}

		String source = view.getSourceTable().getText();

		String target = view.getTargetTable().getText();

		if (StringUtils.isNotBlank(target) && StringUtils.isBlank(source)) {
			LogUtils.loginfo(view.getLogs(), "目标库的表填写后,来源库的表名必填...");
		}

	}
}
