package cc.lixiaohui.share.core.config.loader.rule;

import org.apache.commons.digester3.Digester;

import cc.lixiaohui.share.core.config.PoolConfig;

/**
 * @author lixiaohui
 * @date 2016年11月10日 下午9:18:23
 */
public class PoolConfigRuleSet extends RuleSetSupport {

	@Override
	public void addRuleInstances(Digester digester) {
		digester.addRule(RULE_POOL, createObjectCreateRule(PoolConfig.class));
		digester.addRule(RULE_POOL, createSetPropertiesRule());
        digester.addRule(RULE_POOL, createSetNestedPropertiesRule());
	}

}
