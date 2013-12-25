package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

/** This class is called by runTbFormatter and does the work of parsing the input CSV.
 * Needs to know:
 * 1) Type (c, or ACL)
 * 2) if c (baseline or 1 year)
 *    if ACL (baseline, baseline contra, 1 year, or 1 year contra)
 * 3) for tibia, special case need to consider lateral, medial or whole
 *    all others only need to deal with whole
 * Steps?
 *   1) Read in all lines.
 *   2) Collect trabecular values into groups based on type
 *   3) 
 * @author paul
 *
 */
public class TbFormatter {
    private static int debug = 1;
    private Scanner _scanner;
    private static boolean _tb;
    private PatientLists _lists;
    public TbFormatter(BufferedReader reader) {
        _scanner = new Scanner(reader);
    }
    
    public PatientLists lists() {
        return _lists;
    }
    public void setTb(boolean bool) {
        _tb = bool;
    }
    /** Before building patient list, make sure:
     * 1) all entries in csv for type are filled (c or acl)
     * 2) timeline is only b, 1, b-contra, 1-contra
     * 3) Remember to pass in flag for control or acl
     * @throws IOException
     */
    public void buildPatientLists() throws IOException {
        if (_tb) {
            _lists = new PatientLists(_tb);
            String[] line;
            Patient patient = null;
            PatientRecordGroup group = null;
            int pnumber = -1;
            String timeline = "";
            String type = "";
            _scanner.nextLine();//throw away first line
            while (_scanner.hasNextLine()) {
                String blob = _scanner.nextLine();
                line = blob.replaceAll("\"","").split(",");
                if (line.length == 0) {
                    continue;
                }
                if(!line[0].equals("")) {
                    if (patient != null) {
                        completePatientRecords(patient);
                        System.out.println(patient);
                        _lists.addPatient(patient);
                    }
                    pnumber = Integer.parseInt(line[0]);
                    patient = new Patient(pnumber);
                    patient.setTb(_tb);
                    type = line[1];
                    patient.setType(type);
                }
                if ((line.length < 9) || line[8].equals("")){ //empty record, so skip;
                    continue; //
                }
                if (!line[2].equals("")) {
                    timeline = line[2];
                    group = new PatientRecordGroup(pnumber);
                    group.setTimeline(timeline);
                    patient.addRecordGroup(group);
                }
                PatientRecord rec = new PatientRecord();
                rec.setTB(true);
                rec.setEmpty(false);
                rec.setP(pnumber);
                rec.setType(type);
                rec.setTimeline(timeline);
                rec.setFuzzy(line[8]);
                rec.setTbTh(line[9]);
                rec.setTbSp(line[10]);
                rec.setTbN(line[11]);
                rec.setPortion(line[7]);
                group.set(rec);
            }
            completePatientRecords(patient);
            _lists.addPatient(patient);
            _scanner.close();
            if (debug == 1) {
                this.printPatientList();
            }
            
        } else {
            /** Assumes that input to scanner 
             * a) only consists of data that should be added
             * b) first element has a patient number that matches the regex "/(P\d*)/"
             * c) first element has a timeline that matches the regex
             *    "/(b|b-contra|1|1contra)/"
             *  Assumes that "type" will be given (control or acl)
             */
            String[] line;
            _lists = new PatientLists(_tb);
            Patient patient = null;
            int pnumber = -1;
            _scanner.nextLine(); // throw away first line;
            while(_scanner.hasNextLine()) {
                line = _scanner.nextLine().split("\\s");//split input lines around whitespace
                Pattern pNumber = Pattern.compile("/P\\d*/");
                Pattern timeline = Pattern.compile("/(b|b-contra|1|1-contra)/");
                
                
                if (!line[0].equals("")) { // if true then you have scanned in a new patient
                    if (patient != null) {
                        completePatientRecords(patient);
                        //System.out.println(patient);
                        _lists.addPatient(patient);
                    }
                    pnumber = Integer.parseInt(line[0]);
                    patient = new Patient(pnumber);
                    patient.setTb(_tb);
                    patient.setType(line[1]);
                }
                if ((line.length < 9) || line[8].equals("")){ //empty record, so skip;
                    continue; //
                }
                PatientRecord rec = new PatientRecord();
                rec.setTB(false);
                rec.setEmpty(false);
                rec.setP(pnumber);
                rec.setType(line[1]);
                rec.setTimeline(line[2]); //make sure input file only has c, 1, b, b-contra, 1, 1-contra
                rec.setFuzzy(line[7]);
                rec.setTbTh(line[8]);
                rec.setTbSp(line[9]);
                rec.setTbN(line[10]);
                patient.addRecord(rec.timeline(), rec);    
            }
            completePatientRecords(patient);
            _lists.addPatient(patient);
            _scanner.close();
            if (debug == 1) {
                this.printPatientList();
            }
        }
    }

    
    /** Fill in unfilled Records in the patient with blank entries.*/
    public static void completePatientRecords(Patient pat) {
        if(_tb) {
            String[] portions = {"whole", "med", "lat"};
            if (pat.type().equals("c")) {
                if(!pat.hasGroupRecord("b")) {
                    /** Create Empty Record with (number, "c", "b")*/
                    PatientRecordGroup gr = new PatientRecordGroup(pat.pnumber());
                    gr.setType("c");
                    gr.setTimeline("b");
                    for(int i = 0; i < 3; i++) {
                        PatientRecord rec = new PatientRecord(pat.pnumber());
                        rec.setType("c");
                        rec.setTimeline("b");
                        rec.setPortion(portions[i]);
                        rec.setEmpty(true);
                        rec.setTB(true);
                        gr.set(rec);
                    }
                    pat.addRecordGroup(gr);
                }
                if(!pat.hasGroupRecord("1")) {
                    /** Create Empty Record with (number, "c", "b")*/
                    PatientRecordGroup gr = new PatientRecordGroup(pat.pnumber());
                    gr.setType("c");
                    gr.setTimeline("1");
                    for(int i = 0; i < 3; i++) {
                        PatientRecord rec = new PatientRecord(pat.pnumber());
                        rec.setType("c");
                        rec.setTimeline("1");
                        rec.setPortion(portions[i]);
                        rec.setEmpty(true);
                        rec.setTB(true);
                        gr.set(rec);
                    }
                    pat.addRecordGroup(gr);
                }
            } else { //patient is "acl" type
                if(!pat.hasGroupRecord("b")) {
                    /** Create Empty Record with (number, "c", "b")*/
                    PatientRecordGroup gr = new PatientRecordGroup(pat.pnumber());
                    gr.setType("acl");
                    gr.setTimeline("b");
                    for(int i = 0; i < 3; i++) {
                        PatientRecord rec = new PatientRecord(pat.pnumber());
                        rec.setType("acl");
                        rec.setTimeline("b");
                        rec.setPortion(portions[i]);
                        rec.setEmpty(true);
                        rec.setTB(true);
                        gr.set(rec);
                    }
                    pat.addRecordGroup(gr);
                }
                if(!pat.hasGroupRecord("b-contra")) {
                    /** Create Empty Record with (number, "c", "b")*/
                    PatientRecordGroup gr = new PatientRecordGroup(pat.pnumber());
                    gr.setType("acl");
                    gr.setTimeline("b-contra");
                    for(int i = 0; i < 3; i++) {
                        PatientRecord rec = new PatientRecord(pat.pnumber());
                        rec.setType("acl");
                        rec.setTimeline("b-contra");
                        rec.setPortion(portions[i]);
                        rec.setEmpty(true);
                        rec.setTB(true);
                        gr.set(rec);
                    }
                    pat.addRecordGroup(gr);
                }
                if(!pat.hasGroupRecord("1")) {
                    /** Create Empty Record with (number, "c", "b")*/
                    PatientRecordGroup gr = new PatientRecordGroup(pat.pnumber());
                    gr.setType("acl");
                    gr.setTimeline("1");
                    for(int i = 0; i < 3; i++) {
                        PatientRecord rec = new PatientRecord(pat.pnumber());
                        rec.setType("acl");
                        rec.setTimeline("1");
                        rec.setPortion(portions[i]);
                        rec.setEmpty(true);
                        rec.setTB(true);
                        gr.set(rec);
                    }
                    pat.addRecordGroup(gr);
                }
                if(!pat.hasGroupRecord("1-contra")) {
                    /** Create Empty Record with (number, "c", "b")*/
                    PatientRecordGroup gr = new PatientRecordGroup(pat.pnumber());
                    gr.setType("acl");
                    gr.setTimeline("1-contra");
                    for(int i = 0; i < 3; i++) {
                        PatientRecord rec = new PatientRecord(pat.pnumber());
                        rec.setType("acl");
                        rec.setTimeline("1-contra");
                        rec.setPortion(portions[i]);
                        rec.setEmpty(true);
                        rec.setTB(true);
                        gr.set(rec);
                    }
                    pat.addRecordGroup(gr);
                }
               
            }
        } else {
            if (pat.type().equals("c")) {
                if(!pat.hasRecord("b")) {
                    /** Create Empty Record with (number, "c", "b")*/
                    PatientRecord rec = new PatientRecord(pat.pnumber());
                    rec.setType("c");
                    rec.setTimeline("b");
                    rec.setEmpty(true);
                    rec.setTB(false);
                    pat.addRecord("b", rec);
                }
                if(!pat.hasRecord("1")) {
                    PatientRecord rec = new PatientRecord(pat.pnumber());
                    rec.setType("c");
                    rec.setTimeline("1");
                    rec.setEmpty(true);
                    rec.setTB(false);
                    pat.addRecord("1", rec);
                }
            } else { //patient is "acl" type
                if(!pat.hasRecord("b")) {
                    PatientRecord rec = new PatientRecord(pat.pnumber());
                    rec.setType("acl");
                    rec.setTimeline("b");
                    rec.setEmpty(true);
                    rec.setTB(false);
                    pat.addRecord("b", rec);
                }
                if(!pat.hasRecord("b-contra")) {
                    PatientRecord rec = new PatientRecord(pat.pnumber());
                    rec.setType("acl");
                    rec.setTimeline("b-contra");
                    rec.setEmpty(true);
                    rec.setTB(false);
                    pat.addRecord("b-contra", rec);
                }
                if(!pat.hasRecord("1")) {
                    PatientRecord rec = new PatientRecord(pat.pnumber());
                    rec.setType("acl");
                    rec.setTimeline("1");
                    rec.setEmpty(true);
                    rec.setTB(false);
                    pat.addRecord("1", rec);
                }
                if(!pat.hasRecord("1-contra")) {
                    PatientRecord rec = new PatientRecord(pat.pnumber());
                    rec.setType("acl");
                    rec.setTimeline("1-contra");
                    rec.setEmpty(true);
                    rec.setTB(false);
                    pat.addRecord("1-contra", rec);
                }
            }
        }
    }
    
    /**Prints out every patient in _lists controls and then acl's*/
    private void printPatientList() {
       for(int i = 0; i < _lists.controls().size(); i++) {
            System.out.printf(_lists.controls().get(i).toString());
        }
        System.out.println("=========== Break =============");
        for (int i = 0; i < _lists.acls().size(); i++) {
            System.out.printf(_lists.acls().get(i).toString());
        }
    }
    
    
}
    
    
    
    

