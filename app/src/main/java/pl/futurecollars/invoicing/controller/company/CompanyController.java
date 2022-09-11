package pl.futurecollars.invoicing.controller.company;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.company.CompanyRestService;

@RestController
@AllArgsConstructor
public class CompanyController implements CompanyApi {

    private final CompanyRestService restService;

    @Override
    public List<Company> getAll() {
        return restService.getAll();
    }

    @Override
    public long add(@RequestBody Company company) {
        return restService.save(company);
    }

    @Override
    public ResponseEntity<Company> getById(@PathVariable long id) {
        return restService.getById(id);
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable long id) {
        return restService.deleteById(id);
    }

    @Override
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Company company) {
        return restService.update(id, company);
    }

}
