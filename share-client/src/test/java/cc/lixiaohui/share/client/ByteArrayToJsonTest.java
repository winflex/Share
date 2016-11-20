package cc.lixiaohui.share.client;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

/**
 * @author lixiaohui
 * @date 2016年11月3日 下午10:16:49
 */
public class ByteArrayToJsonTest {
	
	@Test
	public void test() throws Exception {
		String path = "C:\\Users\\lixiaohui\\Pictures\\g1.PNG";
		
		FileInputStream fis = new FileInputStream(path);
		byte[] fileBytes = new byte[fis.available()];
		fis.read(fileBytes);
		fis.close();
		
		String pictureString = JSON.toJSONString(fileBytes);
		byte[] b = JSON.parseObject(pictureString, byte[].class);
		
		FileOutputStream fos = new FileOutputStream("C:\\Users\\lixiaohui\\Pictures\\g1_copy.PNG");
		fos.write(b);
		fos.close();
	}
	
	public static void main(String[] args) {
	}
}
