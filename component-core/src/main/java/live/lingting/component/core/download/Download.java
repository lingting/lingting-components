package live.lingting.component.core.download;

import live.lingting.component.core.exception.DownloadException;

import java.io.File;
import java.io.IOException;

/**
 * @author lingting 2024-01-17 10:09
 */
public interface Download {

	Download start() throws IOException;

	Download await() throws InterruptedException;

	boolean isStart();

	boolean isFinished();

	boolean isSuccess();

	File getFile() throws InterruptedException, DownloadException;

}
