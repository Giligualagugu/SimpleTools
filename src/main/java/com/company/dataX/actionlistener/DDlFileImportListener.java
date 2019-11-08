package com.company.dataX.actionlistener;

import com.company.MainView;
import com.company.dataX.jsonInfo.LogUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/3 17:36
 */
public class DDlFileImportListener implements ActionListener {

	private MainView view;

	private JFileChooser jFileChooser;

	public DDlFileImportListener(MainView mainView) {
		this.view = mainView;
		jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jFileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("SQL files", "sql");
		jFileChooser.setFileFilter(filter);
	}

	public void actionPerformed(ActionEvent e) {
		view.getFileList().clear();//读取之前要清空缓存;

		int status = jFileChooser.showDialog(view.getMotherboard(), "批量选择");

		if (status == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = jFileChooser.getSelectedFiles();

			Collections.addAll(view.getFileList(), selectedFiles);

			StringBuilder stringBuilder = new StringBuilder("\n");
			for (File file : selectedFiles) {
				stringBuilder.append(file.getName()).append("\n");
			}
			LogUtils.loginfo(view.getLogs(), "导入文件: " + stringBuilder.toString());
		} else {
			view.getFileList().clear();
		}

	}
}
