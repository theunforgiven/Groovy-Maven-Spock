package lt.nsg.gms

import spock.lang.Specification
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.TestExecutionListener
import org.springframework.test.context.TestContext

import static PrintIndented.INSTANCE
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.slf4j.LoggerFactory
import org.slf4j.Logger

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

class SuperASpec extends BaseSpec {
    def setup() { INSTANCE.printAndIndent "setup super a" }
    def setupSpec() { INSTANCE.printAndIndent "setup super spec a" }
    def cleanupSpec(){ INSTANCE.printAndDedent "cleanup super spec a" }
    def cleanup(){ INSTANCE.printAndDedent "cleanup super a" }

    def "the super test to run"() {
        expect:
        INSTANCE.print "Super Test Executing a"
        true
    }
}

class SuperBSpec extends BaseSpec {
    def setup() { INSTANCE.printAndIndent "setup super b" }
    def setupSpec() { INSTANCE.printAndIndent "setup super spec b" }
    def cleanupSpec(){ INSTANCE.printAndDedent "cleanup super spec b" }
    def cleanup(){ INSTANCE.printAndDedent "cleanup super b" }

    def "the super test to run"() {
        expect:
        INSTANCE.print "Super Test Executing b"
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

@Configuration
class ContextConfig {
    ContextConfig() {
        INSTANCE.print("Context Config Created")
    }

    @Bean
    ContextConfig cc () {
        return this
    }
}

class PrintIndented {
    private final static Logger log = LoggerFactory.getLogger(PrintIndented)
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
        log.info "${padding}>$message"
    }
}