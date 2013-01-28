package net.sf.perftence.reporting;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

public class FileUtilTest {

    @SuppressWarnings("static-method")
    @Test
    public void writeToFile() {
        FileUtil.writeToFile(content(), "target/content");
    }

    @SuppressWarnings("static-method")
    @Test
    public void ensureDirectoryExists() throws FileNotFoundException {
        FileUtil.ensureDirectoryExists(new File("target/daapa",
                "diipa/directory"));
    }

    private static StringBuffer content() {
        return new StringBuffer("content");
    }
}
