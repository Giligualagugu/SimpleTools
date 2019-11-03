package com.company;

import com.company.dataX.actionlistener.BuildMultiJsonFile;
import com.company.dataX.actionlistener.ClearBtnListener;
import com.company.dataX.actionlistener.FileImporter;
import com.company.dataX.actionlistener.SingleJsonListener;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * xukele
 * 2019/11/1 10:55
 */
@Data
public class MainView {

	private JTextArea logs;

	private JFrame motherboard;

	private JTextField url;
	private JTextField uname;
	private JTextField pwd;
	private JTextField url2;
	private JTextField uname2;
	private JTextField pwd2;

	private JTextField sourceTable;
	private JTextField targetTable;

	private JTextArea columns;

	private List<File> fileList = new LinkedList<File>();

	private JFileChooser jFileChooser;

	MainView() {
		initialize();
	}

	private void initialize() {

		motherboard = new JFrame("DataX json creater");

		initElement();
		motherboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		motherboard.setLocationByPlatform(true);
		motherboard.pack();
		motherboard.setVisible(true);
		motherboard.setSize(800, 600);

	}

	private void initElement() {

		/*
			source	数据库地址 用户名 密码
			target	数据库地址 用户名 密码

			public  表名, 表字段;

			button 页面生成json
			button 导入文件
			button 导入生成json
			button 下载模板文件
		 */

		showDataBaseInput();

		showButtons();

		showColums();

	}

	private void showColums() {

		logs = new JTextArea();
		logs.setTabSize(4);

		JScrollPane right = new JScrollPane(logs);
		right.setWheelScrollingEnabled(true);
		right.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		right.setBorder(BorderFactory.createTitledBorder("操作日志"));

		columns = new JTextArea();
		JScrollPane left = new JScrollPane(columns);

		columns.setLineWrap(true);
		left.setWheelScrollingEnabled(true);
		left.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		left.setBorder(BorderFactory.createTitledBorder("表字段"));

		JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);

		motherboard.add(jSplitPane, BorderLayout.CENTER);
	}

	private void showButtons() {

		JPanel jPanel = new JPanel(new FlowLayout());
		JButton singleJson = new JButton("生成单个");
		JButton importTemplate = new JButton("导入文件");
		JButton downLoadFile = new JButton("下载模板");
		JButton multiJson = new JButton("批量生成");
		JButton clearBtn = new JButton("清理数据");

		jPanel.add(singleJson);
		jPanel.add(importTemplate);
		jPanel.add(downLoadFile);
		jPanel.add(multiJson);
		jPanel.add(clearBtn);

		motherboard.getContentPane().add(jPanel, BorderLayout.SOUTH);

		singleJson.addActionListener(new SingleJsonListener(this));
		clearBtn.addActionListener(new ClearBtnListener(this));
		importTemplate.addActionListener(new FileImporter(this));
		multiJson.addActionListener(new BuildMultiJsonFile(this));
	}

	private void showDataBaseInput() {

		JPanel pannel = new JPanel();
		//pannel
		JLabel urlLabel = new JLabel("源数据库:", JLabel.RIGHT);
		url = new JTextField();
		JLabel unameLabel = new JLabel("用户:", JLabel.RIGHT);
		uname = new JTextField();
		JLabel pwdLable = new JLabel("密码:", JLabel.RIGHT);
		pwd = new JTextField();
		JLabel tableLabel = new JLabel("原表名:", JLabel.RIGHT);
		sourceTable = new JTextField();

		JLabel urlLabel2 = new JLabel("目标库:", JLabel.RIGHT);
		url2 = new JTextField();
		JLabel unameLabel2 = new JLabel("用户:", JLabel.RIGHT);
		uname2 = new JTextField();
		JLabel pwdLable2 = new JLabel("密码:", JLabel.RIGHT);
		pwd2 = new JTextField();
		JLabel tableLabel2 = new JLabel("现表名:", JLabel.RIGHT);
		targetTable = new JTextField();

		pannel.setLayout(new GridLayout(2, 6));

		pannel.add(urlLabel);
		pannel.add(url);
		pannel.add(unameLabel);
		pannel.add(uname);
		pannel.add(pwdLable);
		pannel.add(pwd);
		pannel.add(tableLabel);
		pannel.add(sourceTable);

		pannel.add(urlLabel2);
		pannel.add(url2);
		pannel.add(unameLabel2);
		pannel.add(uname2);
		pannel.add(pwdLable2);
		pannel.add(pwd2);
		pannel.add(tableLabel2);
		pannel.add(targetTable);

		pannel.setBorder(BorderFactory.createTitledBorder("数据库参数项"));

		motherboard.add(pannel, BorderLayout.NORTH);

	}

}
