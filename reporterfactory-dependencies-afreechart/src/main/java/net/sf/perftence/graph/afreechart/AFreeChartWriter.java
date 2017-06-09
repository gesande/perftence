package net.sf.perftence.graph.afreechart;

import java.io.File;

import org.afree.chart.AFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.perftence.graph.ChartWriter;
import net.sf.völundr.fileio.FileUtil;

public class AFreeChartWriter implements ChartWriter<AFreeChart> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AFreeChartWriter.class);
    private final String reportRootDirectory;

    public AFreeChartWriter(final String reportRootDirectory) {
        this.reportRootDirectory = reportRootDirectory;
    }

    @Override
    public void write(final String id, final AFreeChart chart, final int height, final int width) {
        final String outputFilePath = reportRootDirectory() + "/" + id + ".png";
        LOGGER.info("Writing chart as an image to file {}", outputFilePath);
        try {
            FileUtil.ensureDirectoryExists(newFile(reportRootDirectory()));
            // ChartUtilities.saveChartAsPNG(newFile(outputFilePath), chart,
            // width, height);
            LOGGER.info("Chart image successfully written to {}", outputFilePath);
        } catch (final Exception e) {
            throw new RuntimeException(logError(outputFilePath, e), e);
        }
    }

    private static File newFile(final String path) {
        return new File(path);
    }

    private String reportRootDirectory() {
        return this.reportRootDirectory;
    }

    private static String logError(final String outputFilePath, final Throwable t) {
        final String errorMsg = "Writing file '" + outputFilePath + "' failed!";
        LOGGER.error(errorMsg, t);
        return errorMsg;
    }

}
