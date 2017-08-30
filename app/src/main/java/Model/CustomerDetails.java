package Model;

/**
 * Created by hp on 8/29/2017.
 */

public class CustomerDetails {
    private String CName;
    private String CPhone;
    public CustomerDetails(String CName, String CPhone) {
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
