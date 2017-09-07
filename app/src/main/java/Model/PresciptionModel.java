package Model;

/**
 * Created by hp on 9/4/2017.
 */

public class PresciptionModel {
    private String medName,dosage,refillDate,endDate,medicineid;
    public PresciptionModel(){
        //reqd. public constructor
    }

    public PresciptionModel(String medName,String dosage,String refillDate,String endDate,String medicineid){
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


