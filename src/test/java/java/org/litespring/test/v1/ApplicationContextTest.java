package java.org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.org.litespring.context.ApplicationContext;
import java.org.litespring.context.support.ClassPathXmlApplicationContext;
import java.org.litespring.util.ClassUtils;

public class ApplicationContextTest {

    @Test
    public void testGetBean(){

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Object student = context.getBean("student");
        Assert.assertNotNull(student);
    }

    @Test
    public void testClassPath(){
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        InputStream inputStream = cl.getResourceAsStream("beans.xml");
        System.out.println(inputStream);
    }
}
