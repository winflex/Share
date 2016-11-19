package cc.lixiaohui.share.server.config.loader.rule;

import org.apache.commons.digester3.Digester;

import cc.lixiaohui.share.server.config.SocketConfig;



/**
 * SocketConfig配置规则集
 * 
 * @author lixiaohui
 * @date 2016年10月30日 上午12:34:10
 */
public class SocketConfigRuleSet extends RuleSetSupport {

	/* 
	 * @see org.apache.commons.digester3.RuleSet#addRuleInstances(org.apache.commons.digester3.Digester)
	 */
	@Override
	public void addRuleInstances(Digester digester) {
		digester.addRule(RULE_SOCKET, createObjectCreateRule(SocketConfig.class));
		digester.addRule(RULE_SOCKET, createSetPropertiesRule());
        digester.addRule(RULE_SOCKET, createSetNestedPropertiesRule());
	}

}
