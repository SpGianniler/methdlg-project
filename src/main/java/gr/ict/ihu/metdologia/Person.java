package gr.ict.ihu.metdologia;


public  class Person {
    public final String firstName;
    public final String lastName;
    public final String eidikotita;
    public final String wresPrwi;
    public final String wresApog;
    public final String wresVrad;
    public final String wresTot;


    public Person(String fName, String lName, String eidik, String wresPrwi, String wresApog, String wresVrad, String wresTot) {
        this.firstName = fName;
        this.lastName = lName;
        this.eidikotita = eidik;
        this.wresPrwi = wresPrwi;
        this.wresApog = wresApog;
        this.wresVrad = wresVrad;
        this.wresTot = wresTot;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEidikotita() {
        return eidikotita;
    }

    public String getWresPrwi() {
        return wresPrwi;
    }

    public String getWresApog() {
        return wresApog;
    }

    public String getWresVrad() {
        return wresVrad;
    }

    public String getWresTot() {
        return wresTot;
    }
}
