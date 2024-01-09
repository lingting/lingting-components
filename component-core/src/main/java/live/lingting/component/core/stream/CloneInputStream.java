package live.lingting.component.core.stream;

import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 克隆输入流, 可直接读取, 也可以克隆出一个新流然后读取
 * <p>
 * 当直接读取时, 所有行为和文件流一致
 * </p>
 *
 * @author lingting 2024-01-09 15:41
 */
public class CloneInputStream extends InputStream {

	private final File file;

	private final FileInputStream stream;

	public CloneInputStream(File file) throws FileNotFoundException {
		this.file = file;
		this.stream = new FileInputStream(file);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return stream.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return stream.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return stream.skip(n);
	}

	@Override
	public int available() throws IOException {
		return stream.available();
	}

	@Override
	public void close() {
		StreamUtils.close(stream);
	}

	@Override
	public synchronized void mark(int readlimit) {
		stream.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		stream.reset();
	}

	@Override
	public boolean markSupported() {
		return stream.markSupported();
	}

	@Override
	public int read() throws IOException {
		return stream.read();
	}

	public FileInputStream copy() throws FileNotFoundException {
		return new FileInputStream(file);
	}

	public void clear() {
		FileUtils.delete(file);
	}

}
