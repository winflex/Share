package cc.lixiaohui.share.util.future;

public interface IFutureListener<V> {
	
	void operationCompleted(IFuture<V> future) throws Exception;
	
}
