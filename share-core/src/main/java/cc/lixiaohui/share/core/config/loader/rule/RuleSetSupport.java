package cc.lixiaohui.share.core.config.loader.rule;

import org.apache.commons.digester3.BeanPropertySetterRule;
import org.apache.commons.digester3.CallMethodRule;
import org.apache.commons.digester3.CallParamRule;
import org.apache.commons.digester3.ObjectCreateRule;
import org.apache.commons.digester3.Rule;
import org.apache.commons.digester3.RuleSetBase;
import org.apache.commons.digester3.SetNestedPropertiesRule;
import org.apache.commons.digester3.SetNextRule;
import org.apache.commons.digester3.SetPropertiesRule;

/**
 * 提供规则集基本操作
 * 
 * @author lixiaohui
 * @date 2016年10月30日 上午12:17:18
 */
public abstract class RuleSetSupport extends RuleSetBase {
	
	protected static final String RULE_SERVER = "server";
	protected static final String RULE_SOCKET_OPTIONS = RULE_SERVER + "/socketOptions";
	protected static final String RULE_DB = RULE_SERVER + "/db";
	protected static final String RULE_DB_MYBATIS = RULE_DB + "/mybatis";
	
	
	public Rule createObjectCreateRule(Class<?> clazz) {
        return new ObjectCreateRule(clazz);
    }

    public Rule createSetNextRule(String methodName) {
        return new SetNextRule(methodName);
    }

    public Rule createSetPropertiesRule() {
        return new SetPropertiesRule();
    }

    public Rule createCallMethodRule(String methodName, int paramCount) {
        return new CallMethodRule(methodName, paramCount);
    }

    public Rule createCallParamRule(int paramIndex, String attributeName) {
        return new CallParamRule(paramIndex, attributeName);
    }

    protected Rule createBeanPropertySetterRule() {
        return new BeanPropertySetterRule();
    }
    
    public Rule createSetNestedPropertiesRule() {
        SetNestedPropertiesRule rule = new SetNestedPropertiesRule();
        rule.setAllowUnknownChildElements(true);
        return rule;
    }

}
