package gr.ict.ihu.metdologia;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule extends Main {
    public static ArrayList<Vardia> vardia = new ArrayList<>();


    public static ArrayList<Vardia> createSchedule(ArrayList<Ergazomenoi> ergazomenoi) throws IOException, ParseException {
        int imera, periodos, r, counter;
        ListToMap ltm = new ListToMap();
        Ergazomenoi ergazomenois;
        Vardia vrd;
        HashMap hashDep, hashArg;

        ltm.transition(ergazomenoi);
        hashDep = jsonReader.getDepends();
        hashArg = jsonReader.getArgies();

        matrix.popMatrix(ergazomenoi,ergazomenoi.size());
        int count = 0;

        for (imera = 0; imera <= diff; imera++) {
            for(Object date : hashArg.keySet()){
                LocalDate tempDate = LocalDate.parse(date.toString());
                int intDate = (int) ChronoUnit.DAYS.between(Main.dpSchStart.getValue(),tempDate);
                if(imera == intDate){
                    imera = imera + Integer.parseInt((String)hashArg.get(date));
                }
            }
                if (imera != 0 && (imera % 7) == 0) {
                    for (int j = 0; j < matrix.pinakas.length; j++) {
                        matrix.pinakas[j].wresevd = 0;
                    }
                }
                if (count < jsonReader.daysperweek && count >= 0) {
                    matrix.clearPicked();
                    for (RestDay rest : restDayArrayList) {
                        rest.restDay(rest, ergazomenoi, matrix, imera);
                    }
                    matrix.checkSeqDays();
                    for (periodos = 0; periodos < jsonReader.vardies; periodos++) {
                        matrix.check_adeies(ergazomenoi, jsonReader, periodos, restDayArrayList, imera);
                        for (String f : ltm.getFields()) {
                            counter = matrix.setVardTheseis(hashDep, f, periodos);
                            for (int thesi = 0; thesi < counter; thesi++) {
                                r = matrix.findLeastHours(periodos, f);
                                while (!matrix.pinakas[r].picked && matrix.checkHoursTot(r, jsonReader)) {
                                    ergazomenois = matrix.pinakas[r].ergazomenos;
                                    matrix.addHours(periodos, r, jsonReader);
                                    vrd = matrix.placeErg(ergazomenois, imera, periodos);
                                    vardia.add(vrd);
                                }
                            }
                        }
                    }
                } else {
                    count = 7 - jsonReader.daysperweek;
                    if (count > 0) {
                        imera += count - 1;
                        count = -1;
                    } else if (count == 0) {
                        imera--;
                        count = -1;
                    }
                }
                count++;
        }
        return vardia;
    }
}
