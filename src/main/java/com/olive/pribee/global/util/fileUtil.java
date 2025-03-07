package com.olive.pribee.global.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class fileUtil {

	public static String encodeImageToBase64(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			InputStream inputStream = url.openStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("Failed to encode image", e);
		}
	}
}
