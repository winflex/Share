package cc.lixiaohui.share.server.config.loader.rule;

import org.apache.commons.digester3.Digester;

import cc.lixiaohui.share.server.config.DatabaseConfig;
import cc.lixiaohui.share.server.config.util.Property;


/**
 * DatabaseConfig配置规则集
 * 
 * @author lixiaohui
 * @date 2016年10月30日 上午12:37:33
 */
public class DatabaseConfigRuleSet extends RuleSetSupport {

	protected static final String RULE_PROPERTY = RULE_DB + "/" + "property";
	
	@Override
	public void addRuleInstances(Digester digester) {
		digester.addRule(RULE_DB, createObjectCreateRule(DatabaseConfig.class));
		digester.addRule(RULE_DB, createSetPropertiesRule());
        digester.addRule(RULE_DB, createSetNestedPropertiesRule());
        
        digester.addRule(RULE_PROPERTY, createObjectCreateRule(Property.class));
        digester.addRule(RULE_PROPERTY, createSetPropertiesRule());
        digester.addRule(RULE_PROPERTY, createSetNestedPropertiesRule());
        
        digester.addSetNext(RULE_PROPERTY, "addProperty");
        
	}

}
