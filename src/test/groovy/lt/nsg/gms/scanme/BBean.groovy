package lt.nsg.gms.scanme

import org.springframework.stereotype.Component

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@MyVerySpecialComponent
class BBean {
    String doIt = ""
}

