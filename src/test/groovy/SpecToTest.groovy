import spock.lang.Specification

class SpecToTest extends Specification {
    def "given a class it wors"() {
        given:
        def tc = new ClassToTest(42)
        when:
        def res = tc.someInt

        then:
        res == 42
    }
}