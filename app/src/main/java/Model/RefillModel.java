package Model;

/**
 * Created by Dell on 09-Oct-17.
 */

public class RefillModel {

    private String medName,dosage,refillDate,endDate,medicineid;
    boolean check;
    public RefillModel(){
        //reqd. public constructor
    }

    public RefillModel(String medName,String dosage,String refillDate,String endDate,String medicineid,boolean check){
        this.medName=medName;
        this.dosage=dosage;
        this.refillDate=refillDate;
        this.endDate=endDate;
        this.medicineid=medicineid;
        this.check=check;

    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setRefillDate(String refillDate) {
        this.refillDate = refillDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getMedName() {
        return medName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getRefillDate() {
        return refillDate;
    }

    public String getEndDate() {return endDate; }

    public String getMedicineid() {return medicineid; }
}
