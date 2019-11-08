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
	}

	private void showBtnGroups() {

		JPanel jPanel = new JPanel();
		jPanel.setBorder(BorderFactory.createTitledBorder("目标数据库类型"));
		JRadioButton mysqlBtn = new JRadioButton(DataBaseType.MYSQL.name());
		mysqlBtn.setSelected(true);
		JRadioButton postgreBtn = new JRadioButton(DataBaseType.POSTGRESQL.name());
		targetBtnGroup = new ButtonGroup();
		targetBtnGroup.add(mysqlBtn);
		targetBtnGroup.add(postgreBtn);
		jPanel.add(mysqlBtn);
		jPanel.add(postgreBtn);
		//===================================
		JPanel jPanel2 = new JPanel();
		jPanel2.setBorder(BorderFactory.createTitledBorder("源数据库类型"));
		JRadioButton mysqlBtn2 = new JRadioButton(DataBaseType.MYSQL.name());
		mysqlBtn2.setSelected(true);
		JRadioButton postgreBtn2 = new JRadioButton(DataBaseType.POSTGRESQL.name());
		sourceBtnGroup = new ButtonGroup();
		sourceBtnGroup.add(mysqlBtn2);
		sourceBtnGroup.add(postgreBtn2);
		jPanel2.add(mysqlBtn2);
		jPanel2.add(postgreBtn2);

		JPanel parent = new JPanel();

		parent.add(jPanel);
		parent.add(jPanel2);
		parent.setLayout(new GridLayout(2, 1));

		motherboard.getContentPane().add(parent, BorderLayout.WEST);
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

		left.setBorder(BorderFactory.createTitledBorder("表字段(单文件必填)"));

		JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);

		motherboard.add(jSplitPane, BorderLayout.CENTER);
	}

	private void showButtons() {

		JPanel jPanel = new JPanel(new FlowLayout());
		JButton singleJson = new JButton("生成单个");
		JButton importddl = new JButton("导入ddl.sql文件");
		JButton multiJson = new JButton("批量生成");
		JButton clearBtn = new JButton("清理数据");

		JButton testBtn = new JButton("test");

		jPanel.add(singleJson);
		jPanel.add(importddl);
		jPanel.add(multiJson);
		jPanel.add(clearBtn);
		jPanel.add(testBtn);

		motherboard.getContentPane().add(jPanel, BorderLayout.SOUTH);

		singleJson.addActionListener(new SingleJsonListener(this));
		clearBtn.addActionListener(new ClearBtnListener(this));
		importddl.addActionListener(new DDlFileImportListener(this));
		multiJson.addActionListener(new MultiJsonListener(this));

		testBtn.addActionListener(new TestActionListener(this));
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

		JLabel urlLabel2 = new JLabel("目标库:", JLabel.RIGHT);
		url2 = new JTextField();
		JLabel unameLabel2 = new JLabel("用户:", JLabel.RIGHT);
		uname2 = new JTextField();
		JLabel pwdLable2 = new JLabel("密码:", JLabel.RIGHT);
		pwd2 = new JTextField();

		pannel.setLayout(new GridLayout(2, 5));

		pannel.add(urlLabel);
		pannel.add(url);
		pannel.add(unameLabel);
		pannel.add(uname);
		pannel.add(pwdLable);
		pannel.add(pwd);
		pannel.add(urlLabel2);
		pannel.add(url2);
		pannel.add(unameLabel2);
		pannel.add(uname2);
		pannel.add(pwdLable2);
		pannel.add(pwd2);

		pannel.setBorder(BorderFactory.createTitledBorder("公共项"));

		JPanel panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createTitledBorder("单文件必填"));

		JLabel tableLabel = new JLabel("原表名:", JLabel.RIGHT);
		sourceTable = new JTextField();
		JLabel tableLabel2 = new JLabel("现表名:", JLabel.RIGHT);
		targetTable = new JTextField();

		panel2.add(tableLabel);
		panel2.add(sourceTable);
		panel2.add(tableLabel2);
		panel2.add(targetTable);
		panel2.setLayout(new GridLayout(2, 2));

		Box box = Box.createHorizontalBox();

		box.add(pannel);
		box.add(panel2);

		motherboard.add(box, BorderLayout.NORTH);

	}

}
