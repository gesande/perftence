package net.sf.völundr.fileio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtil {
    public static void writeToFile(final String path, final byte[] data)
            throws WritingFileFailed {
        final File outFile = new File(path);
        try {
            ensureDirectoryExists(outFile.getParentFile());
            writeToFile(outFile, data);
        } catch (final Throwable cause) {
            throw newWritingFileFailed(path, cause);
        }
    }

    private static void writeToFile(final File outFile, final byte[] data)
            throws FileNotFoundException, IOException {
        writeAndClose(new FileOutputStream(outFile), data);
    }

    private static void writeAndClose(final OutputStream fos, final byte[] data)
            throws IOException {
        try {
            fos.write(data);
            fos.flush();
        } finally {
            fos.close();
        }
    }

    private static WritingFileFailed newWritingFileFailed(final String path,
            final Throwable cause) {
        return new WritingFileFailed("Couldn't write to file '" + path + "' ",
                cause);
    }

    public static void ensureDirectoryExists(final File dir)
            throws FileNotFoundException, DirectoryNotCreatedException {
        final File parent = dir.getParentFile();
        if (!dir.exists()) {
            if (parent == null) {
                throw new FileNotFoundException(
                        "Parent directory didn't exist!");
            }
            ensureDirectoryExists(parent);
            if (!dir.mkdir()) {
                throw new DirectoryNotCreatedException("Directory '"
                        + dir.getName() + "' wasn't created!");
            }
        }
    }

    public static void appendToFile(final String file, final byte[] bytes)
            throws AppendToFileFailed {
        try {
            writeAndClose(new FileOutputStream(file, true), bytes);
        } catch (final Throwable t) {
            throw new AppendToFileFailed("Failed to append to file '" + file
                    + "' ", t);
        }
    }
}