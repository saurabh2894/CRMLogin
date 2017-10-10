package Model;

/**
 * Created by Dell on 09-Oct-17.
 */

public class RefillModel {

    private String medName,dosage,refillDate,endDate,medicineid;
    public RefillModel(){
        //reqd. public constructor
    }

    public RefillModel(String medName,String dosage,String refillDate,String endDate,String medicineid){
        this.medName=medName;
        this.dosage=dosage;
        this.refillDate=refillDate;
        this.endDate=endDate;
        this.medicineid=medicineid;

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
