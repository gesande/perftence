package net.sf.perftence.reporting.summary;

import java.util.ArrayList;
import java.util.List;

public abstract class PluginBasedSummaryBuilder extends AbstractSummaryBuilder {

	private List<SummaryFieldPlugin<?>> plugins;

	public PluginBasedSummaryBuilder() {
		this.plugins = new ArrayList<>();
	}

	@Override
	protected void fields(final TestSummary summary) {
		for (SummaryFieldPlugin<?> plugin : plugins()) {
			summary.field(plugin.field().build());
		}
	}

	PluginBasedSummaryBuilder register(final SummaryFieldPlugin<?> plugin) {
		plugins().add(plugin);
		return this;
	}

	private List<SummaryFieldPlugin<?>> plugins() {
		return this.plugins;
	}

}
