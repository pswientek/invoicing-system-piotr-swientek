package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Invoice {

    @ApiModelProperty(value = "Invoice id (generated by application)", required = true, example = "1")
    private int id;

    @ApiModelProperty(value = "Invoice number (assigned by user)", required = true, example = "2022/08/24/0001")
    private String number;
    
    @ApiModelProperty(value = "Date invoice was created", required = true)
    private LocalDate date;

    @ApiModelProperty(value = "Company who bought the product/service", required = true)
    private Company buyer;

    @ApiModelProperty(value = "Company who is selling the product/service", required = true)
    private Company seller;

    @ApiModelProperty(value = "List of products/services", required = true)
    private List<InvoiceEntry> entries;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
