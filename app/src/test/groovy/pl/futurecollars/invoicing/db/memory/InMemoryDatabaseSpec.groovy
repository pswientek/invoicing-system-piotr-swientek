package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.AbstractDatabaseSpec
import pl.futurecollars.invoicing.db.Database

class InMemoryDatabaseSpec extends AbstractDatabaseSpec{

    @Override
    Database getDatabaseInstance() {

        return new InMemoryDatabase()
    }
}
