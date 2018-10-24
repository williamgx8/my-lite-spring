package java2.org.litespring.test.v1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTestV1.class,
        BeanFactoryTest.class,
        ResourceTest.class})
public class V1AllTest {
}
