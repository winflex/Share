package cc.lixiaohui.share.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

import com.alibaba.fastjson.JSONObject;

import cc.lixiaohui.share.protocol.CSCRequestMessage;
import cc.lixiaohui.share.protocol.CSCResponseMessage;
import cc.lixiaohui.share.protocol.CSRequestMessage;
import cc.lixiaohui.share.protocol.CSResponseMessage;
import cc.lixiaohui.share.protocol.Message;
import cc.lixiaohui.share.server.config.ServerConfig;
import cc.lixiaohui.share.server.util.ResponseFuture;
import cc.lixiaohui.share.util.ErrorCode;
import cc.lixiaohui.share.util.JSONUtils;
import cc.lixiaohui.share.util.TimeUtils;
import cc.lixiaohui.share.util.future.IFuture;
import cc.lixiaohui.share.util.future.IFutureListener;

/**
 * 业务处理
 * @author lixiaohui
 * @date 2016年11月8日 下午11:32:40
 */
public class ShareServer extends AbstractShareServer {

	protected ShareServer(ServerConfig config) {
		super(config);
	}

	
	/**
	 * TODO handle {@link CSCRequestMessage}
	 * <pre>
	 * 1.判断当前用户是否登陆, 否错误, 往源客户端回写错误{@link CSCResponseMessage}
	 * 2.判断对方用户是否登陆, 否错误, 往源客户端回写错误{@link CSCResponseMessage}
	 * 3.将{@link CSCRequestMessage}转发给目标客户端, 并把源消息存起来以备接收到目标客户端的CSCResponseMessage时往源客户端回写响应
	 * </pre>
	 */
	public void handleCSCRequest(ChannelHandlerContext ctx, final CSCRequestMessage message) {
		Session session = ctx.channel().attr(SessionManager.ATTR_SESSION).get();
		if (!session.isLogined()) {
			// 未登陆, 回写错误
			Message m = CSCResponseMessage.builder().correlationId(message.getId())
							.responseJson(JSONUtils.newFailureResult("未登录不允许执行此操作", ErrorCode.AUTH, "",  packOriginCSC(message)))
							.responseTime(TimeUtils.currentTimeMillis()).build();
			writeMessage(m, ctx);
			return;
		} 
		
		Session targetSession = sessionManager.getSessionByUserId(message.getToUserId());
		
		// 目标不存在, 回写错误
		if (targetSession == null || !targetSession.isLogined()) {
			Message m = CSCResponseMessage.builder().correlationId(message.getId())
					.responseJson(JSONUtils.newFailureResult("目标用户未登陆", ErrorCode.USER_NOT_FOUND, "",  packOriginCSC(message)))
					.responseTime(TimeUtils.currentTimeMillis()).build();
			writeMessage(m, ctx);
			return;
		}
		// 保存future
		final ResponseFuture future = new ResponseFuture(message, ctx.channel());
		putFuture(message.getId(), future);
		// 转发给目标客户端
		message.setFromUserId(session.getUserId()); // 设置源ID
		writeMessage(message, targetSession.getContext());
		
		newDetectTaskFor(future);
		
		//收到响应后的逻辑
		future.addListener(new IFutureListener<Message>() {
			
			@Override
			public void operationCompleted(IFuture<Message> f) throws Exception {
				if (!(f.getNow() instanceof CSCResponseMessage)) {
					return;
				}
				CSCResponseMessage resp = null;
				if (future.isSuccess()) { // 成功
					resp = (CSCResponseMessage) f.getNow();
					
				} else { // 失败:1.异常失败, 2.超时
					resp = CSCResponseMessage.builder()
							.correlationId(message.getId())
							.responseJson(JSONUtils.newFailureResult("请求超时", ErrorCode.TIMEOUT, ""))
							.responseTime(TimeUtils.currentTimeMillis()).build();
				}
				// 往源客户端回写响应
				writeMessage(resp, future.getOriginChannel());
				
				removeFuture(message.getId());
			}
		});
	}
	

	/**
	 * TODO 找到并设置future为success即可
	 * @param ctx
	 */
	public void handleCSCResponse(ChannelHandlerContext ctx, CSCResponseMessage message) {
		ResponseFuture future = futures.get(message.getId());
		if (future == null) {
			logger.warn("no future correlated to message {}", message);
			return;
		}
		
	}
	
	/**
	 * TODO handle {@link CSRequestMessage}
	 */
	public void handleCSRequest(ChannelHandlerContext ctx, CSRequestMessage message) {
		String key = message.getService() + AbstractShareServer.SP_SPLITER + message.getProcedure();
		Method method = PROCEDURE_MAP.get(key);
		String result = null;
		Session session = ctx.channel().attr(SessionManager.ATTR_SESSION).get();
		Map<String, Object> params = new HashMap<String, Object>(message.getParameterMap());
		if (method != null) {  
			// 反射调用服务 
			try {
				Object serviceInstance = getServiceInstance(method, session, params);
				// service method must produce json
				result = (String) method.invoke(serviceInstance);
			} catch (Exception e) {
				result = JSONUtils.newFailureResult(e.getMessage(), ErrorCode.SERVICE, e);
			}
		} else { 
			// 服务不存在
			result = JSONUtils.newFailureResult("服务不存在", ErrorCode.SERVICE_NOT_FOUND, "");
		}
		// 回写响应
		CSResponseMessage resp = CSResponseMessage.builder()
				.correlationId(message.getId())
				.responseJson(result).build();
		writeMessage(resp, ctx);
	}

	private JSONObject packOriginCSC(CSCRequestMessage message) {
		JSONObject origin = new JSONObject();
		origin.put("toUserId", message.getToUserId());
		origin.put("content", message.getText());
		return origin;
	}
}
