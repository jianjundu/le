package com.le.jr.am.profit.service.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.le.jr.am.profit.common.util.FileUtil;



@Component
public class PageControl {
	String charsetName = "UTF-8";
	
	// file
	String batchCode;
	BufferedWriter bw4Shell = null;
	
	/** shell存放目录 */
	@Value("${agreement.shell.path}")
	String agreementShellPath = "/upload/ghorder/shells/";
	@Value("${agreement.path}")
	String agreementPath = "/upload/ghorder/agreements/";
	
	public String getFtpLog() {
		return this.batchCode + ".log";
	}
	
	
	
	public String getBatchCode() {
		return batchCode;
	}



	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}



	private String makeBatchCode() {
		long tid = Thread.currentThread().getId();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR);
		int minutes = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.MINUTE);
		int millisecond = cal.get(Calendar.MILLISECOND);
		return "" + tid + year + addZero(month, 2) + addZero(day, 2) + "" + addZero(hour, 2) + addZero(minutes, 2) + addZero(second, 2) + addZero(millisecond, 3);
	}
	
	public static void main(String[] args) {
		System.out.println(new PageControl().makeBatchCode());
	}
	
	private String addZero(int num, int i) {
		if (i == 2) {
			if (num < 10) {
				return "0" + num;
			}
		}
		if (i == 3) {
			if (num < 10) {
				return "00" + num;
			}
			if (num < 100) {
				return "0" + num;
			}
		}
		return "" + num;
	}

	public BufferedWriter initBw4Shell() {
		try {
			this.batchCode = this.makeBatchCode();
			FileUtil.mkdirs(this.agreementPath + this.batchCode);
			bw4Shell = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.agreementShellPath + this.batchCode + ".sh"),
					Charset.forName(charsetName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bw4Shell;
	}
	
	
	public void overBw4Shell() throws IOException {
		try {
			File flagFile = new File(this.agreementShellPath + batchCode + ".f.success");
			flagFile.createNewFile();
			if (null != this.bw4Shell ) {
				
				bw4Shell.flush();
				bw4Shell.close();
				bw4Shell = null;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			
		}
	}
	
}
