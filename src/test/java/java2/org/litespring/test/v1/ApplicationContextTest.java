package java2.org.litespring.test.v1;

import java2.org.litespring.bean.v1.Student;
import java2.org.litespring.context.support.ClassPathXmlApplicationContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

import java2.org.litespring.context.ApplicationContext;
import java2.org.litespring.util.ClassUtils;

public class ApplicationContextTest {

    @Test
    public void testGetBean() {

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Student student = (Student) context.getBean("student");
        Assert.assertNotNull(student);
        student.setName("zhangsan");
        student.setAge(12);
        System.out.println(student);
    }

    @Test
    public void testClassPath() {
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        InputStream is = cl.getResourceAsStream("beans.xml");
        System.out.println(is);
    }
}
