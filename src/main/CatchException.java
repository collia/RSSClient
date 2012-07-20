package main;

import java.io.IOException;
import java.io.PrintStream;

public class CatchException {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Runtime run = Runtime.getRuntime();
		String[] arg = {"java","main.Main"};
		try {
			Process proc = run.exec(arg);
			//System.setOut(new PrintStream(proc.getOutputStream()));
			int ret = proc.waitFor();
			System.out.println(ret);
			//proc.exitValue();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
