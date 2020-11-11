package gr.ict.ihu.metdologia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JsonReader {
    int vardies;
    int wresAnaEidik;
    int daysperweek;
    HashMap depends, not_working, argies;

    private static int i=0;
    public static ArrayList<Ergazomenoi> ergazomenoi = new ArrayList<Ergazomenoi>();
    private static Ergazomenoi erg;

    public void readFile() throws IOException, ParseException {

        FileReader reader = new FileReader("Workers.json");
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        JSONArray employeeList = (JSONArray) obj;

        Iterator<Object> iterator = employeeList.iterator();
        while (iterator.hasNext()) {
            JSONObject jo = (JSONObject) iterator.next();
            String onoma = (String) jo.get("firstName");
            String epitheto = (String) jo.get("lastName");
            String eidikotita = (String) jo.get("field");
            String hlikia = (String) jo.get("age");
            String evWres = (String) jo.get("whours");
            erg = new Ergazomenoi(onoma, epitheto, eidikotita,hlikia, Integer.parseInt(evWres));
            ergazomenoi.add(erg);
            i++;
        }
    }

    public static ArrayList<Ergazomenoi> getErg(){
        return ergazomenoi;
    }

    public void readDepends() throws IOException, ParseException {

        FileReader reader = new FileReader("Dependancies.json");
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        JSONArray employeeList = (JSONArray) obj;

        Iterator<Object> iterator = employeeList.iterator();
        while (iterator.hasNext()) {
            JSONObject jo = (JSONObject) iterator.next();

            vardies = Integer.parseInt((String) jo.get("ShiftsPerDay"));
            wresAnaEidik = Integer.parseInt((String) jo.get("HoursPerShift"));
            daysperweek = Integer.parseInt((String) jo.get("DaysPerWeek"));

            depends = ((HashMap)jo.get("Depend"));
            not_working = ((HashMap)jo.get("Not_Working"));
            argies = ((HashMap)jo.get("Argies"));
        }
    }

    public int getVardies() {
        return vardies;
    }

    public int getWresAnaEidik() {
        return wresAnaEidik;
    }

    public int getDaysperweek() {
        return daysperweek;
    }

    public HashMap getDepends() {
        return depends;
    }

    public HashMap getNot_working() {
        return not_working;
    }

    public HashMap getArgies() {
        return argies;
    }
}
