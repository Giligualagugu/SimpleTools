package com.company.dataX.actionlistener;

import com.company.MainView;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * @AUTHOR xukele
 * @SINCE 2019/12/23 10:49
 * <p>
 * 推出时缓存本地信息;
 */
public class CustomWindowListener extends WindowAdapter {
	private MainView view;

	public CustomWindowListener(MainView mainView) {
		this.view = mainView;
	}

	@Override
	public void windowClosing(WindowEvent e) {

		ObjectOutputStream outputStream = null;
		try {
			Map<String, String> cache = view.getCache();
			cache.put("url", view.getUrl().getText().trim());
			cache.put("pwd", view.getPwd().getText().trim());
			cache.put("username", view.getUname().getText().trim());
			cache.put("sourceTable", view.getSourceTable().getText().trim());
			cache.put("url2", view.getUrl2().getText().trim());
			cache.put("pwd2", view.getPwd2().getText().trim());
			cache.put("username2", view.getUname2().getText().trim());
			cache.put("targetTable", view.getTargetTable().getText().trim());
			File dir = new File("/usr/local/share/");
			if (!dir.exists()) dir.mkdirs();
			File file = new File("/usr/local/share/xukele.txt");
			file.setWritable(true);
			outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeObject(cache);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
