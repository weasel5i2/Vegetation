package net.weasel.Vegetation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFile {

	private File file;
	private String dir = "plugins/Vegetation/";
	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public void loadFile(String name) {
		file = new File(dir + name);
	}

	public void write(String text) {
		BufferedWriter bw;
		Thread t = Thread.currentThread();
		Date date = new Date();

		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write("[" + dateFormat.format(date) + "][" + t.getId() + "] " + text);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException ioe) {
			Vegetation.logOutput("Error while trying to write log file: " + file.getName());
		}
	}
}
