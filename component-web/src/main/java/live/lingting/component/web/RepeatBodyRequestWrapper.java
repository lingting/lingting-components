package live.lingting.component.web;

import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.core.util.SystemUtils;
import lombok.Getter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lingting 2022/9/16 17:00
 */
public class RepeatBodyRequestWrapper extends HttpServletRequestWrapper {

	public static final File TMP_DIR = new File(SystemUtils.tmpDirLingting(), "request");

	@Getter
	private final File bodyFile;

	private final Map<String, String[]> paramsMap;

	@Getter
	private final List<Closeable> closeableList;

	public RepeatBodyRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		if (!TMP_DIR.exists()) {
			TMP_DIR.mkdirs();
		}
		/*
		 * spring web 文件类型参数再 org.springframework.web.bind.ServletRequestDataBinder.bind
		 * 这里处理的. 通过 org.springframework.web.util.WebUtils.getNativeRequest 获取并直接使用
		 * MultipartRequest 进行处理, 并且内部实现使用的是 parts() 原始数据. 不会受request被规范性覆盖的影响. 这个类仅在
		 * LogFilter 中进行实例化, 该 filter 优先级高, 不会覆盖 spring web 的
		 * StandardMultipartHttpServletRequest 实现
		 *
		 * 综上, 支持文件类型请求的body复用. 但是目前的实现是否支持对请求中文件的复用未知, 后续有相应的需求时可以进行研究与实现
		 */
		paramsMap = request.getParameterMap();
		bodyFile = File.createTempFile("repeat", ".tmp", TMP_DIR);
		try (FileOutputStream outputStream = new FileOutputStream(bodyFile)) {
			StreamUtils.write(request.getInputStream(), outputStream);
		}
		closeableList = new ArrayList<>();
	}

	public static RepeatBodyRequestWrapper of(HttpServletRequest request) throws IOException {
		if (!(request instanceof RepeatBodyRequestWrapper)) {
			return new RepeatBodyRequestWrapper(request);
		}
		return (RepeatBodyRequestWrapper) request;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(bodyFile));
		closeableList.add(bufferedReader);
		return bufferedReader;
	}

	@Override
	@SuppressWarnings("java:S2095")
	public ServletInputStream getInputStream() throws IOException {
		FileInputStream stream = new FileInputStream(bodyFile);
		ServletInputStream servletInputStream = new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				//
			}

			@Override
			public int read() throws IOException {
				return stream.read();
			}

			@Override
			public void close() throws IOException {
				stream.close();
			}
		};
		closeableList.add(servletInputStream);
		return servletInputStream;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return paramsMap;
	}

}
