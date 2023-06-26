package live.lingting.component.web.undertow;

import live.lingting.component.core.thread.AbstractTimer;
import lombok.RequiredArgsConstructor;

import java.io.File;

/**
 * @author lingting 2023-06-26 19:26
 */
@RequiredArgsConstructor
public class ComponentUndertowTimer extends AbstractTimer {

	protected final File dir;

	@Override
	protected void process() throws Exception {
		try {
			if (dir == null || dir.exists()) {
				return;
			}
			dir.mkdirs();
		}
		catch (Exception e) {
			//
		}
	}

}
