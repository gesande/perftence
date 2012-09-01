package net.sf.perftence.reporting;

public final class HtmlReportDeployment {

    private HtmlReportDeployment() {
    }

    private static String htmlReportPrefix() {
        return "perftence";
    }

    public static String deploymentDirectory() {
        return System.getProperty("user.dir") + "/target/perftence";
    }

    public static String reportAsHtml(final String id) {
        return htmlReportPrefix() + "-" + id + ".html";
    }
}
