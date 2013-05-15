package net.sf.perftence.common;

import java.nio.charset.Charset;

import net.sf.perftence.AppendToFileFailed;
import net.sf.perftence.FileUtil;
import net.sf.perftence.WritingFileFailed;
import net.sf.perftence.reporting.TestReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HtmlTestReport implements TestReport {
    static final Logger LOG = LoggerFactory.getLogger(HtmlTestReport.class);

    private final String directory;
    private final FileAppender fileAppender;
    private final ToBytes toBytes;

    private HtmlTestReport(final String reportRootDirectory) {
        this.directory = reportRootDirectory;
        this.toBytes = new ToBytes(Charset.defaultCharset());
        this.fileAppender = new FileAppender(this.toBytes,
                new FileAppendHandler() {
                    @Override
                    public void failed(final String file,
                            final AppendToFileFailed e) {
                        throw newRuntimeException(
                                "Appending data to summary file '" + file
                                        + "' failed", e);
                    }

                    @Override
                    public void ok(String file) {
                        LOG.info("Data to summary file '{}' appended", file);
                    }

                    @Override
                    public void start(String file) {
                        LOG.info("Appending data to summary file '{}' ...",
                                file);
                    }
                });
    }

    public static TestReport withDefaultReportPath() {
        return new HtmlTestReport(System.getProperty("user.dir")
                + "/target/perftence");
    }

    @Override
    public String reportRootDirectory() {
        return this.directory;
    }

    private static String nameFor(final String id) {
        return "perftence" + "-" + id + ".html";
    }

    private String indexFile() {
        return reportRootDirectory() + "/" + "index.html";
    }

    @Override
    public void updateIndexFile(final String id) {
        fileAppender().appendToFile(indexFile(), asHref(id));
    }

    private FileAppender fileAppender() {
        return this.fileAppender;
    }

    private static String asHref(final String id) {
        return "<a href=" + nameFor(id) + ">" + id + "</a><br/>";
    }

    @Override
    public void writeSummary(final String id, final String data) {
        final String path = reportRootDirectory() + "/" + nameFor(id);
        log().debug("Writing summary to: " + path);
        try {
            FileUtil.writeToFile(path, toBytes(data));
        } catch (final WritingFileFailed cause) {
            throw newRuntimeException("Writing summary to '" + path
                    + "'failed!", cause);
        }
        log().debug("Summary successfully written to '" + path + "' ");
    }

    private byte[] toBytes(final String data) {
        return toBytes().convert(data);
    }

    private ToBytes toBytes() {
        return this.toBytes;
    }

    private static RuntimeException newRuntimeException(final String msg,
            final Throwable cause) {
        return new RuntimeException(logError(msg, cause), cause);
    }

    private static String logError(final String msg, final Throwable t) {
        log().error(msg, t);
        return msg;
    }

    private static Logger log() {
        return LOG;
    }
}
