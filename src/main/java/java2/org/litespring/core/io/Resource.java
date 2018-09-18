package java2.org.litespring.core.io;

import java.io.IOException;
import java.io.InputStream;

public interface Resource {

    String getDescription();

    InputStream getInputStream() throws IOException;
}
