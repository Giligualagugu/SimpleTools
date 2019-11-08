package com.company.dataX.actionlistener;

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
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@AllArgsConstructor
public class SingleJsonListener implements ActionListener {

	private MainView view;

	public void actionPerformed(ActionEvent e) {
		SingleJsonBuilder builder = new SingleJsonBuilder();

		Enumeration<AbstractButton> sourceDTypes = view.getSourceBtnGroup().getElements();
		while (sourceDTypes.hasMoreElements()) {
			AbstractButton abstractButton = sourceDTypes.nextElement();
			if (abstractButton.isSelected()) {
				builder.setReader(DataBaseType.getType(abstractButton.getText()).getReader());
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

		builder.setSourceUrl(view.getUrl().getText());
		builder.setTargetUrl(view.getUrl2().getText());
		builder.setSourcePwd(view.getPwd().getText());
		builder.setTargetPwd(view.getPwd2().getText());
		builder.setSourceUsername(view.getUname().getText());
		builder.setTargetUseranme(view.getUname2().getText());
		builder.setSourceTable(view.getSourceTable().getText());
		builder.setTargetTabel(view.getTargetTable().getText());

		String text = view.getColumns().getText();
		if (StringUtils.isBlank(text)) {
			LogUtils.loginfo(view.getLogs(), "字段不能为空...");
			return;
		}

		String[] split = text.split("\n");
		List<String> strings = Arrays.asList(split);
		builder.setColumn(strings);
		SingleJson singleJson1 = builder.buildJsonFile();
		String s = JSON.toJSONString(singleJson1, true);

		File homeDirectory = FileSystemView.getFileSystemView().getHomeDirectory();

		String absolutePath = homeDirectory.getAbsolutePath();

		String system = System.getProperty("os.name");
		String line = "/";

		if (system.toLowerCase().startsWith("windows")) {
			line = "\\";
		}
		String filename = absolutePath + line + "datax_" + view.getTargetTable().getText() + ".json";

		try {
			File file = new File(filename);

			OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

			streamWriter.write(s);

			streamWriter.close();

			LogUtils.loginfo(view.getLogs(), "生成文件路径:" + filename);

			streamWriter.close();
		} catch (Exception es) {
			LogUtils.loginfo(view.getLogs(), "生成文件失败:" + es.getMessage());
		}

	}
}
