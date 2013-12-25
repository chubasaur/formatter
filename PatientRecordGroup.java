package utilities;

public class PatientRecordGroup {
    private int _pnumber;
    private String _type;
    private String _timeline;
    private PatientRecord _whole;
    private PatientRecord _lat;
    private PatientRecord _med;
    
    public PatientRecordGroup(int pnum) {
        _pnumber = pnum;
    }
    
    public PatientRecord getWhole(PatientRecord whole) {
        return _whole;
    }
    public PatientRecord getLat(PatientRecord lat) {
        return _lat;
    }
    public PatientRecord getMed(PatientRecord med) {
        return _med;
    }
    public void setTimeline(String timeline) {
        _timeline = timeline;
    }
    public String timeline() {
        return _timeline;
    }
    public String type() {
        return _type;
    }
    public void setType(String type) {
        _type = type;
    }
    public void set(PatientRecord rec) {
        if (rec.portion().equals("whole")){
            _whole = rec;
        } else if (rec.portion().equals("lat")) {
            _lat = rec;
        } else {
            _med = rec;
        }
        
    }
    
    public String csvString() {/** Fuzzy/TbTh/TbSp/TbN  (Lat, Med, Whole)*/ // continue here
        if (_whole.empty() && _lat.empty() && _med.empty()) {//if 1 empty,all should be empty
            return String.format("%d,\"No data\",,,,,,,,,,,\n", _pnumber);
        }
        String groupRecord = String.format("%d,\"%s\",\"%s\",\"%s\",", _pnumber, _lat.fuzzy(), _med.fuzzy(), _whole.fuzzy());
        groupRecord = groupRecord.concat(String.format("\"%s\",\"%s\",\"%s\",", _lat.TbTh(), _med.TbTh(), _whole.TbTh()));
        groupRecord = groupRecord.concat(String.format("\"%s\",\"%s\",\"%s\",", _lat.TbSp(), _med.TbSp(), _whole.TbSp()));
        groupRecord = groupRecord.concat(String.format("\"%s\",\"%s\",\"%s\"\n", _lat.TbN(), _med.TbN(), _whole.TbN()));
        return groupRecord;
    }
    
    @Override
    public String toString() {
        return String.format("\t%s\t%s\t%s", _whole.toString(),
                _lat.toString(), _med.toString());
        
   }
}
