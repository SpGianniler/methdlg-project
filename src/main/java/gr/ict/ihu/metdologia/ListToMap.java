package gr.ict.ihu.metdologia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ListToMap {

    HashMap <Ergazomenoi, String> HashErg = new HashMap<Ergazomenoi, String>();
    Set<String> fields = new HashSet<String>();

    public void transition(ArrayList<Ergazomenoi> erg){

        for(int i = 0; i < erg.size(); i++ ){
                HashErg.put(erg.get(i), erg.get(i).eidikotita);
                fields.add(erg.get(i).eidikotita);
        }
    }

    public HashMap<Ergazomenoi, String> getHashErg() {
        return HashErg;
    }

    public Set<String> getFields() {
        return fields;
    }

}