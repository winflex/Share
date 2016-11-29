package cc.lixiaohui.share.server.core.config.loader.rule;

import org.apache.commons.digester3.Digester;

import cc.lixiaohui.share.server.core.config.SessionConfig;


/**
 * @author lixiaohui
 * @date 2016年11月7日 下午11:44:56
 */
public class SessionConfigRuleSet extends RuleSetSupport {

	/* 
	 * @see org.apache.commons.digester3.RuleSet#addRuleInstances(org.apache.commons.digester3.Digester)
	 */
	@Override
	public void addRuleInstances(Digester digester) {
		digester.addRule(RULE_SESSION, createObjectCreateRule(SessionConfig.class));
		digester.addRule(RULE_SESSION, createSetPropertiesRule());
        digester.addRule(RULE_SESSION, createSetNestedPropertiesRule());
	}

}
