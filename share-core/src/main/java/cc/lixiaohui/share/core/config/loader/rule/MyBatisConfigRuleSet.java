package cc.lixiaohui.share.core.config.loader.rule;

import org.apache.commons.digester3.Digester;

import cc.lixiaohui.share.core.config.MyBatisConfig;

/**
 * @author lixiaohui
 * @date 2016年10月30日 上午12:40:09
 */
public class MyBatisConfigRuleSet extends RuleSetSupport {

	/* 
	 * @see org.apache.commons.digester3.RuleSet#addRuleInstances(org.apache.commons.digester3.Digester)
	 */
	@Override
	public void addRuleInstances(Digester digester) {
		digester.addRule(RULE_DB_MYBATIS, createObjectCreateRule(MyBatisConfig.class));
		digester.addRule(RULE_DB_MYBATIS, createSetPropertiesRule());
        digester.addRule(RULE_DB_MYBATIS, createSetNestedPropertiesRule());
	}

}
