package gr.ict.ihu.metdologia;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Validity {
    HashMap <String, String> hash = new HashMap<>();
    int countErgJson;
    int wresApoJson;
    int realwres;

    public boolean hoursOK(ArrayList<Ergazomenoi> ergazomenoi, JsonReader jr) throws IOException, ParseException {

        ListToMap ltm = new ListToMap();
        ltm.transition(ergazomenoi);
        hash = jr.getDepends();

        int temp;
        String f;
        for (String i : ltm.getFields()) {
            realwres=0;
            for (Ergazomenoi j : ergazomenoi) {
                if (j.eidikotita.equals(i)) {
                    realwres+=j.evWres;
                }
            }
            temp =0;
            countErgJson = 0;
            for(int var =0 ; var<jr.getVardies();var++) {
                f = i + var;
                for (String key : hash.keySet()) {
                    if (key.equals(f)) {
                        temp = Integer.parseInt(hash.get(key));
                    }
                }
                countErgJson += temp;

            }
            wresApoJson = jr.getDaysperweek() * jr.getWresAnaEidik() * countErgJson;
        }
        if(realwres >= wresApoJson)
            return true;
        else
            return false;
    }

}