package com.company.dataX.jsonInfo;

import javax.swing.*;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/1 16:15
 */
public class LogUtils {
	private static String ENDSTR = "\n";

	public static void loginfo(JTextArea jTextArea, String content) {
		jTextArea.append(content);
		jTextArea.append(ENDSTR);
	}

}
