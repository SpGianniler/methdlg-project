package gr.ict.ihu.metdologia;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static gr.ict.ihu.metdologia.Main.*;

public class Matrix {
    Ergazomenoi ergazomenos;
    int wresPrwi;
    int wresApogeyma;
    int wresVradu;
    int wresevd;
    int wresTot;
    int seqDays; //συνεχόμενες μέρες που δούλεψε
    boolean picked;
    public Matrix[] pinakas;
    ArrayList<Ergazomenoi> erg = new ArrayList <>();

    public Matrix(int size) {//size = ergazomenoi.size()
        pinakas = new Matrix[size];
    }

    public Matrix(Ergazomenoi ergazomenos, int wresPrwi, int wresApogeyma, int wresVradu) {
        this.ergazomenos = ergazomenos;
        this.wresPrwi = wresPrwi;
        this.wresApogeyma = wresApogeyma;
        this.wresVradu = wresVradu;
        this.seqDays = 0;
        this.wresevd = this.wresPrwi + this.wresApogeyma + this.wresVradu;
        this.wresTot = this.wresPrwi + this.wresApogeyma + this.wresVradu;
        this.picked = false;
    }

    public void popMatrix(ArrayList<Ergazomenoi> erg, int size){
        for(int i=0;i<size;i++){
            Matrix mtr = new Matrix(erg.get(i),0,0,0);
            this.pinakas[i]=mtr;
        }
    }

    public int getWres(int var){//Επιστρέφει τις ώρες του εργαζομένου στη βάρδια που θέλουμε
            if(var == 0)
                return wresPrwi;
            else if(var == 1)
                return wresApogeyma;
            else
                return wresVradu;
    }

    public void addHours(int var, int i, JsonReader jsonReader){ //todo: να καλείται μέσα στην placeErg
            if(var == 0){
                this.pinakas[i].wresPrwi += jsonReader.wresAnaEidik; // o arithmos vardies einai 3
            }
            else if(var == 1){
                this.pinakas[i].wresApogeyma += jsonReader.wresAnaEidik;
            }
            else if(var == 2){
                this.pinakas[i].wresVradu += jsonReader.wresAnaEidik;
            }
            this.pinakas[i].wresevd += jsonReader.wresAnaEidik;
            this.pinakas[i].wresTot += jsonReader.wresAnaEidik;
            this.pinakas[i].seqDays ++;
            this.pinakas[i].picked = true;

    }

    //επιλέγει τυχαία έναν εργαζόμενο
    public Ergazomenoi pickErg(ArrayList<Ergazomenoi> ergazomenois, int r){
            Ergazomenoi erg;
            erg = ergazomenois.get(r);

            return erg;
    }

//Τοποθέτηση εργαζομένου σε βάρδια
    public Vardia placeErg(Ergazomenoi erg, int date, int var){// κλήση vardia.add(placeErg(.,.,.))
            String shft="";
        switch (var){
            case 0:
                shft = "Πρωί";
                break;
            case 1:
                shft = "Απόγευμα";
                break;
            case 2:
                shft = "Βράδυ";
                break;
        }

        String onoma = erg.onoma;
        String epitheto = erg.epitheto;
        String eidikotita = erg.eidikotita;


        return new Vardia(onoma, epitheto, shft, eidikotita, (dpSchStart.getValue().plus(date, ChronoUnit.DAYS)));
    }

    int setVardTheseis(HashMap<String, String> hash, String field, int var){
        int theseis=0;
        String f;
        f = field+var;

        for(String h : hash.keySet()) {
            if(h.equals(f)){
                theseis = Integer.parseInt(hash.get(h));
            }
        }
        return theseis;
    }

    public void clearPicked(){
        for(int i=0; i<this.pinakas.length; i++){
            this.pinakas[i].picked =false;
        }
    }

    public void checkSeqDays(){
        for(Matrix pinaka : this.pinakas){
            if (pinaka.seqDays == 5){
                pinaka.picked = true;
                pinaka.seqDays = 0;
            }
        }
    }

