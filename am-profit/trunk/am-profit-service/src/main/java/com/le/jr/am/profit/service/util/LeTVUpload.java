package com.le.jr.am.profit.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LeTVUpload {
	
	@Value(value = "${letv.upload.url}")
	String letvUploadUrl;
	
	@Value(value = "${letv.upload.html.url}")
	String letvUploadHtmlUrl;
	
	@PostConstruct
	public void createFileDirectory(){
		leUploadUrl = this.letvUploadUrl;
		leUploadHtmlUrl = this.letvUploadHtmlUrl;
	}
	
	static String leUploadUrl;
	
	static String leUploadHtmlUrl;
	
	public static String upload(File file) {
		FileInputStream fis = null;
		String res = null;
		try {
			fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()]; //上传文件的二进制流
		    fis.read(buffer, 0, buffer.length);
			
			ByteArrayEntity entity = new ByteArrayEntity(buffer);

			HttpClient client = HttpClients.createDefault();

			HttpPost post = new HttpPost(leUploadUrl + "?filename=" + file.getName());//uploadUrl为上文提到的文件服务器的上传接口地址；
			entity.setContentType( "application/octet-stream" );
			post.setEntity(entity);
			HttpResponse response1 = client.execute(post);

			HttpEntity resEntity = response1.getEntity();
			res = EntityUtils. toString(resEntity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	public static String uploadHtml(File file) {
		FileInputStream fis = null;
		String res = null;
		try {
			fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()]; //上传文件的二进制流
		    fis.read(buffer, 0, buffer.length);
			
			ByteArrayEntity entity = new ByteArrayEntity(buffer);

			HttpClient client = HttpClients.createDefault();

			HttpPost post = new HttpPost(leUploadHtmlUrl + "?filename=" + file.getName());//uploadUrl为上文提到的文件服务器的上传接口地址；
			entity.setContentType( "application/octet-stream" );
			post.setEntity(entity);
			HttpResponse response1 = client.execute(post);

			HttpEntity resEntity = response1.getEntity();
			res = EntityUtils. toString(resEntity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		File file = new File("D:\\multiMedia\\photos\\img10.jpg");
		String returnURL = upload(file);
		System.out.println(returnURL);
	}
}
