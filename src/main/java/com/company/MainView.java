package com.company;

import com.company.dataX.actionlistener.*;
import com.company.dataX.enums.DataBaseType;
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

	private ButtonGroup sourceBtnGroup;
	private ButtonGroup targetBtnGroup;

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
		motherboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		motherboard.setLocationByPlatform(true);
		motherboard.pack();
		motherboard.setSize(800, 600);
		motherboard.setVisible(true);
		initElement();
	}

	private void initElement() {
		showDataBaseInput();
		showButtons();
		showColums();
		showBtnGroups();

		String tips = "1.从源数据库读取表信息并填充,不校验目标库的url, username, password\n" +
				"2.表名不填则获取所有表名\n" +
				"3.目标库的表填写后,源库的表名也必填";
		logs.setText(tips);
	}

	private void showBtnGroups() {

		Box sourceBox = Box.createVerticalBox();
		Box targetBox = Box.createVerticalBox();
		JPanel boxLeft = new JPanel();
		boxLeft.setLayout(new GridLayout(2, 1));
		//boxLeft.add(sourceBox);
		boxLeft.add(targetBox);
		sourceBox.setBorder(BorderFactory.createTitledBorder("来源库类型"));
		targetBox.setBorder(BorderFactory.createTitledBorder("目标库类型"));

		// 来源库
		JRadioButton mysqlBtnS = new JRadioButton(DataBaseType.MYSQL.name());
		mysqlBtnS.setSelected(true);
		JRadioButton postgreBtnS = new JRadioButton(DataBaseType.POSTGRESQL.name());
		JRadioButton oracleBtnS = new JRadioButton(DataBaseType.ORACLE.name());
		sourceBtnGroup = new ButtonGroup();
		sourceBtnGroup.add(mysqlBtnS);
		sourceBtnGroup.add(postgreBtnS);
		sourceBtnGroup.add(oracleBtnS);
		sourceBox.add(mysqlBtnS);
		sourceBox.add(postgreBtnS);
		sourceBox.add(oracleBtnS);

		// 目标库
		JRadioButton mysqlBtnT = new JRadioButton(DataBaseType.MYSQL.name());
		mysqlBtnT.setSelected(true);
		JRadioButton postgreBtnT = new JRadioButton(DataBaseType.POSTGRESQL.name());
		JRadioButton oracleBtnT = new JRadioButton(DataBaseType.ORACLE.name());
		targetBtnGroup = new ButtonGroup();
		targetBtnGroup.add(mysqlBtnT);
		targetBtnGroup.add(postgreBtnT);
		targetBtnGroup.add(oracleBtnT);
		targetBox.add(mysqlBtnT);
		targetBox.add(postgreBtnT);
		targetBox.add(oracleBtnT);

		motherboard.getContentPane().add(boxLeft, BorderLayout.WEST);
	}

	private void showColums() {
		logs = new JTextArea();
		logs.setTabSize(4);
		logs.setLineWrap(true);
		JScrollPane right = new JScrollPane(logs);
		right.setWheelScrollingEnabled(true);
		right.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		right.setBorder(BorderFactory.createTitledBorder("操作日志"));

		columns = new JTextArea();
		columns.setColumns(20);
		JScrollPane left = new JScrollPane(columns);

		columns.setLineWrap(true);
		left.setWheelScrollingEnabled(true);
		left.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		left.setBorder(BorderFactory.createTitledBorder("表字段(单文件必填)"));

		JSplitPane inputArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		//Box inputArea = Box.createHorizontalBox();
//		inputArea.add(left);
//		inputArea.add(right);
		//motherboard.add(inputArea, BorderLayout.CENTER);
		motherboard.add(right, BorderLayout.CENTER);
	}

	private void showButtons() {

		JPanel jPanel = new JPanel(new FlowLayout());
		JButton singleJson = new JButton("生成单个");
		JButton importddl = new JButton("导入ddl.sql文件");
		JButton multiJson = new JButton("批量生成");
		JButton clearBtn = new JButton("清理数据");

		JButton testBtn = new JButton("test");

		JButton makefile = new JButton("生成脚本");

		//jPanel.add(singleJson);
		//jPanel.add(importddl);
		//jPanel.add(multiJson);
		jPanel.add(clearBtn);
		jPanel.add(makefile);
		//jPanel.add(testBtn);

		motherboard.getContentPane().add(jPanel, BorderLayout.SOUTH);

		singleJson.addActionListener(new SingleJsonListener(this));
		clearBtn.addActionListener(new ClearBtnListener(this));
		importddl.addActionListener(new DDlFileImportListener(this));
		multiJson.addActionListener(new MultiJsonListener(this));
		testBtn.addActionListener(new TestActionListener(this));
		makefile.addActionListener(new BuildWithJdbcListener(this));
	}

	private void showDataBaseInput() {

		Box sourceBox = Box.createHorizontalBox();
		Box targetBox = Box.createHorizontalBox();
		Box boxLeft = Box.createVerticalBox();
		boxLeft.add(sourceBox);
		boxLeft.add(targetBox);
		//pannel
		JLabel urlLabel = new JLabel("来源库:", JLabel.RIGHT);
		url = new JTextField();
		JLabel unameLabel = new JLabel("用户:", JLabel.RIGHT);
		uname = new JTextField();
		JLabel pwdLable = new JLabel("密码:", JLabel.RIGHT);
		pwd = new JTextField();
		sourceBox.add(urlLabel);
		sourceBox.add(url);
		sourceBox.add(unameLabel);
		sourceBox.add(uname);
		sourceBox.add(pwdLable);
		sourceBox.add(pwd);

		JLabel urlLabel2 = new JLabel("目标库:", JLabel.RIGHT);
		url2 = new JTextField();
		JLabel unameLabel2 = new JLabel("用户:", JLabel.RIGHT);
		uname2 = new JTextField();
		JLabel pwdLable2 = new JLabel("密码:", JLabel.RIGHT);
		pwd2 = new JTextField();

		targetBox.add(urlLabel2);
		targetBox.add(url2);
		targetBox.add(unameLabel2);
		targetBox.add(uname2);
		targetBox.add(pwdLable2);
		targetBox.add(pwd2);
		boxLeft.setBorder(BorderFactory.createTitledBorder("公共必填项"));

		Box boxRight = Box.createVerticalBox();
		boxRight.setBorder(BorderFactory.createTitledBorder("可选项"));

		Box yuanbox = Box.createHorizontalBox();
		Box nowbox = Box.createHorizontalBox();

		JLabel tableLabel = new JLabel("原表名:", JLabel.RIGHT);
		sourceTable = new JTextField();
		JLabel tableLabel2 = new JLabel("现表名:", JLabel.RIGHT);
		targetTable = new JTextField();

		yuanbox.add(tableLabel);
		yuanbox.add(sourceTable);
		nowbox.add(tableLabel2);
		nowbox.add(targetTable);

		boxRight.add(yuanbox);
		boxRight.add(nowbox);

		Box notrh = Box.createHorizontalBox();
		notrh.add(boxLeft);
		notrh.add(boxRight);
		motherboard.add(notrh, BorderLayout.NORTH);

	}

}