    public void check_adeies(ArrayList <Ergazomenoi> ergazomenoi, JsonReader jsonReader, int var,ArrayList<RestDay> restDays,int imera){
        HashMap hash = jsonReader.not_working;
        String times, times2;
        boolean found=false;

        for(Object key : jsonReader.not_working.keySet()){
            for(Ergazomenoi erg : ergazomenoi){
                for(RestDay red : restDays){
                    if (red.ergazomenos.equals(erg) && (red.day-1) == imera) {
                        found = true;
                        break;
                    }
                }
                if(!found){
                    times = erg.onoma+" "+erg.epitheto+" "+erg.eidikotita;
                    if(key.equals(times)){
                        for(int i =0; i<ergazomenoi.size(); i++){
                            times2 = this.pinakas[i].ergazomenos.onoma +" "+this.pinakas[i].ergazomenos.epitheto+" "+this.pinakas[i].ergazomenos.eidikotita;
                            if(times2.equals(times) && var == Integer.parseInt(hash.get(key).toString()))
                                this.pinakas[i].picked= true;
                            else if(times2.equals(times) && var != Integer.parseInt(hash.get(key).toString())){
                                this.pinakas[i].picked = false;
                            }

                        }
                    }
                }
            }
        }
    }

    public int findLeastHours(int var, String f) { //βρισκει τη μικροτερη ωρα
        int temp=0;
        int min=56;
        boolean found = false;
        ArrayList<Ergazomenoi> erg = new ArrayList <>();

        if (var ==0 ) {
            for (int i = 0; i < (this.pinakas.length); i++) {
                if (this.pinakas[i].ergazomenos.eidikotita.equals(f) && !this.pinakas[i].picked) {
                    if(this.pinakas[i].wresPrwi < min ) {
                        min = this.pinakas[i].wresPrwi;
                        temp = i;
                        found = true;
                    }
                    else if(this.pinakas[i].wresVradu == min){
                        placeRandErg(erg, this.pinakas[i].ergazomenos);
                    }
                }
            }
        }
        else if (var ==1 ){
            for (int i = 0; i < (this.pinakas.length); i++) {
                if (this.pinakas[i].ergazomenos.eidikotita.equals(f) && !this.pinakas[i].picked) {
                    if(this.pinakas[i].wresApogeyma < min ) {
                        min = this.pinakas[i].wresApogeyma;
                        temp = i;
                        found = true;
                    }
                    else if(this.pinakas[i].wresVradu == min){
                        placeRandErg(erg, this.pinakas[i].ergazomenos);
                    }
                }
            }
        }
        else{
            for (int i = 0; i < (this.pinakas.length); i++) {
                if (this.pinakas[i].ergazomenos.eidikotita.equals(f) && !this.pinakas[i].picked) {
                    if(this.pinakas[i].wresVradu < min){
                        min = this.pinakas[i].wresVradu;
                        temp = i;
                        found = true;
                    }
                    else if(this.pinakas[i].wresVradu == min){
                        placeRandErg(erg, this.pinakas[i].ergazomenos);
                    }
                }
            }
        }
        if(!found) {
            if (erg.size() > 0) {
                temp = findRandErg(erg);
            }
        }

        return temp;
    }

    public boolean checkPicked(int i){
        boolean check = false;

        if(!this.pinakas[i].picked)
            check = true;

        return check;
    }

    public boolean checkPost(int i, String f){
        boolean check = false;

        if (this.pinakas[i].ergazomenos.eidikotita.equals(f))
            check = true;

        return check;
    }

    public boolean checkHoursTot(int i, JsonReader jsonReader) throws IOException, ParseException {
        boolean check=false;

        jsonReader.readDepends();
        if(this.pinakas[i].wresevd +jsonReader.wresAnaEidik <= this.pinakas[i].ergazomenos.evWres)
            check = true;

        return check;
    }

    public int checkHoursVard(int i, int var,int min){
        int minimum=min;
        if (var == 0) {
            if (this.pinakas[i].wresPrwi < min)
                minimum = i;
            else if(this.pinakas[i].wresPrwi == min){
                placeRandErg(erg, this.pinakas[i].ergazomenos);
            }
        }
        else if(var == 1) {
            if (this.pinakas[i].wresApogeyma < min)
                minimum = i;
            else if(this.pinakas[i].wresApogeyma == min){
                placeRandErg(erg, this.pinakas[i].ergazomenos);
            }
        }
        else
            if (this.pinakas[i].wresVradu < min)
                minimum = i;
            else if(this.pinakas[i].wresApogeyma == min){
                placeRandErg(erg, this.pinakas[i].ergazomenos);
            }

        return minimum;
    }

