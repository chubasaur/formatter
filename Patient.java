package utilities;

import java.util.HashMap;

public class Patient implements Comparable{
    private int _pnumber;
    private boolean _tb;
    private String _type; // acl or c
    HashMap<String, PatientRecord> _records = new HashMap<String, PatientRecord>(); //index records for 
    //this patient by timeline (b, 1) (b, b-contra, 1, 1-contra);
    HashMap<String, PatientRecordGroup> _groupRecords = new HashMap<String, PatientRecordGroup>();
    
    public Patient(int pnumber) {
        _pnumber = pnumber;
    }
    
    public void setTb(boolean bool) {
        _tb = bool;
    }
    
    public void addRecord(String timeline, PatientRecord rec) {
        _records.put(timeline, rec);
    }

    public void addRecordGroup(PatientRecordGroup gr) {
        _groupRecords.put(gr.timeline(), gr);
    }
    
    public PatientRecordGroup getGroup(String timeline) {
        return _groupRecords.get(timeline);
    }
    public void setType(String type) {
        _type = type;
    }
   
    public PatientRecord getRecord(String timeline) {
        return _records.get(timeline);
    }
    
    public int pnumber() {
        return _pnumber;
    }
    
    public String type() {
        return _type;
    }
    
    public boolean hasRecord(String timeline) {
        return _records.containsKey(timeline);
    }
    
    public boolean hasGroupRecord(String timeline) {
        return _groupRecords.containsKey(timeline);
    }
    @Override
    public int compareTo(Object arg0) {
        if (this.pnumber() < ((Patient) arg0).pnumber()) {
            return -1;
        } else if (this.pnumber() < ((Patient) arg0).pnumber()) {
            return 0;
        } else {
            return 1;
        }
    }
    
    
    @Override
    public String toString() {
        String me = String.format("P %d %s:\n", _pnumber, _type);
        if (_tb) {
            if (_type.equals("c")) {
                me = me.concat(_groupRecords.get("b").toString());
                me = me.concat(_groupRecords.get("1").toString());
            } else {
                me = me.concat(_groupRecords.get("b").toString());
                me = me.concat(_groupRecords.get("b-contra").toString());
                me = me.concat(_groupRecords.get("1").toString());
                me = me.concat(_groupRecords.get("1-contra").toString());
            }
        } else {
            if(_type.equals("c")) {
                me = me.concat("\t"); //add tab
                me = me.concat(_records.get("b").toString());
                me = me.concat("\t");
                me = me.concat(_records.get("1").toString());
            } else { //acl
                me = me.concat("\t");
                me = me.concat(_records.get("b").toString());
                me = me.concat("\t");
                me = me.concat(_records.get("b-contra").toString());
                me = me.concat("\t");
                me = me.concat(_records.get("1").toString());
                me = me.concat("\t");
                me = me.concat(_records.get("1-contra").toString());
            }
        }
        return me;
    }

}
