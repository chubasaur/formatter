package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class TbFormatter {
    private static int debug = 10;
    private Scanner _scanner;
    private static boolean _tb;
    private PatientLists _lists;
    private static boolean _update;
    private Scanner _replacement;
    public TbFormatter(BufferedReader reader) {
        _scanner = new Scanner(reader);
    }
    
    public PatientLists lists() {
        return _lists;
    }
    public void setTb(boolean bool) {
        _tb = bool;
    }
    /** Set the boolean flag indicating whether a replacement is to happen
     *  or not to BOOL.*/
    public void setUpdate(boolean bool) {
        _update = bool;
    }
    /** Set the replacement text scanner to REPL.*/
    public void setReplacement(BufferedReader repl) {
        _replacement = new Scanner(repl);
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
            while (_scanner.hasNextLine()) {
                String blob = _scanner.nextLine();
                line = blob.split("\t");
                Pattern pNumPat = Pattern.compile(".*/P(\\d*)/.*");
                Pattern timelinePat = Pattern.compile(".*/(b|b-contra|1|1-contra)/.*");
                Pattern typePat = Pattern.compile(".*/(control|acl)/.*");
                Matcher pMatch = pNumPat.matcher(line[0]); //pMatch now contains the pnumber
                Matcher tMatch = timelinePat.matcher(line[0]); // tMatch contains the timeline
                Matcher typeMatch = typePat.matcher(line[0]);
                pMatch.find();
                tMatch.find();
                typeMatch.find();
                if (line.length == 0) {
                    continue;
                }
                if(pnumber != Integer.parseInt(pMatch.group(1))) {
                    if (patient != null) {
                        completePatientRecords(patient);
                        //System.out.println(patient);
                        _lists.addPatient(patient);
                    }
                    pnumber = Integer.parseInt(pMatch.group(1));
                    patient = new Patient(pnumber);
                    patient.setTb(_tb);
                    if (typeMatch.group(1).equals("control")) {
                        type = "c";
                    } else {
                        type = "acl";
                    }
                    patient.setType(type);
                }
                if (line[3].trim().equals("1")) { //1 represents "whole", know that when you scan this you
                                           // are beginning a new record group
                    timeline = tMatch.group(1);
                    group = new PatientRecordGroup(pnumber);
                    group.setTimeline(timeline);
                    patient.addRecordGroup(group);
                }
                PatientRecord rec = new PatientRecord();
                rec.setTB(true);
                rec.setEmpty(false);
                rec.setP(pnumber);
                if (typeMatch.group(1).equals("control")) {
                    rec.setType("c");
                } else {
                    rec.setType("acl");
                }
                rec.setTimeline(tMatch.group(1));
                rec.setFuzzy(line[4].trim());
                rec.setTbTh(line[5].trim());
                rec.setTbSp(line[6].trim());
                rec.setTbN(line[7].trim());
                switch (Integer.parseInt(line[3].trim())) {
                    case 1:
                        rec.setPortion("whole");
                        break;
                    case 2:
                        rec.setPortion("lat");
                        break;
                    case 3:
                        rec.setPortion("med");
                        break;
                }
                group.set(rec);
            }
            completePatientRecords(patient);
            _lists.addPatient(patient);
            _scanner.close();
            if (debug == 1) {
                this.printPatientList();
            }
            insertBlankRecords();
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
            while(_scanner.hasNextLine()) {
                line = _scanner.nextLine().split("\t");//split input lines around tabs
                Pattern pNumPat = Pattern.compile(".*/P(\\d*)/.*");
                Pattern timelinePat = Pattern.compile(".*/(b|b-contra|1|1-contra)/.*");
                Pattern typePat = Pattern.compile(".*/(control|acl)/.*");
                Matcher pMatch = pNumPat.matcher(line[0]); //pMatch now contains the pnumber
                Matcher tMatch = timelinePat.matcher(line[0]); // tMatch contains the timeline
                Matcher typeMatch = typePat.matcher(line[0]);
                pMatch.find();
                tMatch.find();
                typeMatch.if();
                
                 (pnumber != Integer.parseInt(pMatch.group(1))) { // if true then you have scanned in a new patient
                    if (patient != null) {
                        completePatientRecords(patient);
                        //System.out.println(patient);
                        _lists.addPatient(patient);
                    }
                    pnumber = Integer.parseInt(pMatch.group(1));
                    patient = new Patient(pnumber);
                    patient.setTb(_tb);
                    if (typeMatch.group(1).equals("control")) {
                        patient.setType("c");
                    } else {
                        patient.setType("acl");
                    }
                }
                PatientRecord rec = new PatientRecord();
                rec.setTB(false);
                rec.setEmpty(false);
                rec.setP(pnumber);
                if (typeMatch.group(1).equals("control")) {
                    rec.setType("c");
                } else {
                    rec.setType("acl");
                }
                rec.setTimeline(tMatch.group(1)); //make sure input file only has c, 1, b, b-contra, 1, 1-contra
                rec.setFuzzy(line[4].trim());
                rec.setTbTh(line[5].trim());
                rec.setTbSp(line[6].trim());
                rec.setTbN(line[7].trim());
                patient.addRecord(rec.timeline(), rec);    
            }
            completePatientRecords(patient);
            _lists.addPatient(patient);
            _scanner.close();
            if (debug == 1) {
                this.printPatientList();
            }
            insertBlankRecords();
        }
        _lists.sort();
        if (_update) {
            update();
        }
    }



    /** Updates patient records in _lists with patient information in _replacement.*/
    private void update() {
        String[] line;
        while (_scanner.hasNextLine()) {
            line = _scanner.nextLine().split("\t");
            PatientRecord rec = makeRecord(line);
            if (!_lists.replaceRecord(rec)) {
                throw new IllegalArgumentException("Cannot replace a record which does not exist.");
            }
        }
    }



    private PatientRecord makeRecord(String[] line) {
        line = _scanner.nextLine().split("\t");//split input lines around tabs
        Pattern pNumPat = Pattern.compile(".*/P(\\d*)/.*");
        Pattern timelinePat = Pattern.compile(".*/(b|b-contra|1|1-contra)/.*");
        Pattern typePat = Pattern.compile(".*/(control|acl)/.*");
        Matcher pMatch = pNumPat.matcher(line[0]); //pMatch now contains the pnumber
        Matcher tMatch = timelinePat.matcher(line[0]); // tMatch contains the timeline
        Matcher typeMatch = typePat.matcher(line[0]);
        pMatch.find();
        tMatch.find();
        typeMatch.find();
        PatientRecord rec = new PatientRecord();
        rec.setP(Integer.parseInt(pMatch.group(1)));
        rec.setTimeline(tMatch.group(1));
        rec.setTB(_tb);
        if (_tb) { //tb and !tb have different ways of matching
            rec.setEmpty(false);
            if (typeMatch.group(1).equals("control")) {
                rec.setType("c");
            } else {
                rec.setType("acl");
            }
            rec.setTimeline(tMatch.group(1));
            rec.setFuzzy(line[4].trim());
            rec.setTbTh(line[5].trim());
            rec.setTbSp(line[6].trim());
            rec.setTbN(line[7].trim());
            switch (Integer.parseInt(line[3].trim())) {
            case 1:
                rec.setPortion("whole");
                break;
            case 2:
                rec.setPortion("lat");
                break;
            case 3:
                rec.setPortion("med");
                break;
            }
        } else {
            rec.setEmpty(false);
            if (typeMatch.group(1).equals("control")) {
                rec.setType("c");
            } else {
                rec.setType("acl");
            }
            rec.setFuzzy(line[4].trim());
            rec.setTbTh(line[5].trim());
            rec.setTbSp(line[6].trim());
            rec.setTbN(line[7].trim());
        }
        return rec;
    }




    /** Insert Blank Records into patient list to maintain consistency.*/
    private void insertBlankRecords() {
        String[] emptyControls = {"91"};//insert missing control patients here
        String[] emptyACLs = {"290"};// insert missing acl patients here
        for (int i = 0; i < emptyControls.length; i++) {
            Patient pat = new Patient(Integer.parseInt(emptyControls[i]));
            pat.setTb(_tb);
            pat.setType("c");
            completePatientRecords(pat);
            _lists.addPatient(pat);
        }
        for (int i = 0; i < emptyControls.length; i++) {
            Patient pat = new Patient(Integer.parseInt(emptyACLs[i]));
            pat.setTb(_tb);
            pat.setType("acl");
            completePatientRecords(pat);
            _lists.addPatient(pat);
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
    
    
    
    

