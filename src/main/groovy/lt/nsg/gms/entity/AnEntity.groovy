package lt.nsg.gms.entity

import groovy.transform.EqualsAndHashCode

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@EqualsAndHashCode
class AnEntity {
    @Id
    @GeneratedValue
    int id

    String aString

    AnEntity(String aString) {
        this.aString = aString
    }

    AnEntity() {}
}
