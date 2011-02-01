package lt.ltech.numbers.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NumberTest extends TestCase {
    static public Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new AnswerTest());
        return suite;
    }
}
