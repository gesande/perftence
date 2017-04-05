package net.sf.perftence.fluent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import net.sf.perftence.PercentileRequirement;
import net.sf.perftence.PerformanceRequirements;
import net.sf.perftence.fluent.PerformanceRequirementsPojo.PerformanceRequirementsBuilder;

public class PerformanceRequirementsPojoTest {

	@Test
	public void build() {
		final PerformanceRequirementsBuilder builder = PerformanceRequirementsPojo
				.builder();
		assertNotNull(builder);
		final PerformanceRequirements req = builder.max(150).average(100)
				.median(90).percentile90(140).percentile95(145)
				.percentile99(149).throughput(1000).totalTime(10000).build();
		assertEquals(150, req.max());
		assertEquals(100, req.average());
		assertEquals(90, req.median());
		assertEquals(1000, req.throughput());
		assertEquals(10000, req.totalTime());
		final PercentileRequirement[] percentileRequirements = req
				.percentileRequirements();
		assertEquals(3, percentileRequirements.length);
		assertEquals(90, percentileRequirements[0].percentage());
		assertEquals(140, percentileRequirements[0].millis());
		assertEquals(95, percentileRequirements[1].percentage());
		assertEquals(145, percentileRequirements[1].millis());
		assertEquals(99, percentileRequirements[2].percentage());
		assertEquals(149, percentileRequirements[2].millis());
		assertEquals(
				"PerformanceRequirementsPojo [average=100, median=90, max=150, totalTime=10000, throughput=1000, percentileRequirements=[PercentileRequirementPojo [percentage()=90, millis()=140], PercentileRequirementPojo [percentage()=95, millis()=145], PercentileRequirementPojo [percentage()=99, millis()=149]]]",
				req.toString());
	}

}
