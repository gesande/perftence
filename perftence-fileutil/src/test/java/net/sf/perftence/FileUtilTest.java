package net.sf.perftence;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

import org.junit.Test;

public class FileUtilTest {

    @SuppressWarnings("static-method")
    @Test
    public void writeToFile() {
        FileUtil.writeToFile("./target/content",
                content().toString().getBytes(Charset.defaultCharset()));
    }

    @SuppressWarnings("static-method")
    @Test
    public void ensureDirectoryExists() throws FileNotFoundException {
        FileUtil.ensureDirectoryExists(new File("./target/daapa",
                "diipa/directory"));
    }

    private static StringBuffer content() {
        return new StringBuffer("content");
    }
}
