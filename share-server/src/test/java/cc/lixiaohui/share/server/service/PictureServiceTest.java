package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SystemRuntime;
import cc.lixiaohui.share.server.service.util.ServiceException;

/**
 * @author lixiaohui
 * @date 2016年11月12日 下午9:11:44
 */
public class PictureServiceTest {
	
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	@Test
	public void upload() throws ServiceException {
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 5));
		params.put("suffix", "jpg");
		params.put("bytes", "起始我是我是图片".getBytes());
		PictureService svc = new PictureService(session, params);
		String json =  svc.uploadPicture();
		System.out.println(json);
	}
	@Test
	public void delete () throws ServiceException {
		params.put("pictureId", 9);
		PictureService svc = new PictureService(null, params);
		String json = svc.deletePicture();
		System.out.println(json);
	}
	@Test
	public void getPictures() throws ServiceException {
		params.put("ignoreIfNotExist", false);
		params.put("pictureIds", new int[]{10, 11,88,99});
		PictureService svc = new PictureService(null, params);
		String json = svc.getPictures();
		System.out.println(json);
	}
}
