package cc.lixiaohui.share.server.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cc.lixiaohui.share.server.Session;
import cc.lixiaohui.share.server.SystemRuntime;
import cc.lixiaohui.share.server.service.util.ServiceException;

/**
 * @author lixiaohui
 * @date 2016年11月18日 上午10:09:41
 */
public class CommentServiceTest {
	static {
		System.setProperty(SystemRuntime.SERVER_HOME, "E:\\GitRepository\\share\\share-release");
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	@Test
	public void getCommentOfShare() throws ServiceException {
		params.put("shareId", 9);
		params.put("start", 0);
		params.put("limit", 1);
		
		CommentService svc = new CommentService(null, params);
		String json = svc.getComments();
		System.out.println(json);
	}
	
	@Test
	public void deleteComment() throws ServiceException {
		params.put("commentId", 1);
		
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 4));
		
		
		CommentService svc = new CommentService(session, params);
		String json = svc.deleteComment();
		System.out.println(json);
	}
	
	@Test
	public void publishComment() throws ServiceException {
		params.put("shareId", 10);
		params.put("content", "的开始垃圾的咯");
		params.put("toUserId", 10);
		
		Session session = TestUtils.loginSession(TestUtils.newUser(2, 4));
		
		
		CommentService svc = new CommentService(session, params);
		String json = svc.publishComment();
		System.out.println(json);
	}
}
