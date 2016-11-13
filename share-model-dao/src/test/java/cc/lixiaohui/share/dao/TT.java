package cc.lixiaohui.share.dao;

/**
 * @author lixiaohui
 * @date 2016年11月2日 下午11:26:06
 */
public class TT {
	
	public static void main(String[] args) {
		new Child().say();
	}

}

class Super {
	void say() {
		System.out.println(this.getClass());
	}
}

class Child extends Super {
}
