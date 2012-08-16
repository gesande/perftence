package net.sf.perftence.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileUtil {
    public static void writeToFile(final StringBuffer sb, final String path) {
        final File outFile = new File(path);
        try {
            ensureDirectoryExists(outFile.getParentFile());
        } catch (FileNotFoundException e) {
            throw newRuntimeException("Couldn't write to file: " + path, e);
        }
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(sb.toString().getBytes());
            fos.flush();
            fos.close();

        } catch (Exception e) {
            throw newRuntimeException("Couldn't write to file: " + path, e);
        }
    }

    public static void ensureDirectoryExists(final File dir)
            throws FileNotFoundException {
        File parent = dir.getParentFile();
        if (!dir.exists()) {
            if (parent == null)
                throw new FileNotFoundException();
            ensureDirectoryExists(parent);
            dir.mkdir();
        }
    }

    public static RuntimeException newRuntimeException(final String message,
            final Throwable t) {
        return new RuntimeException(message, t);
    }

}