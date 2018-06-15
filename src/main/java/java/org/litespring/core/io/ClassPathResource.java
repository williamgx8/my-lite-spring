package java.org.litespring.core.io;

import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource{
    private String path;

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }
}
