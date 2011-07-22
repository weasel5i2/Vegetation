package net.weasel.Vegetation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile {

	private String file;
	private DateFormat dateFormat;

	public void loadFile(String name) {
		file = "plugins/Vegetation/" + name;
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}

	public void write(String text) {
		BufferedWriter bw = null;
		Thread t = Thread.currentThread();
		Date date = new Date();

		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write("[" + dateFormat.format(date) + "][" + t.getId() + "] " + text);
			bw.newLine();
		} catch (IOException ioe) {
			Vegetation.logOutput("Error while trying to write log file: " + file);
		} finally {
			try {
				if (bw != null) {
					bw.flush();
					bw.close();
				}
			} catch (IOException e) {
				Vegetation.logOutput("Error while trying to write log file: " + file);
			}
		}
	}
}
