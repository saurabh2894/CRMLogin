package Model;

/**
 * Created by Dell on 28-Aug-17.
 */

public class CustomerDetailModel {

    private String CustomerName;

    public CustomerDetailModel(String CustomerName) {
        this.CustomerName = CustomerName;


    }
    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

}
