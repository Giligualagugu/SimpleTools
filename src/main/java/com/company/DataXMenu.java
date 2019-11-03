package com.company;

import javax.swing.*;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/1 10:53
 */
public class DataXMenu {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainView();
			}
		});
	}
}
