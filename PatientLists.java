package utilities;

import java.util.ArrayList;
import java.util.Collections;

/** This class keeps a list of ordered Patients (Patients are either controls or ACL injured)*/
public class PatientLists {
    private ArrayList<Patient> _controlPatients = new ArrayList<Patient>();
    private ArrayList<Patient> _ACLPatients = new ArrayList<Patient>();
    private boolean _tb;
    /** Default constructor.*/
    public PatientLists(boolean tb) {
        _tb = tb;
    }
    public void addPatient(Patient p) { //Patients don't have to be added in order, they are sorted later in this program
        if (p.type().equals("acl")) {
            _ACLPatients.add(p);
        } else {
            _controlPatients.add(p);
        }
    }
    public ArrayList<Patient> controls() {
        return _controlPatients;
    }
    
    public ArrayList<Patient> acls() {
        return _ACLPatients;
    }


    /** Replace patient record PR. Returns true if replacement is successful.*/
    public boolean replaceRecord(PatientRecord pr) {
        if (pr.type().equals("c")) {
            for (int i = 0; i < _controlPatients.size(); i++) {
                if (_controlPatients.get(i).pnumber() == pr.pnumber()) {
                    if (_controlPatients.get(i).hasRecord(pr.timeline())) {
                        _controlPatients.get(i).addRecord(pr.timeline(), pr);
                    } else {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        } else if (pr.type().equals("acl")) {
            for (int i = 0; i < _ACLPatients.size(); i++) {
                if (_ACLPatients.get(i).pnumber() == pr.pnumber()) {
                    if (_ACLPatients.get(i).hasRecord(pr.timeline())) {
                        _ACLPatients.get(i).addRecord(pr.timeline(), pr);
                    } else {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    public String[] getControlBList() {// continue from here
        String[] blist = new String[_controlPatients.size()];
        for(int i = 0; i < _controlPatients.size(); i++) {
            if (!_tb) {
                blist[i] = _controlPatients.get(i).getRecord("b").csvString();
            } else {
                blist[i] = _controlPatients.get(i).getGroup("b").csvString();
            }
           
        }
        return blist;
    }
    public String[] getControl1List() {
        String[] blist = new String[_controlPatients.size()];
        for(int i = 0; i < _controlPatients.size(); i++) {
            if (!_tb) {
                blist[i] = _controlPatients.get(i).getRecord("1").csvString();
            } else {
                blist[i] = _controlPatients.get(i).getGroup("1").csvString();
            }

        }
        return blist;
    }
    public String[] getACLBList() {
        String[] blist = new String[_ACLPatients.size()];
        for(int i = 0; i < _ACLPatients.size(); i++) {
            if (!_tb) {
                blist[i] = _ACLPatients.get(i).getRecord("b").csvString();
            } else {
                blist[i] = _ACLPatients.get(i).getGroup("b").csvString();
            }
        }
        return blist;
    }
    public String[] getACLBContraList() {
        String[] blist = new String[_ACLPatients.size()];
        for(int i = 0; i < _ACLPatients.size(); i++) {
            if (!_tb) {
                blist[i] = _ACLPatients.get(i).getRecord("b-contra").csvString();
            } else {
                blist[i] = _ACLPatients.get(i).getGroup("b-contra").csvString();
            }
        }
        return blist;
    }
    public String[] getACL1List() {
        String[] blist = new String[_ACLPatients.size()];
        for(int i = 0; i < _ACLPatients.size(); i++) {
            if (!_tb) {
                blist[i] = _ACLPatients.get(i).getRecord("1").csvString();
            } else {
                blist[i] = _ACLPatients.get(i).getGroup("1").csvString();
            }
        }
        return blist;
    }
    public String[] getACL1ContraList() {
        String[] blist = new String[_ACLPatients.size()];
        for(int i = 0; i < _ACLPatients.size(); i++) {
            if (!_tb) {
                blist[i] = _ACLPatients.get(i).getRecord("1-contra").csvString();
            } else {
                blist[i] = _ACLPatients.get(i).getGroup("1-contra").csvString();
            }
        }
        return blist;
    }
    
    public void sort() {
        Collections.sort(_controlPatients);
        Collections.sort(_ACLPatients);
    }

}
