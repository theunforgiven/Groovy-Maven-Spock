package lt.nsg.gms

import lt.nsg.gms.scanme.ABean
import lt.nsg.gms.scanme.BBean
import lt.nsg.gms.scanme.MyVerySpecialComponent
import org.springframework.aop.TargetSource
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [SpringBeanProxyTestConfiguration])
class SpringBeanProxyTest extends Specification {
    @Autowired
    ApplicationContext ctx

//    @Autowired
//    ABean aBean
//
    def "resolve"() {
        expect:
        ctx.getBean(BBean)
    }

    def "blah"() {
        given:
        def expected = new ABean("things")
        def counter = 0

        when:
        def bf = {
            counter++;
            return new ABean()
        }
        PicoContainerStatic.abeanFactory = bf
        def actual = ctx.getBean(ABean)
        actual.getWord()
        println("1")
        actual.getWord()
        println("2")
        actual.getWord()
        println("3")
        then:
        counter > 3
        when:
        println(counter)
        PicoContainerStatic.abeanFactory = null
        def second = ctx.getBean(BBean)
        then:
        noExceptionThrown()
        second instanceof BBean
        second.doIt == ""
    }
}

@Configuration
@ComponentScan(value = "lt.nsg.gms.scanme")
public class SpringBeanProxyTestConfiguration {
    @Bean
    PicoAutoProxyFactoryBean picoAutoProxyFactoryBean() {
        new PicoAutoProxyFactoryBean()
    }
}

class PicoAutoProxyFactoryBean extends AbstractAutoProxyCreator {

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        return new Object[0]
    }

    @Override
    protected TargetSource getCustomTargetSource(Class<?> beanClass, String beanName) {
        new PicoTargetSource(beanClass, new PicoContainer())
    }

    @Override
    protected void customizeProxyFactory(ProxyFactory proxyFactory) {
        proxyFactory.proxyTargetClass = true
    }

    @Override
    protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
        return beanClass.isAnnotationPresent(MyVerySpecialComponent)
    }
}
public class PicoContainerStatic {
    public static Closure abeanFactory = null;
}
public class PicoContainer {
    public Object getBean(Class<?> clazz) {
        println(clazz.name)
        if (PicoContainerStatic.abeanFactory != null) {
            return PicoContainerStatic.abeanFactory();
        }
        clazz.newInstance()
    }
}

class PicoTargetSource implements TargetSource {
    private final Class<?> targetClass
    private final PicoContainer picoContainer

    PicoTargetSource(Class<?> clazz, PicoContainer container) {
        this.picoContainer = container
        this.targetClass = clazz;
    }

    Class<?> getTargetClass() {
        return this.targetClass;
    }

    boolean isStatic() {
        return false
    }

    Object getTarget() throws Exception {
        picoContainer.getBean(this.targetClass)
    }

    void releaseTarget(Object target) throws Exception {
        //swallow because pico container will deal with
        //releasing the target bean
    }
}