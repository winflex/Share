package cc.lixiaohui.share.client;

import org.junit.Test;

import cc.lixiaohui.share.util.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * @author lixiaohui
 * @date 2016年11月16日 下午7:44:49
 */
public class ClientTest {
	
	final IMessageListener l = new IMessageListener() {
		
		@Override
		public void onUnlike(String json) {
			System.out.println(json);
		}
		
		@Override
		public void onShare(String shareJson) {
			System.out.println(shareJson);
		}
		
		@Override
		public void onLike(String json) {
			System.out.println(json);
		}
		
		@Override
		public void onFriendResponse(String json) {
			System.out.println(json);
		}
		
		@Override
		public void onFriendRequest(String json) {
			System.out.println(json);
		}
		
		@Override
		public void onFriendDeleted(String json) {
			System.out.println(json);
		}
		
		@Override
		public void onComment(String json) {
			System.out.println(json);
		}
		
		@Override
		public void onChat(int fromUserId, String content, long sendTime) {
			System.out.println("chat: " + fromUserId + ", " + content + ", " + sendTime);
		}
	};
	
	@Test
	public void connect() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("小王", "xiaowang"); // id = 7
		System.out.println(json);
		
		System.in.read();
	}
	
	@Test
	public void addFriend() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("小明", "xiaoming");//id = 5
		System.out.println(json);
		
		json = client.addFriend(7);
		System.out.println(json);
		
		json = client.getFriends(0, 20);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void deleteFriend() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("小明", "xiaoming"); // id = 5;
		System.out.println(json);
		
		json = client.getFriends(0, 20);
		System.out.println(json);
		
		
		Thread.sleep(2 * 1000);
		json = client.deleteFriend(4);
		System.out.println(json);
		//Thread.sleep(2 * 1000);
		System.in.read();
	}
	@Test
	public void uploadPicture() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("李小辉", "lixiaohui");
		System.out.println(json);
		
		byte[] bytes = FileUtils.getResourceAsBytes("C:\\Users\\lixiaohui\\Desktop\\g1.PNG");
		//int len = bytes.length;
		client.uploadPicture("", bytes);
		System.out.println(json);
		json = client.getPictures(true, new int[]{16});
		System.out.println(json);
		JSONObject o = JSON.parseObject(json);
		JSONArray arr = o.getJSONObject("result").getJSONArray("pictures");
		JSONObject p = arr.getJSONObject(0);
		byte[] byte111 = p.getBytes("bytes");
		//int len2 = byte111.length;
		FileUtils.saveFile("aa.jpg", byte111);
		System.in.read();
	}
	
	@Test
	public void getLikedShares() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("老王", "laowang");
		System.out.println(json);
		
		json = client.getLikedShares(0, 20);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void getCommentedShares() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("小明", "xiaoming");
		System.out.println(json);
		
		json = client.getCommentedShares(0, 20);
		System.out.println(json);
		System.in.read();
	}
	
}
