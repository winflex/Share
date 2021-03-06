package cc.lixiaohui.share.client;

import org.junit.Test;

import cc.lixiaohui.share.util.FileUtils;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

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
	
	
	@Test
	public void getPictures() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("小明", "xiaoming");
		System.out.println(json);
		
		json = client.getPictures(true, new int[]{10,11,54,32});
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void getUser() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("小明", "xiaoming");
		System.out.println(json);
		
		json = client.getUser(4);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void getUsers() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("小明", "xiaoming");
		System.out.println(json);
		
		json = client.getUsers(0, 5);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void selfShield() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("小王", "xiaowang");
		System.out.println(json);
		
		json = client.shield(7);
		System.out.println(json);
		
		json = client.unshield(7);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void adminShield() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("李小辉", "lixiaohui");
		System.out.println(json);
		
		json = client.shield(7);
		System.out.println(json);
		
		json = client.unshield(7);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void getForbidenWords() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("李小辉", "lixiaohui");
		System.out.println(json);
		
		json = client.getForbidenWords(0, 20);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void publishShare() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("李小辉", "lixiaohui");
		System.out.println(json);
		
		json = client.publishShare("暴的电影", new int[0]);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void addForbidenWord() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("李小辉", "lixiaohui");
		System.out.println(json);
		
		json = client.addForbidenWord("恶心");
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void deleteForbidenWord() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("李小辉", "lixiaohui");
		System.out.println(json);
		
		json = client.deleteForbidendWord(4);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void getForbidenWord() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("李小辉", "lixiaohui");
		System.out.println(json);
		
		json = client.getDeletedForbidenWords(0, 20);
		System.out.println(json);
		
		json = client.getForbidenWords(0, 20);
		System.out.println(json);
		System.in.read();
	}
	
	@Test
	public void forbidenWordTest() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.login("李小辉", "lixiaohui");
		System.out.println(json);
		
		json = client.addForbidenWord("国歌");
		int id = JSON.parseObject(json).getJSONObject("result").getIntValue("id");
		System.out.println(json);
		
		json = client.getForbidenWords(0, 20);
		System.out.println(json);
		
		json = client.publishShare("我拍的国歌的", new int[0]);
		
		System.out.println(json); // reject
		
		json = client.deleteForbidendWord(id);
		System.out.println(json);
		
		json = client.publishShare("我拍的国歌的", new int[0]);
		System.out.println(json); // ok
		
		json = client.recoverForbidendWord(id);
		System.out.println(json);
		
		json = client.publishShare("我国歌的", new int[0]);
		System.out.println(json); // reject
		
		json = client.deleteForbidendWord(id);
		System.in.read();
	}
	
	@Test
	public void getShares() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.getShares(null, -1, -1, 0, 0, 0, 20, false);
		System.out.println(json);
		
	}
	
	@Test
	public void getShare() throws Exception {
		final IShareClient client = ShareClientFactory.newInstance("", 8888);
		client.start();
		client.addMessageListener(l);
		
		String json = client.getShare(10);
		System.out.println(json);
		
	}
}
