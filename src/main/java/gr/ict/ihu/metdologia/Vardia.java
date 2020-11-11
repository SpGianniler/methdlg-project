package gr.ict.ihu.metdologia;

import java.time.LocalDate;

public class Vardia {
    String name;
    String surname;
    String shift;
    String post;
    LocalDate date;

    public Vardia(String name, String surname, String shift, String post, LocalDate day){
        this.name = name;
        this.surname = surname;
        this.shift = shift;
        this.post = post;
        this.date = day;
    }

    public String getName() {return name;}

    public String getSurname() {return surname;}

    public String getShift() {return shift;}

    public String getPost() {
        return post;
    }

    public LocalDate getDate() {
        return date;
    }
}
