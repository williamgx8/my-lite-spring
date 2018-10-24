package java2.org.litespring.test;

import java2.org.litespring.test.v1.V1AllTest;
import java2.org.litespring.test.v2.V2AllTests;
import java2.org.litespring.test.v3.V3AllTests;
import java2.org.litespring.test.v4.V4AllTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({V1AllTest.class, V2AllTests.class, V3AllTests.class, V4AllTests.class})
public class AllTests {

}