    public void placeRandErg(ArrayList<Ergazomenoi> erg, Ergazomenoi ergazomenos) {
        erg.add(ergazomenos);
    }

    public int findRandErg(ArrayList<Ergazomenoi> erg){
        int thesi=0;
        Ergazomenoi ergazomenos;
        Random rand = new Random();

        thesi = rand.nextInt(erg.size());
        ergazomenos = erg.get(thesi);

        for(int i =0 ; i<this.pinakas.length; i++){
            if(ergazomenos.equals(this.pinakas[i].ergazomenos)){
                thesi = i;
            }
        }

        return thesi;
    }

    public void nullMatrix(){
        for(int i =0; i < (this.pinakas.length); i++){
            this.pinakas[i].wresPrwi = 0;
            this.pinakas[i].wresApogeyma = 0;
            this.pinakas[i].wresVradu = 0;
            this.pinakas[i].wresevd = 0;
        }
    }

    public void createMatrixTable(VBox vBox){


        TableColumn firstNameCol = new TableColumn("Όνομα");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        TableColumn lastNameCol = new TableColumn("Επίθετο");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
        TableColumn eidikCol = new TableColumn("Ειδικότητα");
        eidikCol.setMinWidth(100);
        eidikCol.setCellValueFactory(new PropertyValueFactory<Person, String>("eidikotita"));
        TableColumn wresPCol = new TableColumn("Ώρες Πρωί");
        wresPCol.setMinWidth(100);
        wresPCol.setCellValueFactory(new PropertyValueFactory<Person, String>("wresPrwi"));
        TableColumn wresACol = new TableColumn("Ώρες Απόγευμα");
        wresACol.setMinWidth(100);
        wresACol.setCellValueFactory(new PropertyValueFactory<Person, String>("wresApog"));
        TableColumn wresVCol = new TableColumn("Ώρες Βράδυ");
        wresVCol.setMinWidth(100);
        wresVCol.setCellValueFactory(new PropertyValueFactory<Person, String>("wresVrad"));
        TableColumn wresTCol = new TableColumn("Ώρες Σύνολο");
        wresTCol.setMinWidth(100);
        wresTCol.setCellValueFactory(new PropertyValueFactory<Person, String>("wresTot"));

        tvPerson.setItems(people);
        tvPerson.getColumns().addAll(firstNameCol, lastNameCol, eidikCol, wresPCol, wresACol, wresVCol, wresTCol);
        HBox hBoxEdit = new HBox(8);
        Button btIndSave = new Button("Export Work Hours");
        hBoxEdit.setPadding(new Insets(0));
        hBoxEdit.getChildren().add(btIndSave);
        hBoxEdit.setAlignment(Pos.CENTER_RIGHT);
        btIndSave.setOnAction(e-> {
            try {
                DataExport.export_Wres();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Handlers.checkSuccess("Success!", "Work Hours data exported successfully!");
        });

        vBox.getChildren().addAll(tvPerson,hBoxEdit);
    }

    public ObservableList<Person> getMatrixList(){
        ObservableList<Person> people = FXCollections.observableArrayList();
        for(int i=0;i<this.pinakas.length;i++){
            String onoma = this.pinakas[i].ergazomenos.onoma;
            String epitheto = this.pinakas[i].ergazomenos.epitheto;
            String eidikotita = this.pinakas[i].ergazomenos.eidikotita;
            String wresPrwi = Integer.toString(this.pinakas[i].wresPrwi);
            String wresApogeyma = Integer.toString(this.pinakas[i].wresApogeyma);
            String wresVradu = Integer.toString(this.pinakas[i].wresVradu);
            String wresTotal = Integer.toString(this.pinakas[i].wresTot);
            people.add(new Person(onoma, epitheto, eidikotita, wresPrwi, wresApogeyma, wresVradu, wresTotal));
        }
        return people;
    }
}
