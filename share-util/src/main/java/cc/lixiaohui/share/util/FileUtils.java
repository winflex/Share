package cc.lixiaohui.share.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作工具类
 * 
 * @author lixiaohui
 * @date 2016年11月10日 下午8:11:37
 */
public class FileUtils {
	
	/**
	 * 获取文件的输入流, 先让类加载器去加载文件, 若文件不在classpath下则手动加载
	 * @param path 文件路径
	 * @return 输入流
	 * @throws FileNotFoundException if any exception 
	 */
	@SuppressWarnings("resource")
	public static InputStream getResourceAsStream(String path) throws IOException {
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		} finally {
			if (in == null) {
				in = new FileInputStream(path);
			}
		}
		return in;
	}
	
	/**
	 * 获取文件的字节数组, 先让类加载器去加载文件, 若文件不在classpath下则手动加载
	 * @param path 文件路径
	 * @return 字节数组
	 * @throws IOException if any exception 
	 */
	public static byte[] getResourceAsBytes(String path) throws IOException {
		InputStream in = null;
		try {
			in = getResourceAsStream(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[2048];
			int len;
			while ((len = in.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
		return baos.toByteArray();
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	/**
	 * 保存文件
	 * @param path
	 * @param content
	 * @throws IOException
	 */
	public static void saveFile(String fullpath, byte[] content) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(fullpath);
			out.write(content);
			return;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 保存文件
	 * @param parentPath
	 * @param fileName
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static String saveFile(String parentPath, String fileName, byte[] content) throws IOException {
		String path = concatPath(parentPath, fileName); // full absolute path
		saveFile(path, content);
		return path;
	}
	
	
	/**
	 * 删除文件
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteFile(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			return file.delete();
		}
		return false;
	}
	
	/**
	 * 拼接文件路径
	 * @param relative
	 * @param paths
	 * @return
	 */
	public static String concatPath(boolean relative, String... paths) {
		paths = Objects.requireNonNull(paths);
		if (paths.length == 1) {
			return paths[0];
		}
		String fullPath = paths[0];
		String separator = relative ? "/" : File.separator;
		for (int i = 1; i < paths.length; i++) {
			fullPath += separator + paths[i];
		}
		return fullPath;
	}
	
	public static String concatPath(String... paths) {
		return concatPath(false, paths);
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(concatPath(true, "home", "lixiaohui"));
		//saveFile("D:\\", "a.txt", "等哈".getBytes());
	}
	
}
