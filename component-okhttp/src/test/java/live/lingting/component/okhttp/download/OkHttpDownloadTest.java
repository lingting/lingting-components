package live.lingting.component.okhttp.download;

import live.lingting.component.core.util.DigestUtils;
import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.RandomUtils;
import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.okhttp.exception.OkHttpDownloadException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2023-12-20 21:17
 */
class OkHttpDownloadTest {

	final String url = "https://mirrors.huaweicloud.com/repository/maven/live/lingting/components/component-validation/0.0.1/component-validation-0.0.1.pom";

	final String filename = "component-validation-0.0.1.pom";

	final String md5 = "2ce519cf7373a533e1fd297edb9ad1c3";

	File file = null;

	@AfterEach
	void after() {
		if (file != null) {
			FileUtils.delete(file);
		}
	}

	@Test
	void resolveFilename() {
		OkHttpDownload download = OkHttpDownload.single(url).build();
		assertEquals(filename, download.filename);
	}

	@Test
	void single() throws IOException, InterruptedException, NoSuchAlgorithmException {
		OkHttpDownload download = OkHttpDownload.single(url)
			.filename(String.format("%d.%s.pom", System.currentTimeMillis(), RandomUtils.nextHex(3)))
			.build();

		assertFalse(download.isStart());
		assertTrue(download.isSuccess());
		assertFalse(download.isFinished());
		assertThrowsExactly(OkHttpDownloadException.class, download::await);

		OkHttpDownload await = download.start().await();

		assertEquals(download, await);
		assertTrue(download.isStart());
		assertTrue(download.isSuccess());
		assertTrue(download.isFinished());

		file = download.getFile();
		try (FileInputStream stream = new FileInputStream(file)) {
			String string = StreamUtils.toString(stream);
			String md5Hex = DigestUtils.md5Hex(string);
			assertEquals(md5, md5Hex);
		}
	}

}
