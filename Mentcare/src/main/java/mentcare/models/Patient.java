package mentcare.models;

public class Patient {
    private int patientId;
    private String name;
    private String dob;
    private String gender;
    private String contact;
    private String summary;
    private String lastVisit;

    // Constructors, getters and setters
    public Patient() {}
    public Patient(int id, String name, String dob, String gender, String contact, String summary, String lastVisit) {
        this.patientId = id; this.name = name; this.dob = dob; this.gender = gender;
        this.contact = contact; this.summary = summary; this.lastVisit = lastVisit;
    }
    // getters/setters omitted for brevity — add them in your IDE.
    public int getPatientId(){return patientId;}
    public void setPatientId(int patientId){this.patientId = patientId;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getDob(){return dob;}
    public void setDob(String dob){this.dob = dob;}
    public String getGender(){return gender;}
    public void setGender(String gender){this.gender = gender;}
    public String getContact(){return contact;}
    public void setContact(String contact){this.contact = contact;}
    public String getSummary(){return summary;}
    public void setSummary(String summary){this.summary = summary;}
    public String getLastVisit(){return lastVisit;}
    public void setLastVisit(String lastVisit){this.lastVisit = lastVisit;}
}
