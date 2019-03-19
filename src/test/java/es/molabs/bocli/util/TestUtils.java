package es.molabs.bocli.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestUtils {

    public static String readFile(String resource) throws IOException {
        return IOUtils.toString(TestUtils.class.getResourceAsStream(resource), StandardCharsets.UTF_8);
    }
}
