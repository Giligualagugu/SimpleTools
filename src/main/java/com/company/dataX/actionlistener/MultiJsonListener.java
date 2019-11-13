package com.company.dataX.actionlistener;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.fastjson.JSON;
import com.company.MainView;
import com.company.dataX.enums.DataBaseType;
import com.company.dataX.jsonInfo.LogUtils;
import com.company.dataX.jsonInfo.SingleJson;
import com.company.dataX.builder.SingleJsonBuilder;
import lombok.AllArgsConstructor;
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
import java.nio.file.Files;
import java.util.*;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/3 14:53
 */
@AllArgsConstructor
public class MultiJsonListener implements ActionListener {

	private MainView view;

	private static final String TABLENAME = "tablename";
	private static final String COLUMNS = "columns";

	/**
	 * 生成多个文件;
	 * <p>
	 * filelist 保存每个ddl.sql文件  来自于源数据库;
	 * 表名和表字段都由此获取;
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		SingleJsonBuilder builder = new SingleJsonBuilder();

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

		List<File> fileList = view.getFileList();

		builder.setSourceUrl(view.getUrl().getText().trim());
		builder.setTargetUrl(view.getUrl2().getText().trim());
		builder.setSourcePwd(view.getPwd().getText().trim());
		builder.setTargetPwd(view.getPwd2().getText().trim());
		builder.setSourceUsername(view.getUname().getText().trim());
		builder.setTargetUseranme(view.getUname2().getText().trim());

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

		try {
			for (File source : fileList) {
				Map<String, Object> tableNamesColumns = parseFileToResult(source, sourceDType);
				String tableName = ((List<String>) tableNamesColumns.get(TABLENAME)).get(0);
				builder.setSourceTable(tableName);
				builder.setTargetTabel(tableName);
				builder.setColumn((List<String>) tableNamesColumns.get(COLUMNS));
				SingleJson singleJson = builder.buildJsonFile();
				String s = JSON.toJSONString(singleJson, true);

				String filename = absolutePath + line + dir + line + "datax_" + tableName + ".json";

				File file = new File(filename);

				OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
				streamWriter.write(s);
				LogUtils.loginfo(view.getLogs(), "生成文件路径:" + filename);
				streamWriter.close();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			LogUtils.loginfo(view.getLogs(), "json文件创建失败:" + e);
		}
		fileList.clear();

	}

	/*
	解析sql  获取 表名 和 列名;
	 */
	private Map<String, Object> parseFileToResult(File source, DataBaseType sourceDType) throws IOException {

		Map<String, Object> result = new HashMap<>();

		List<String> nameList = new ArrayList<>();
		Set<String> columnList = new HashSet<>();

		byte[] bytes = Files.readAllBytes(source.toPath());

		String sql = StringUtils.toEncodedString(bytes, null);

		List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, sourceDType.getDbType(), false);

		for (SQLStatement sqlStatement : sqlStatements) {
			SchemaStatVisitor visitor = getSchemaStatVisitor(sourceDType);
			sqlStatement.accept(visitor);

			Map<TableStat.Name, TableStat> tables = visitor.getTables();

			for (Map.Entry<TableStat.Name, TableStat> statEntry : tables.entrySet()) {
				if (statEntry.getValue().toString().equalsIgnoreCase("Create")) {
					nameList.add(statEntry.getKey().getName());
				} else {
					//当前这句sqlStatement 如果不是建表语句则跳出;
					continue;
				}
			}

			Collection<TableStat.Column> columns = visitor.getColumns();
			for (TableStat.Column column : columns) {
				if (nameList.contains(column.getTable())) {
					columnList.add(column.getName());
				}
			}
		}

		if (nameList.size() > 1) {
			throw new RuntimeException("ddl.sql文件中tableName只允许存在一个");
		}

		List<String> list = new ArrayList<>(columnList.size());
		list.addAll(columnList);

		result.put(TABLENAME, nameList);
		result.put(COLUMNS, list);
		return result;
	}

	private SchemaStatVisitor getSchemaStatVisitor(DataBaseType sourceDType) {
		return new SchemaStatVisitor(sourceDType.getDbType());
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
	}
}
