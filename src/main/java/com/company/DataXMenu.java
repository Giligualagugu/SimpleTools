package com.company;

import javax.swing.*;
import java.io.IOException;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/1 10:53
 */
public class DataXMenu {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainView();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
