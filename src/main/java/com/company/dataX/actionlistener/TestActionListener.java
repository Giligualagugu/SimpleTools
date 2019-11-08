package com.company.dataX.actionlistener;

import com.company.MainView;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/4 10:03
 */
public class TestActionListener implements ActionListener {

	private MainView view;

	public TestActionListener(MainView mainView) {
		this.view = mainView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		ButtonGroup buttonGroup = view.getSourceBtnGroup();

		Enumeration<AbstractButton> elements = buttonGroup.getElements();

		while (elements.hasMoreElements()) {
			AbstractButton abstractButton = elements.nextElement();
			boolean selected = abstractButton.isSelected();
			System.out.println(selected);
			String text = abstractButton.getText();
			System.out.println(text);
			System.out.println("=====");
		}



	}
}
