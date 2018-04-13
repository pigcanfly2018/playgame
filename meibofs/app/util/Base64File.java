package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64File {
    public static void main(String[] args) {
        // 测试从Base64编码转换为图片文件
        String strImg = "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";
        fileDecode(strImg);
        System.out.println(fileEncode(new File("d:\\wangyc.jpg")));
    }

    public static String fileEncode(File file) {
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
    public static byte[] fileDecode(String data) {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes =null;
		try {
			bytes = decoder.decodeBuffer(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        return bytes;
    }
}