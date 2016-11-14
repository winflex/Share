package cc.lixiaohui.share.server.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lixiaohui
 * @date 2016年11月13日 下午11:52:23
 */
public class AnnotationUtils {
	
	private static final String CLASS_FILE_SUFFIX = "class";
	
	private static final FileFilter CLASS_FILE_FILTER = new FileFilter() {
		
		@Override
		public boolean accept(File file) {
			return file.getName().endsWith(".class") || file.isDirectory();
		}
	};
	
	/**
	 * 在packageName包系 下搜索所有被注解 annotationClass 标记的类
	 * @param packageName 包名
	 * @param annotationClass 注解class
	 * @return 所有找到的Class
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> findAnnotatedlasses(String packageName, Class<? extends Annotation> annotationClass) throws IOException, ClassNotFoundException {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replace('.', '/'));
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if ("file".equals(url.getProtocol())) {
				findClasses(new File(URLDecoder.decode(url.getFile(), "UTF-8")), packageName, classes, annotationClass);
			} 
		}
		return classes;
	}
	
	/**
	 * 查找指定类的所有被注解 annotationClass 标记的Method
	 * @param clazz 目标class
	 * @param annotationClass 注解class
	 * @return 所有找到的方法
	 */
	public static List<Method> findAnnotatedMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		List<Method> methods = new ArrayList<Method>();
		for (Method method : clazz.getDeclaredMethods()) {
			Annotation anno = method.getAnnotation(annotationClass);
			if (anno == null) {
				continue;
			}
			methods.add(method);
		}
		return methods;	
	}
	
	private static void findClasses(File file, String packageName, Set<Class<?>> container, Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
		for (File f : file.listFiles(CLASS_FILE_FILTER)) {
			if (f.isDirectory()) {
				findClasses(f, packageName + "." + f.getName(), container, annotationClass);
			} else {
				String className = noSuffix(f.getName(), CLASS_FILE_SUFFIX);
				Class<?> klass = Class.forName(packageName + "." + className);
				if (klass.getAnnotation(annotationClass) != null) {
					container.add(klass);
				}
			}
		}
	}
	
	private static String noSuffix(String filename, String suffix) {
		return filename.substring(0, filename.length() - suffix.length() - 1);
	}
	
	public static void main(String[] args) throws Exception {
		Set<Class<?>> classes = findAnnotatedlasses("cc.lixiaohui.share.util", Target.class);
		for (Class<?> c : classes) {
			System.out.println(c.getName());
		}
	}
}
