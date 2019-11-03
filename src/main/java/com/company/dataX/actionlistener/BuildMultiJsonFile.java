package com.company.dataX.actionlistener;

import com.alibaba.fastjson.JSON;
import com.company.MainView;
import com.company.dataX.jsonInfo.LogUtils;
import com.company.dataX.jsonInfo.SingleJson;
import com.company.dataX.single.SingleJsonBuilder;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/3 14:53
 */
@AllArgsConstructor
public class BuildMultiJsonFile implements ActionListener {

	private MainView view;

	/**
	 * 生成多个文件;
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		checkParams();

		List<File> fileList = view.getFileList();

		SingleJsonBuilder builder = new SingleJsonBuilder();
		builder.setSourceUrl(view.getUrl().getText());
		builder.setTargetUrl(view.getUrl2().getText());
		builder.setSourcePwd(view.getPwd().getText());
		builder.setTargetPwd(view.getPwd2().getText());
		builder.setSourceUsername(view.getUname().getText());
		builder.setTargetUseranme(view.getUname2().getText());

		String absolutePath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

		String dir = "dataXjson";
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
				List<String> strings = Files.readAllLines(source.toPath());
				List<String> columns = strings.stream().filter((str) ->
						StringUtils.isNotBlank(str)
				).collect(Collectors.toList());
				builder.setSourceTable(columns.get(0));
				builder.setTargetTabel(columns.get(1));
				builder.setColumn(columns.subList(2, columns.size() - 1));
				SingleJson singleJson = builder.buildJsonFile();
				String s = JSON.toJSONString(singleJson, true);

				String filename = absolutePath + line + dir + line + "datax_" + columns.get(1) + ".json";

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
		LogUtils.loginfo(view.getLogs(), "文件创建成功,输出到桌面文件夹:" + dir);

	}

	private void checkParams() {
		if (view.getFileList().isEmpty()) {
			LogUtils.loginfo(view.getLogs(), "请选择至少一个txt文件");
			return;
		}

		String url = view.getUrl().getText();
		String url2 = view.getUrl2().getText();
		String name = view.getUname().getText();
		String name2 = view.getUname2().getText();
		String pwd = view.getPwd().getText();
		String pwd2 = view.getPwd2().getText();

		if (StringUtils.isAnyBlank(url, url2, name, name2, pwd, pwd2)) {
			LogUtils.loginfo(view.getLogs(), "两个数据库路径,用户名密码必填...");
			return;
		}

	}
}
