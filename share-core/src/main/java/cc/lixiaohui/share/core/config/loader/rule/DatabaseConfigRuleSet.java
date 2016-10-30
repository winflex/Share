package cc.lixiaohui.share.core.config.loader.rule;

import org.apache.commons.digester3.Digester;

import cc.lixiaohui.share.core.config.DatabaseConfig;

/**
 * DatabaseConfig配置规则集
 * 
 * @author lixiaohui
 * @date 2016年10月30日 上午12:37:33
 */
public class DatabaseConfigRuleSet extends RuleSetSupport {

	/* 
	 * @see org.apache.commons.digester3.RuleSet#addRuleInstances(org.apache.commons.digester3.Digester)
	 */
	@Override
	public void addRuleInstances(Digester digester) {
		digester.addRule(RULE_DB, createObjectCreateRule(DatabaseConfig.class));
		digester.addRule(RULE_DB, createSetPropertiesRule());
        digester.addRule(RULE_DB, createSetNestedPropertiesRule());
        
        digester.addRuleSet(new MyBatisConfigRuleSet());
        digester.addRule(RULE_DB_MYBATIS, createSetNextRule("setMyBatisConfig"));
	}

}
