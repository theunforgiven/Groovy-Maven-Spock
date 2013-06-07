package lt.nsg.gms

import lt.nsg.gms.entity.AnEntity
import org.hibernate.SessionFactory
import org.hibernate.cfg.AvailableSettings
import org.hibernate.cfg.Configuration
import org.hibernate.dialect.H2Dialect
import org.hibernate.tool.hbm2ddl.SchemaExport
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.orm.hibernate4.LocalSessionFactoryBean
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

//@ContextConfiguration(classes = HibernateTestConfiguration)
class HibernateTest extends Specification {
    @Shared
    private SessionFactory sessionFactory

    def setupSpec() {
        def sfb = createSessionFactoryBean(new SingleConnectionDataSource("jdbc:h2:mem:test", true))
        createSchema(sfb.getConfiguration())
        sessionFactory = sfb.getObject()
    }

    def cleanupSpec() {
        sessionFactory.close()
    }

    def "can create session factory"() {
        given:
        def session = sessionFactory.openSession()

        when:
        def tx = session.beginTransaction()
        session.save(new AnEntity("a string"))
        tx.commit()

        then:
        session.createQuery("FROM AnEntity").list().size() == 1
    }

    private static LocalSessionFactoryBean createSessionFactoryBean(DataSource dataSource) {
        def sfb = new LocalSessionFactoryBean()
        def hibernateProperties = new Properties()
        hibernateProperties.setProperty(AvailableSettings.DIALECT, H2Dialect.class.name)
        sfb.setPackagesToScan("lt.nsg.gms.entity")
        sfb.setHibernateProperties(hibernateProperties)
        sfb.setDataSource(dataSource)
        sfb.afterPropertiesSet()
        sfb
    }
    private static void createSchema(Configuration configuration) {
        def export = new SchemaExport(configuration)
        export.create(false, true)
    }
}
