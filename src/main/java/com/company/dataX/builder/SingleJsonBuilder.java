package com.company.dataX.builder;

import com.company.dataX.jsonInfo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/1 12:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleJsonBuilder {

	private String sourceUrl;
	private String sourceUsername;
	private String sourcePwd;
	private String targetUrl;
	private String targetUseranme;
	private String targetPwd;
	private String sourceTable;
	private String targetTabel;

	private String reader;

	private String writer;

	private List<String> column;

	//构建json对象;
	public SingleJson buildJsonFile() {
		// 源数据库
		SourceDataConnection sourceConnection = new SourceDataConnection();
		sourceConnection.setJdbcUrl(Collections.singletonList(getSourceUrl()));
		sourceConnection.setTable(Collections.singletonList(getSourceTable()));
		SourceDataBaseParam sourceParam = new SourceDataBaseParam();
		sourceParam.setColumn(getColumn());
		sourceParam.setPassword(getSourcePwd());
		sourceParam.setUsername(getSourceUsername());
		List<SourceDataConnection> sConnectionList = new ArrayList<SourceDataConnection>(1);
		sConnectionList.add(sourceConnection);
		sourceParam.setConnection(sConnectionList);

		DataReader dataReader = new DataReader();
		dataReader.setName(getReader());
		dataReader.setParameter(sourceParam);

		//目标数据库
		TargetDataConnection targetConnection = new TargetDataConnection();
		targetConnection.setTable(Collections.singletonList(getTargetTabel()));
		targetConnection.setJdbcUrl(getTargetUrl());
		TargetDataBaseParam targetParam = new TargetDataBaseParam();
		targetParam.setColumn(getColumn());
		targetParam.setPassword(getTargetPwd());
		targetParam.setUsername(getTargetUseranme());
		List<TargetDataConnection> tConnectionList = new ArrayList<>(1);
		tConnectionList.add(targetConnection);
		targetParam.setConnection(tConnectionList);

		DataWriter dataWriter = new DataWriter();
		dataWriter.setName(getWriter());
		dataWriter.setParameter(targetParam);

		Operater operater = new Operater();
		operater.setReader(dataReader);
		operater.setWriter(dataWriter);

		Setting setting = new Setting();
		setting.setSpeed(new Speed("3"));

		Job job = new Job();
		job.setSetting(setting);
		job.setContent(Collections.singletonList(operater));

		return new SingleJson(job);
	}

}
