package Model;

/**
 * Created by hp on 10/11/2017.
 */

public class RefillEditModel {
    private String id,medName,dosage;
    private int increment,decrement;
    public RefillEditModel(){

    }
    public RefillEditModel(String id,String medName,String dosage,int increment,int decrement){
        this.id=id;
        this.medName=medName;
        this.dosage=dosage;
        this.increment=increment;
        this.decrement=decrement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getDecrement() {
        return decrement;
    }

    public void setDecrement(int decrement) {
        this.decrement = decrement;
    }
}
