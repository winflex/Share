package cc.lixiaohui.share.server.core;

import cc.lixiaohui.share.server.model.bean.ForbidenWord;

/**
 * @author lixiaohui
 * @date 2016年11月29日 下午7:42:59
 */
public class ForbidenWordFilter {
	
	private ForbidenWordsHolder holder;
	
	public ForbidenWordFilter(ForbidenWordsHolder holder) {
		this.holder = holder;
	}
	
	
	public boolean filter(String content) {
		for (ForbidenWord word : holder.getWords()) {
			if (word.isDeleted()) {
				continue;
			}
			if (content.contains(word.getContent())) {
				return false;
			}
		}
		return true;
	}


	public ForbidenWordsHolder getHolder() {
		return holder;
	}
	
}
