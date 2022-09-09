package pl.futurecollars.invoicing.db.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.IfProfileValue
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.AbstractDatabaseSpec

@SpringBootTest
@IfProfileValue(name = "spring.profiles.active", value = "jpa")
class JpaDatabaseSpec extends AbstractDatabaseSpec {

    @Autowired
    private InvoiceRepository invoiceRepository

    @Override
    Database getDatabaseInstance() {
        assert invoiceRepository != null
        new JpaDatabase(invoiceRepository)
    }

}
