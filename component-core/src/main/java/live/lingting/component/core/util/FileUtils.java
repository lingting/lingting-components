package live.lingting.component.core.util;

import live.lingting.component.core.constant.FileConstants;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static live.lingting.component.core.constant.FileConstants.POINT;

/**
 * @author lingting
 */
@UtilityClass
public class FileUtils {

	private static final File TEMP_DIR = SystemUtils.tmpDirLingting();

	private static final Map<String, String> MIME_TYPE;

	static {
		MIME_TYPE = new HashMap<>(16);
		MIME_TYPE.put(FileConstants.CSS_END, FileConstants.CSS_MIME);
		MIME_TYPE.put(FileConstants.JS_END, FileConstants.JS_MIME);
		MIME_TYPE.put(FileConstants.APK_END, FileConstants.APK_MIME);
	}

	/**
	 * 文件路径获取文件扩展名
	 * @param path 文件路径
	 * @return java.lang.String
	 */
	public static String getExt(String path) {
		if (!path.contains(POINT)) {
			return null;
		}

		return path.substring(path.lastIndexOf(POINT));
	}

	/**
	 * 根据文件扩展名获得MimeType
	 * @param path 文件路径或文件名
	 * @return MimeType
	 */
	public static String getMimeType(String path) throws IOException {
		String contentType = URLConnection.getFileNameMap().getContentTypeFor(path);
		if (!StringUtils.hasText(contentType)) {
			String ext = getExt(path);
			if (ext != null && MIME_TYPE.containsKey(ext)) {
				return MIME_TYPE.get(ext);
			}
			else {
				return Files.probeContentType(Paths.get(path));
			}
		}

		return contentType;
	}

	/**
	 * 扫描指定路径下所有文件
	 * @param path 指定路径
	 * @param recursive 是否递归
	 * @return java.util.List<java.lang.String>
	 */
	public static List<String> scanFile(String path, boolean recursive) {
		List<String> list = new ArrayList<>();
		File file = new File(path);
		if (!file.exists()) {
			return list;
		}

		if (file.isFile()) {
			list.add(file.getAbsolutePath());
		}
		// 文件夹
		else {
			File[] files = file.listFiles();
			for (File childFile : files) {
				// 如果递归
				if (recursive && childFile.isDirectory()) {
					list.addAll(scanFile(childFile.getAbsolutePath(), true));
				}
				// 是文件
				else if (childFile.isFile()) {
					list.add(childFile.getAbsolutePath());
				}
			}
		}

		return list;
	}

	/**
	 * 创建指定文件夹, 已存在时不会重新创建
	 * @param dir 文件夹.
	 * @throws IOException 创建失败时抛出
	 */
	public static void createDir(File dir) throws IOException {
		if (dir.exists()) {
			return;
		}

		if (!dir.mkdirs()) {
			throw new IOException("文件夹创建失败! 文件夹路径: " + dir.getAbsolutePath());
		}
	}

	/**
	 * 创建指定文件, 已存在时不会重新创建
	 * @param file 文件.
	 * @throws IOException 创建失败时抛出
	 */
	public static void createFile(File file) throws IOException {
		if (file.exists()) {
			return;
		}

		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			throw new IOException("父文件创建失败! 文件路径: " + file.getAbsolutePath());
		}

		if (!file.createNewFile()) {
			throw new IOException("文件创建失败! 文件路径: " + file.getAbsolutePath());
		}
	}

	/**
	 * 创建临时文件
	 */
	public static File createTemp() throws IOException {
		return createTemp("lingting");
	}

	/**
	 * 创建临时文件
	 * @param trait 文件特征
	 * @return 临时文件对象
	 */
	public static File createTemp(String trait) throws IOException {
		return createTemp(trait, TEMP_DIR);
	}

	/**
	 * 创建临时文件
	 * @param trait 文件特征
	 * @param dir 文件存放位置
	 * @return 临时文件对象
	 */
	public static File createTemp(String trait, File dir) throws IOException {
		try {
			createDir(dir);
		}
		catch (IOException e) {
			throw new IOException("临时文件夹创建失败! 文件夹地址: " + dir.getAbsolutePath(), e);
		}

		return File.createTempFile(trait, "tmp", dir);
	}

	public static File createTemp(InputStream in) throws IOException {
		File file = createTemp();

		try (FileOutputStream out = new FileOutputStream(file)) {
			StreamUtils.write(in, out);
		}

		return file;
	}

	/**
	 * 复制文件
	 * @param source 源文件
	 * @param target 目标文件
	 * @param override 如果目标文件已存在是否覆盖
	 * @param options 其他文件复制选项 {@link StandardCopyOption}
	 * @return 目标文件地址
	 */
	public static Path copy(File source, File target, boolean override, CopyOption... options) throws IOException {
		List<CopyOption> list = new ArrayList<>();
		if (override) {
			list.add(StandardCopyOption.REPLACE_EXISTING);
		}

		if (options != null && options.length > 0) {
			list.addAll(Arrays.asList(options));
		}

		return Files.copy(source.toPath(), target.toPath(), list.toArray(new CopyOption[0]));
	}

	public static boolean delete(File file) {
		try {
			Files.delete(file.toPath());
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

}
