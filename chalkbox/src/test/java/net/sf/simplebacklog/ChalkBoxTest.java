package net.sf.simplebacklog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class ChalkBoxTest {

    @SuppressWarnings("static-method")
    @Test
    public void test() throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        ChalkBox box = new ChalkBox();
        sb.append(box.green().write("green")).append("\n");
        sb.append(box.red().write("red")).append("\n");
        sb.append(box.yellow().write("yellow")).append("\n");
        File file = new File("target");
        file.mkdirs();
        new FileOutputStream(new File(file, "checkthisout")).write(sb
                .toString().getBytes());
        System.out.println(sb.toString());
    }
}
