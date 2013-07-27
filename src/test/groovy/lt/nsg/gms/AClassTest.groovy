package lt.nsg.gms

import spock.lang.Specification

class AClassTest extends Specification {
    def "can test a class"() {
        expect:
        new ClassToTest(84).someInt == 84
    }
}
