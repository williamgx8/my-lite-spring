package java2.org.litespring.core.io;

import java2.org.litespring.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private String path;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader cl) {
        this.path = path;
        this.classLoader = (cl == null ? ClassUtils.getDefaultClassLoader() : cl);
    }

    @Override
    public String getDescription() {
        return this.path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = this.classLoader.getResourceAsStream(this.path);

        if (is == null) {
            throw new FileNotFoundException("path = " + this.path + " cannot open");
        }
        return is;
    }
}
