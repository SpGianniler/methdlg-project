package gr.ict.ihu.metdologia;

import java.util.ArrayList;

public  class RestDay {
    Ergazomenoi ergazomenos;
    int day, duration;

    public RestDay(Ergazomenoi ergazomenos, int day, int duration) {
        this.ergazomenos = ergazomenos;
        this.day = day;
        this.duration = duration;
    }

    public void restDay(RestDay rest, ArrayList<Ergazomenoi> ergazomenoi, Matrix matrix, int imera){
        if(this.duration!=0){
            for (int i=0 ; i<matrix.pinakas.length; i++){
                if(rest.ergazomenos.equals(matrix.pinakas[i].ergazomenos) && rest.day==imera){
                    matrix.pinakas[i].picked = true;
                    this.duration--;
                    this.day++;
                }
            }
        }

    }
}
