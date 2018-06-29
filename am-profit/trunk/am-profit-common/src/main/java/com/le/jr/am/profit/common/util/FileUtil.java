package com.le.jr.am.profit.common.util;

import com.le.jr.trade.publictools.exception.BizException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {








	/**
	 * 删除文件或目录
	 * @param File file a file or a directory
	 * @param boolean deleteSelf when true delete self , other not delete self
	 */
	public static void deleteFiles(File file, boolean deleteSelf) {
		if (file.isDirectory()) {
			File[] fileArr = file.listFiles();
			for (File tmpFile : fileArr) {
				deleteFiles(tmpFile, true);
			}
			if (deleteSelf) {
				file.delete();
			}
		} else if (file.isFile()) {
			file.delete();
		}
	}
	
	/**
	 * 删除文件或目录
	 * @param String path a file string path or a dir string path
	 * @param boolean deleteSelf when true delete self , other not delete self
	 */
	public static void deleteFiles(String path, boolean deleteSelf) {
		deleteFiles(new File(path), deleteSelf);
	}
	
	/**
	 *  删除文件或目录
	 * @param File[] fileArr
	 */
//	public static void deleteFiles(File[] fileArr) {
//		for (File file : fileArr) {
//			deleteFiles(file);
//		}
//	}
	
	/**
	 *  删除文件或目录
	 * @param String[] pathArr
	 */
//	public static void deleteFiles(String[] pathArr) {
//		for (String path : pathArr) {
//			deleteFiles(path);
//		}
//	}
	
	public static boolean mkdirs(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return file.mkdirs();
		}
		return false;
	}
	
	public static void main(String[] args) {
		deleteFiles("D:\\unix111", false);
		deleteFiles("D:\\unix222", true);
	}


	/**
	 * 将URL连接转化为字符串
	 * @param urlStr url地址字符串
	 * @return
	 * @throws Exception
	 */
	public static String urlToString(String urlStr,String charSet)throws Exception{
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream =  conn.getInputStream();  //通过输入流获取html二进制数据
		byte[] data = readInputStream(inStream);        //把二进制数据转化为byte字节数据
		String htmlSource = new String(data,charSet);
		return htmlSource;
	}


	/** *//**
	 * 把二进制流转化为byte字节数组
	 * @param instream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream instream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[]  buffer = new byte[1204];
		int len = 0;
		while ((len = instream.read(buffer)) != -1){
			outStream.write(buffer,0,len);
		}
		instream.close();
		return outStream.toByteArray();
	}


}
