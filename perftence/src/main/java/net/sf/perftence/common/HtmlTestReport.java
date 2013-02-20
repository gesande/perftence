package net.sf.perftence.common;

import java.nio.charset.Charset;

import net.sf.perftence.AppendToFileFailed;
import net.sf.perftence.FileUtil;
import net.sf.perftence.WritingFileFailed;
import net.sf.perftence.reporting.TestReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HtmlTestReport implements TestReport {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(HtmlTestReport.class);

    @Override
    public String directory() {
        return System.getProperty("user.dir") + "/target/perftence";
    }

    private static String nameFor(final String id) {
        return "perftence" + "-" + id + ".html";
    }

    private String indexFile() {
        return directory() + "/" + "index.html";
    }

    @Override
    public void updateIndexFile(final String id) {
        appendToFile(indexFile(), asHref(id));
    }

    private static String asHref(final String id) {
        return "<a href=" + nameFor(id) + ">" + id + "</a><br/>";
    }

    private static void appendToFile(final String file, final String data) {
        log().info("Updating index file '{}' ...", file);
        try {
            FileUtil.appendToFile(file, toBytes(data));
        } catch (final AppendToFileFailed e) {
            throw new RuntimeException(logError("Writing index file '" + file
                    + "' failed", e), e);
        }
        log().info("Index file '{}' updated", file);
    }

    @Override
    public void writeSummary(final String id, final String data) {
        final String path = directory() + "/" + nameFor(id);
        log().debug("Writing summary to: " + path);
        try {
            FileUtil.writeToFile(path, toBytes(data));
        } catch (final WritingFileFailed cause) {
            throw new RuntimeException(logError("Writing summary to '" + path
                    + "'failed!", cause), cause);
        }
        log().debug("Summary successfully written to '" + path + "' ");
    }

    private static byte[] toBytes(final String data) {
        return data.getBytes(Charset.defaultCharset());
    }

    private static String logError(final String msg, final Throwable t) {
        log().error(msg, t);
        return msg;
    }

    private static Logger log() {
        return LOGGER;
    }
}
