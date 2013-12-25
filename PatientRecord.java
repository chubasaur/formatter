package utilities;

/** Each record knows its own patient, type, timeline, and parameters.*/
public class PatientRecord {
    private int _p;
    private String _type; //c, acl or blank
    private String _timeline; // if c then b, or 1 (or 1, 2); if acl then b, b-contra,
    private String _fuzzyBVF;
    private String _TbTh;
    private String _TbSp;
    private String _TbN;
    private String _portion;//whole, lat, or med
    private boolean _empty; //default record is not empty
    private boolean _tb = false;
    
    public PatientRecord() {
    }
    
    public PatientRecord(int pnumber) {
        _p = pnumber;
    }
    
    public String fuzzy() {
        return _fuzzyBVF;
    }
    
    public String TbTh() {
        return _TbTh;
    }
    
    public String TbSp() {
        return _TbSp;
    }
    
    public String TbN() {
        return _TbN;
    }
    
    public void setP(int p) {
        _p = p;
    }
    public void setType(String type) {
        _type = type;
    }
    public void setTimeline(String timeline) {
        _timeline = timeline;
    }
    public void setFuzzy(String fuzzy) {
        _fuzzyBVF = fuzzy;
    }
    public void setTbTh(String tbth) {
        _TbTh = tbth;
    }
    public void setTbSp(String TbSp) {
        _TbSp = TbSp;
    }
    public void setTbN(String tbn) {
        _TbN = tbn;
    }
    
    public void setPortion(String portion) {
        _portion = portion;
    }
    public String timeline() {
        return _timeline;
    }
    
    public void setEmpty(boolean bool) {
        _empty = bool;
    }
    
    public void setTB(boolean bool) {
        _tb = bool;
    }
    public String portion() {
        return _portion;
    }
    
    public boolean empty() {
        return _empty;
    }
    
    public boolean tb() {
        return _tb;
    }
    public String csvString() {
        if (_empty) {
            return String.format("%d,\"No Data\",,,\n",_p);
        }
        return String.format("%d,%s,%s,%s,%s\n", _p, _fuzzyBVF, _TbTh, _TbSp, _TbN);
    }
    
    @Override
    public String toString() {
        if(!_tb) {
            if (_empty) {
                return String.format("P %d,%s,%s,%s\n", _p, _type, _timeline, ((Boolean) _empty).toString());
            }
            return String.format("P %d,%s,%s,%s,%s,%s,%s,%s\n",_p, _type,
                    _timeline, _fuzzyBVF,_TbTh, _TbSp, _TbN, ((Boolean) _empty).toString());
        } else {
            if (_empty) {
                return String.format("P %d,%s,%s,%s,%s\n", _p, _type, _timeline,_portion,
                        ((Boolean) _empty).toString());
            }
            return String.format("P %d,%s,%s,%s,%s,%s,%s,%s,%s\n",_p, _type,
                    _timeline, _fuzzyBVF,_TbTh, _TbSp, _TbN, _portion, 
                    ((Boolean) _empty).toString());
        }
        
    }
    
}
