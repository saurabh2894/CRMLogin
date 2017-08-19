package Model;

/**
 * Created by Dell on 14-Aug-17.
 */



public class MedicineDetail {
    private String MName;
    private int MQuantity;



    public MedicineDetail(String MName, int MQuantity) {
        this.MName = MName;
        this.MQuantity = MQuantity;

    }
    public String getMName() {
        return MName;
    }

    public void setMName(String MName) {
        this.MName = MName;
    }

    public int getMQuantity() {
        return MQuantity;
    }

    public void setMQuantity(int MQuantity) {
        this.MQuantity = MQuantity;
    }



}
