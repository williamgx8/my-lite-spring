package java2.org.litespring.test.v4;

import java2.org.litespring.core.io.Resource;
import java2.org.litespring.core.io.support.PackageResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class PackageResourceLoaderTest {
    @Test
    public void testGetResources() throws IOException {
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("java2.org.litespring.dao.v4");
        Assert.assertEquals(2, resources.length);

    }
}
