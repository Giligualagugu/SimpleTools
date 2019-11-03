package com.company.dataX.actionlistener;

import com.company.MainView;
import lombok.AllArgsConstructor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/1 16:45
 */
@AllArgsConstructor
public class ClearBtnListener implements ActionListener {

	private MainView mainView;

	public void actionPerformed(ActionEvent e) {

		mainView.getColumns().setText(null);
		mainView.getUname().setText(null);
		mainView.getUname2().setText(null);
		mainView.getUrl().setText(null);
		mainView.getUrl2().setText(null);
		mainView.getPwd2().setText(null);
		mainView.getPwd().setText(null);
		mainView.getSourceTable().setText(null);
		mainView.getTargetTable().setText(null);
		mainView.getLogs().setText(null);
		mainView.getFileList().clear();

	}
}
