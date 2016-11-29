package cc.lixiaohui.share.server.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.lixiaohui.share.server.model.bean.ForbidenWord;
import cc.lixiaohui.share.server.model.dao.ForbidenWordDao;
import cc.lixiaohui.share.server.model.dao.impl.ForbidenWordDaoImpl;
import cc.lixiaohui.share.server.model.dao.util.DaoException;
import cc.lixiaohui.share.util.lifecycle.AbstractLifeCycle;
import cc.lixiaohui.share.util.lifecycle.LifeCycleException;

/**
 * @author lixiaohui
 * @date 2016年11月29日 下午7:27:54
 */
public class ForbidenWordsHolder extends AbstractLifeCycle{
	
	private volatile Map<Integer, ForbidenWord> wordMap = new ConcurrentHashMap<Integer, ForbidenWord>();
	
	private static final Logger logger = LoggerFactory.getLogger(ForbidenWordsHolder.class);
	
	@Override
	protected void initInternal() throws LifeCycleException {
		ForbidenWordDao dao = new ForbidenWordDaoImpl();
		List<ForbidenWord> words = null;
		try {
			words = dao.list();
		} catch (DaoException e) {
			throw new LifeCycleException(e);
		}
		for (ForbidenWord word : words) {
			wordMap.put(word.getId(), word);
		}
		logger.debug("total {} forbiden words loaded", words.size());
	}
	
	public void addWord(ForbidenWord word) {
		wordMap.put(word.getId(), word);
	}
	
	public ForbidenWord remove(int id) {
		return wordMap.remove(id);
	}
	
	public boolean unableWord(int id) {
		ForbidenWord word = wordMap.get(id);
		if (word == null) {
			logger.debug("word[id={}] not found", id);
			return false;
		}
		word.setDeleted(true);
		return true;
	}
	
	public boolean enableWord(int id) {
		ForbidenWord word = wordMap.get(id);
		if (word == null) {
			logger.debug("word[id={}] not found", id);
			return false;
		}
		word.setDeleted(false);
		return true;
	}
	
	public Collection<ForbidenWord> getWords() {
		return wordMap.values();
	}
}
