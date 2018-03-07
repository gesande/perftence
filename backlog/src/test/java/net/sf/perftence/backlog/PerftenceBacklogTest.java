package net.sf.perftence.backlog;

import org.junit.Test;

public class PerftenceBacklogTest {

	@SuppressWarnings("static-method")
	@Test
	public void show() {
		PerftenceBacklogMain.main(new String[] {});
	}

	@SuppressWarnings("static-method")
	@Test
	public void showWaitingForImplementation() {
		BacklogWaitingForImplementation.main(new String[] {});
	}

	@SuppressWarnings("static-method")
	@Test
	public void showFeaturesWaitingForImplementation() {
		BacklogWaitingForImplementation.main(new String[] { "feature" });
	}

}
