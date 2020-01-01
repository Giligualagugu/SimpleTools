package com.company.dataX.actionlistener;

import com.alibaba.fastjson.JSON;
import com.company.MainView;
import com.company.dataX.jsonInfo.LogUtils;
import com.company.dataX.jsonInfo.SingleJson;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * @AUTHOR xukele
 * @SINCE 2019/12/30 13:48
 */
public class ExchangeEnvListener implements ActionListener {

	private MainView view;
	private JFileChooser jFileChooser;

	public ExchangeEnvListener(MainView mainView) {
		this.view = mainView;
		jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jFileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
		jFileChooser.setFileFilter(filter);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		inputValided();

		view.getFileList().clear();//读取之前要清空缓存;

		int status = jFileChooser.showDialog(view.getMotherboard(), "批量选择");
		if (status == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = jFileChooser.getSelectedFiles();
			Collections.addAll(view.getFileList(), selectedFiles);
			for (File file : selectedFiles) {
				LogUtils.loginfo(view.getLogs(), "\n导入文件: " + file.getName());
			}
		} else {
			view.getFileList().clear();
		}
		handle();
	}

	private void handle() {
		List<File> fileList = view.getFileList();

		fileList.forEach(e -> {

			byte[] bytes = new byte[0];
			try {
				bytes = Files.readAllBytes(e.toPath());
				SingleJson singleJson = JSON.parseObject(bytes, SingleJson.class);
				String url = view.getUrl().getText().trim();
				String username = view.getUname().getText().trim();
				String pwd = view.getPwd().getText().trim();
				String url2 = view.getUrl2().getText().trim();
				String username2 = view.getUname2().getText().trim();
				String pwd2 = view.getPwd2().getText().trim();

				singleJson.getJob().getContent().forEach(content -> {
					content.getReader().getParameter().setUsername(username);
					content.getReader().getParameter().setPassword(pwd);
					content.getReader().getParameter().getConnection().get(0).setJdbcUrl(Collections.singletonList(url));
					content.getWriter().getParameter().setUsername(username2);
					content.getWriter().getParameter().setPassword(pwd2);
					content.getWriter().getParameter().getConnection().get(0).setJdbcUrl(url2);
				});
				String exchangeStr = JSON.toJSONString(singleJson, true);
				Files.write(e.toPath(), exchangeStr.getBytes(StandardCharsets.UTF_8), TRUNCATE_EXISTING);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		LogUtils.loginfo(view.getLogs(),"环境配置替换完毕");

	}

	private void inputValided() {

		String url = view.getUrl().getText().trim();
		String username = view.getUname().getText().trim();
		String pwd = view.getPwd().getText().trim();

		String url2 = view.getUrl2().getText().trim();
		String username2 = view.getUname2().getText().trim();
		String pwd2 = view.getPwd2().getText().trim();

		if (StringUtils.isAnyBlank(url, url2, username, username2, pwd, pwd2)) {
			LogUtils.loginfo(view.getLogs(), "\nALL url,username,pwd input cannot be empty...");
			throw new RuntimeException("all url,username,pwd input cannot be empty");
		}
	}
}
