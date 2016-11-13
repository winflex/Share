package cc.lixiaohui.share.core.config.loader.rule;

import org.apache.commons.digester3.Digester;

import cc.lixiaohui.share.core.config.ServerConfig;

/**
 * ServerConfig配置规则集
 * 
 * @author lixiaohui
 * @date 2016年10月30日 上午12:29:38
 */
public class ServerConfigRuleSet extends RuleSetSupport {

	/* 
	 * @see org.apache.commons.digester3.RuleSet#addRuleInstances(org.apache.commons.digester3.Digester)
	 */
	@Override
	public void addRuleInstances(Digester digester) {
		digester.addRule(RULE_SERVER, createObjectCreateRule(ServerConfig.class));
		digester.addRule(RULE_SERVER, createSetPropertiesRule());
        digester.addRule(RULE_SERVER, createSetNestedPropertiesRule());
        
        digester.addRuleSet(new SocketConfigRuleSet());
        digester.addRule(RULE_SOCKET, createSetNextRule("setSocketConfig"));
        
        digester.addRuleSet(new SessionConfigRuleSet());
        digester.addRule(RULE_SESSION, createSetNextRule("setSessionConfig"));
        
        digester.addRuleSet(new DatabaseConfigRuleSet());
        digester.addRule(RULE_DB, createSetNextRule("setDatabaseConfig"));
        
        digester.addRuleSet(new PoolConfigRuleSet());
        digester.addRule(RULE_POOL, createSetNextRule("setPoolConfig"));
        
	}

}
