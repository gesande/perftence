package net.sf.perftence.reporting.summary.html;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.perftence.reporting.DefaultDoubleFormatter;
import net.sf.perftence.reporting.Statistics;
import net.sf.perftence.reporting.summary.StatisticsSummary;
import net.sf.perftence.reporting.summary.Summary;

public final class HtmlSummary implements Summary<HtmlSummary>,
        StatisticsSummary<HtmlSummary> {
    private final static DefaultDoubleFormatter DF = new DefaultDoubleFormatter();
    private final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private StringBuffer sb;

    public HtmlSummary start() {
        return init().append(htmlStart());
    }

    private HtmlSummary init() {
        this.sb = new StringBuffer();
        return this;
    }

    private static String htmlStart() {
        return "<html>";
    }

    private HtmlSummary append(final String value) {
        buffer().append(value);
        return this;
    }

    public StringBuffer end() {
        endOfLine();
        append(htmlEnd());
        return buffer();
    }

    private static String htmlEnd() {
        return "</html>";
    }

    public HtmlSummary image(final String id) {
        return append("<img src=\"" + id + ".png\" />");
    }

    public HtmlSummary header(final String name) {
        return append(startBold()).append("Test ").append(name)
                .append(endBold());
    }

    @Override
    public HtmlSummary bold(final String value) {
        return append(startBold()).append(value).append(endBold());
    }

    private static String endBold() {
        return "</b>";
    }

    private static String startBold() {
        return "<b>";
    }

    @Override
    public HtmlSummary endOfLine() {
        return append("\n").br();
    }

    public HtmlSummary testReportCreated(final Date date) {
        return append("Test report created   : ").append(format(date));
    }

    private static String format(final Date date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public HtmlSummary br() {
        return append("<br/>");
    }

    public HtmlSummary numberOfInvocations(final long invocations) {
        return append("Number of invocations : ").append(invocations);
    }

    private HtmlSummary append(final long value) {
        buffer().append(value);
        return this;
    }

    private StringBuffer buffer() {
        return this.sb;
    }

    public HtmlSummary totalTime(final long elapsedTime, final String unit) {
        return append("Total time            : ").append(elapsedTime).append(
                unit);
    }

    private static String format(final double value) {
        return DF.format(value);
    }

    @Override
    public HtmlSummary throughput(final double throughput) {
        return append("Throughput                   : ").append(
                format(throughput));
    }

    public HtmlSummary threadCount(final int threadCount) {
        return append("Thread count      : ").append(threadCount);
    }

    public HtmlSummary duration(final int duration) {
        return append("Duration          : ").append(duration);
    }

    @Override
    public HtmlSummary minResponseTime(final long value, final String unit) {
        return append("Min response time     : ").append(value).append(unit);
    }

    @Override
    public HtmlSummary maxResponseTime(final long value, final String unit) {
        return append("Max response time     : ").append(value).append(unit);
    }

    @Override
    public HtmlSummary averageResponseTime(final double mean, final String unit) {
        return append("Avg response time     : ").append(format(mean)).append(
                unit);
    }

    @Override
    public HtmlSummary median(final long median, final String unit) {
        return append("Median                : ").append(median).append(unit);
    }

    @Override
    public HtmlSummary standardDeviation(final double standardDeviation) {
        return append("Standard deviation    : ").append(
                format(standardDeviation));
    }

    @Override
    public HtmlSummary variance(final double variance) {
        return append("Variance              : ").append(format(variance));
    }

    @Override
    public HtmlSummary percentileHeader() {
        return text("Percentiles           : ").endOfLine();
    }

    @Override
    public HtmlSummary note(final String text) {
        return note().text(text).endOfLine();
    }

    public HtmlSummary note() {
        return text("NOTE:");
    }

    @Override
    public HtmlSummary text(final String text) {
        return append(text);
    }

    public HtmlSummary percentiles(final Statistics stat) {
        percentileHeader();
        percentile90(stat);
        percentile("95", stat.percentile95());
        percentile("96", stat.percentile96());
        percentile("97", stat.percentile97());
        percentile("98", stat.percentile98());
        percentile("99", stat.percentile99());
        return this;
    }

    private void percentile90(final Statistics stat) {
        percentile90(stat.percentile90());
    }

    @Override
    public HtmlSummary percentile90(final long percentileValue) {
        return percentile("90", percentileValue);
    }

    @Override
    public HtmlSummary percentile95(final long percentileValue) {
        return percentile("95", percentileValue);
    }

    @Override
    public HtmlSummary percentile96(final long percentileValue) {
        return percentile("96", percentileValue);
    }

    @Override
    public HtmlSummary percentile97(final long percentileValue) {
        return percentile("97", percentileValue);
    }

    @Override
    public HtmlSummary percentile98(final long percentileValue) {
        return percentile("98", percentileValue);
    }

    @Override
    public HtmlSummary percentile99(final long percentileValue) {
        return percentile("99", percentileValue);
    }

    private HtmlSummary percentile(final String percentile,
            final long percentileValue) {
        return tab().append(percentileLineText(percentile))
                .append(percentileValue).append(" ms").endOfLine();
    }

    private static String percentileLineText(final String percentile) {
        return percentile + " % of the requests (" + percentile
                + "percentile) are either under or equal to ";
    }

    private HtmlSummary tab() {
        return append("   ");
    }

    public HtmlSummary successRate(final double successRate) {
        return append("Invocation success rate : ").append(format(successRate))
                .append(" %");
    }
}