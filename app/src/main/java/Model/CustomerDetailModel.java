package Model;

/**
 * Created by Dell on 28-Aug-17.
 */

public class CustomerDetailModel {

    private String CName;
    private String CPhone;
    public CustomerDetailModel(String CName, String CPhone) {
        this.CName = CName;
        this.CPhone = CPhone;

    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public void setCPhone(String CPhone) {
        this.CPhone = CPhone;
    }

    public String getCPhone() {
        return CPhone;
    }

}
