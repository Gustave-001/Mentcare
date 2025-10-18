package mentcare.models;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private String date;
    private String time;
    private String status;
    private String notes;

    // Constructors + getters/setters
    public Appointment() {}
    public int getAppointmentId(){return appointmentId;}
    public void setAppointmentId(int appointmentId){this.appointmentId = appointmentId;}
    public int getPatientId(){return patientId;}
    public void setPatientId(int patientId){this.patientId = patientId;}
    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}
    public String getTime(){return time;}
    public void setTime(String time){this.time = time;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status = status;}
    public String getNotes(){return notes;}
    public void setNotes(String notes){this.notes = notes;}
}
