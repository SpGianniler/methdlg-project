package gr.ict.ihu.metdologia;

public class Ergazomenoi {
    protected String onoma;
    protected String epitheto;
    protected String eidikotita;
    protected String hlikia;
    protected int evWres;

    public Ergazomenoi(String onoma, String epitheto, String eidikotita, String age, int whours) {
        this.onoma = onoma;
        this.epitheto = epitheto;
        this.eidikotita = eidikotita;
        this.hlikia = age;
        this.evWres = whours;
    }
    public Ergazomenoi(){
        this.onoma = "";
        this.epitheto = "";
        this.eidikotita = "";
        this.hlikia = "";
        this.evWres = 0;
    }

    public String getOnoma() {
        return onoma;
    }
    public String getEpitheto() {
        return epitheto;
    }
    public String getEidikotita() {
        return eidikotita;
    }
    public String getHlikia() {
        return hlikia;
    }
    public int getEvWres() {
        return evWres;
    }
}