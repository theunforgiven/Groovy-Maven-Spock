import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.TestExecutionListener
import org.springframework.test.context.TestContext

import static PrintIndented.INSTANCE
import groovy.transform.Synchronized

@ContextConfiguration(classes = ContextConfig)
@TestExecutionListeners(listeners = MyTestExecutionListener)
class BaseSpec extends Specification {
    def setup() { INSTANCE.printAndIndent "setup base" }
    def setupSpec() { INSTANCE.printAndIndent "setup base spec" }
    def cleanupSpec(){ INSTANCE.printAndDedent "cleanup base spec" }
    def cleanup(){ INSTANCE.printAndDedent "cleanup base" }

    def "the base test to run"() {
        expect:
        INSTANCE.print "Base Test Executing"
        true
    }
}

class SuperSpec extends BaseSpec {
    def setup() { PrintIndented.INSTANCE.printAndIndent "setup super" }
    def setupSpec() { PrintIndented.INSTANCE.printAndIndent "setup super spec" }
    def cleanupSpec(){ PrintIndented.INSTANCE.printAndDedent "cleanup super spec" }
    def cleanup(){ PrintIndented.INSTANCE.printAndDedent "cleanup super" }

    def "the super test to run"() {
        expect:
        PrintIndented.INSTANCE.print "Super Test Executing"
        true
    }
}


class MyTestExecutionListener implements TestExecutionListener {

    @Override
    void beforeTestClass(TestContext testContext) {
        INSTANCE.printAndIndent  "before test class"
    }

    @Override
    void afterTestClass(TestContext testContext) {
        INSTANCE.printAndDedent  "after test class"
    }

    @Override
    void prepareTestInstance(TestContext testContext) {
        INSTANCE.print  "preparing instance"
    }

    @Override
    void beforeTestMethod(TestContext testContext) {
        INSTANCE.printAndIndent "before test method"
    }

    @Override
    void afterTestMethod(TestContext testContext) {
        INSTANCE.printAndDedent  "after test method"
    }
}

class ContextConfig {

}

class PrintIndented {
    public static final INSTANCE = new PrintIndented()

    private int indentCount = 0;

    void printAndIndent(String message) {
        printMessage(message)
        indentCount++
    }

    void printAndDedent(String message) {
        indentCount--
        printMessage(message)
    }

    void print(String message) {
        printMessage(message)
    }

    private void printMessage(String message) {
        def padding = "".padLeft(indentCount, "-")
        println "${padding}>$message"
    }
}