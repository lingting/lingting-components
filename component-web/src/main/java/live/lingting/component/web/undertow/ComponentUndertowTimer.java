package live.lingting.component.web.undertow;

import live.lingting.component.core.thread.AbstractTimer;
import live.lingting.component.core.util.FileUtils;
import lombok.RequiredArgsConstructor;

import java.io.File;

/**
 * @author lingting 2023-06-26 19:26
 */
@RequiredArgsConstructor
public class ComponentUndertowTimer extends AbstractTimer {

	protected final File dir;

	@Override
	protected void process() {
		try {
			FileUtils.createDir(dir);
		}
		catch (Exception e) {
			//
		}
	}

}
