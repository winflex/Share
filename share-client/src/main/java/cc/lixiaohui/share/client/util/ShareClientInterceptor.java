package cc.lixiaohui.share.client.util;


/**
 * 拦截客户端方法, 
 * @author lixiaohui
 * @date 2016年11月16日 下午4:11:51
 */
/*public class ShareClientInterceptor implements MethodInterceptor {

	private Method implMethod;
	
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Proxy anno = method.getAnnotation(Proxy.class);
		
		if (anno == null) { // not a proxy method
			return proxy.invokeSuper(obj, args);
		} 
		
		String service = anno.service();
		String procedure = anno.procedure();
		Param[] params = anno.params();
		
		// CSRequestMessage
		if (anno.message() == CSRequestMessage.class) { 
			CSRequestMessage message = CSRequestMessage.builder().service(service).procedure(procedure).build();
			//  set parameters
			for (Param param : params) {
				message.getParameterMap().put(param.name(), args[param.index()]);
			}
			if (implMethod == null) {
				implMethod = obj.getClass().getDeclaredMethod("syncSend", new Class<?>[]{Message.class});
				implMethod.setAccessible(true);
			}
			return implMethod.invoke(obj, message);
		}
		return null;
	}

}*/
