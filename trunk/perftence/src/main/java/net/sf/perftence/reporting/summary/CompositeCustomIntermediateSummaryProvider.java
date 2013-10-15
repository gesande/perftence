package net.sf.perftence.reporting.summary;

import java.util.ArrayList;
import java.util.List;

public final class CompositeCustomIntermediateSummaryProvider {

	private final List<CustomIntermediateSummaryProvider> customProviders;

	public CompositeCustomIntermediateSummaryProvider() {
		this.customProviders = new ArrayList<CustomIntermediateSummaryProvider>();
	}

	public CompositeCustomIntermediateSummaryProvider customSummaryProviders(
			final CustomIntermediateSummaryProvider... providers) {
		for (final CustomIntermediateSummaryProvider provider : providers) {
			customProviders().add(provider);
		}
		return this;
	}

	public void intermediateSummary(final TestSummary summary,
			final CustomSummaryFieldProvider customFieldProvider) {
		for (final CustomIntermediateSummaryProvider provider : customProviders()) {
			provider.provideIntermediateSummary(new IntermediateSummary() {

				@Override
				public IntermediateSummary endOfLine() {
					summary.endOfLine();
					return this;
				}

				@Override
				public IntermediateSummary text(final String text) {
					summary.text(text);
					return this;
				}

				@Override
				public IntermediateSummary field(final AdjustedField<?> field) {
					summary.field(customFieldProvider
							.custom(toFieldDefinition(field.name()),
									Object.class).value(field.value()).build());
					return this;
				}

				private FieldDefinition toFieldDefinition(final String name) {
					return new FieldDefinition() {

						@Override
						public String fullName() {
							return name;
						}
					};
				}
			});
		}
	}

	private List<CustomIntermediateSummaryProvider> customProviders() {
		return this.customProviders;
	}

}
