package java2.org.litespring.test.v1;

import java2.org.litespring.context.support.ClassPathXmlApplicationContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

import java2.org.litespring.context.ApplicationContext;
import java2.org.litespring.util.ClassUtils;

public class ApplicationContextTest {

    @Test
    public void testGetBean(){

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        System.out.println("zhangsan");
//        Object student = context.getBean("student");
//        Assert.assertNotNull(student);
    }

    @Test
    public void testClassPath(){
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        InputStream is = cl.getResourceAsStream("beans.xml");
        System.out.println(is);
    }
}
