package com.company.dataX.actionlistener;

import com.company.MainView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/3 14:27
 * <p>
 * 导入文件
 */
public class FileImporter implements ActionListener {

	private MainView view;

	private JFileChooser jFileChooser;

	public FileImporter(MainView mainView) {
		this.view = mainView;
		jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jFileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
		jFileChooser.setFileFilter(filter);
	}

	public void actionPerformed(ActionEvent e) {

		int status = jFileChooser.showDialog(view.getMotherboard(), "批量选择文件");

		if (status == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = jFileChooser.getSelectedFiles();
			Collections.addAll(view.getFileList(), selectedFiles);
			System.out.println(view.getFileList().size());
		} else {
			view.getFileList().clear();
		}

	}
}
