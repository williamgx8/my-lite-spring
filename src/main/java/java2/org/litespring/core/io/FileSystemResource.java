package java2.org.litespring.core.io;

import java2.org.litespring.util.ClassUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileSystemResource implements Resource {
    private String path;
    private ClassLoader classLoader;

    public FileSystemResource(String path) {
        this(path, null);
    }

    public FileSystemResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    @Override
    public String getDescription() {
        return path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(path);
    }
}
