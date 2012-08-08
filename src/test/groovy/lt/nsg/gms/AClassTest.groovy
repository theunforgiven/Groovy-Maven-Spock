package lt.nsg.gms

import spock.lang.Specification

class AClassTest extends Specification {
    def "can test a class"() {
        expect:
        new ClassToTest(42).someInt == 42
    }
}
