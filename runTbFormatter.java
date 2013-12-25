package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



/** Read in a csv 3 lines at a time:
 * For each group of three input lines: 
 * 1) Collect lat, med, whole values for Volume, Th, Sp, Number into arrays.
 * 2) concatenate them together (lat, med, whole)
   3) organize them by P number and category (baselineControl, control 1 year, aclBaseline, ACL contra, ACL 1year, ACL 1year contra.
   4) write CSV's that produce desired output
*/

public class runTbFormatter {
    private static boolean tb = false; //When running in TB mode, in addition to making sure timeline is b,1, etc.
    //need to make sure Lateral => lat, etc. whole, med; P192 1year has a space after whole that needs to be fixed
    public static void main(String []args) throws IOException{
        args = new String[2];
        //args[0] = "/Users/paul/Downloads/TbAnalysis_Fuzzy_Femur.csv";
        //args[1] = "/Users/paul/Downloads/SavedTbAnalyis_Fuzzy_Femur.csv";
        //args[0] = "/Users/paul/Downloads/TbAnalysis_Fuzzy_Femur(CSV).csv";
        //args[1] = "/Users/paul/Downloads/SavedFemur.csv";
        //args[0] = "/Users/paul/Downloads/TbAnalysis_Fuzzy__LFM(CSV).csv";
        //args[1] = "/Users/paul/Downloads/SavedLFM.csv";
        args[0] = "/Users/paul/Downloads/TbAnalysis_Fuzzy_MFM_(CSV).csv";
        args[1] = "/Users/paul/Downloads/SavedMFM.csv";
        //args[0] = "/Users/paul/Downloads/TbAnalysis_Fuzzy_Tibia(CSV).csv";
        //args[1] = "/Users/paul/Downloads/SavedTibia.csv";
        File file = null;
        BufferedReader reader = null;
        if (args.length != 2) {
            System.out.printf("Usage: runTbFormatter [csvFileName] [saveDestination\n");
        } else {
            try {
                file = new File(args[0]);
                reader = new BufferedReader(new FileReader(file));
                TbFormatter formatter = new TbFormatter(reader);
                formatter.setTb(tb);
                formatter.buildPatientLists(); //extra parameters and build csv's as string arrays to be written
                File fileW = new File(args[1]);//file to write to
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileW));
                    String[] writeList = formatter.lists().getControlBList();
                    if(tb) {
                        writer.write("Control Baseline,,,,,,,,,,,\n\"P\",,\"FuzzyBVF\",," +
                        		",\"TbTh\",,,\"TbSp\",,,\"TbN\",\n" +
                        		",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\"\n");
                    } else {
                        writer.write("Control Baseline,,,,\n\"P\",\"FuzzyBVF\",\"TbTh\"" +
                        		",\"TbSp\",\"TbN\"\n");
                    }
                    for(int i = 0; i < writeList.length; i++) {
                        writer.write(writeList[i]);
                    }
                    writer.write(",,,,\n"); //Write a Separator;
                    writer.flush();
                    writeList = formatter.lists().getControl1List();
                    if(tb) {
                        writer.write("Control 1-Year,,,,,,,,,,,\n\"P\",,\"FuzzyBVF\"," +
                                ",\"TbTh\",,\"TbSp\",,\"TbN\",\n" +
                                ",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\"\n");
                    } else {
                        writer.write("Control 1-Year,,,,\n\"P\",\"FuzzyBVF\",\"TbTh\"" +
                                ",\"TbSp\",\"TbN\"\n");
                    }
                    for(int i = 0; i < writeList.length; i++) {
                        writer.write(writeList[i]);
                    }
                    writer.write(",,,,\n"); //Write a Separator;
                    writer.flush();
                    writeList = formatter.lists().getACLBList();
                    if(tb) {
                        writer.write("ACL Baseline,,,,,,,,,,,\n\"P\",,\"FuzzyBVF\"," +
                                ",\"TbTh\",,\"TbSp\",,\"TbN\",\n" +
                                ",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\"\n");
                    } else {
                        writer.write("ACL Baseline,,,,\n\"P\",\"FuzzyBVF\",\"TbTh\"" +
                                ",\"TbSp\",\"TbN\"\n");
                    }
                    for(int i = 0; i < writeList.length; i++) {
                        writer.write(writeList[i]);
                    }
                    writer.write(",,,,\n"); //Write a Separator;
                    writer.flush();
                    writeList = formatter.lists().getACLBContraList();
                    if(tb) {
                        writer.write("ACL Baseline Contra,,,,,,,,,,,\n\"P\",,\"FuzzyBVF\"," +
                                ",\"TbTh\",,\"TbSp\",,\"TbN\",\n" +
                                ",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\"\n");
                    } else {
                        writer.write("ACL Baseline Contra,,,,\n\"P\",\"FuzzyBVF\",\"TbTh\"" +
                                ",\"TbSp\",\"TbN\"\n");
                    }
                    for(int i = 0; i < writeList.length; i++) {
                        writer.write(writeList[i]);
                    }
                    writer.write(",,,,\n"); //Write a Separator;
                    writer.flush();
                    writeList = formatter.lists().getACL1List();
                    if(tb) {
                        writer.write("ACL 1-Year,,,,,,,,,,,\n\"P\",,\"FuzzyBVF\"," +
                                ",\"TbTh\",,\"TbSp\",,\"TbN\",\n" +
                                ",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\"\n");
                    } else {
                        writer.write("ACL 1-Year,,,,\n\"P\",\"FuzzyBVF\",\"TbTh\"" +
                                ",\"TbSp\",\"TbN\"\n");
                    }
                    for(int i = 0; i < writeList.length; i++) {
                        writer.write(writeList[i]);
                    }
                    writer.write(",,,,\n"); //Write a Separator;
                    writer.flush();
                    writeList = formatter.lists().getACL1ContraList();
                    if (tb) {
                        writer.write("ACL 1-Year Contra,,,,,,,,,,,\n\"P\",,\"FuzzyBVF\"," +
                                ",\"TbTh\",,\"TbSp\",,\"TbN\",\n" +
                                ",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\",\"lat\",\"med\",\"whole\"\n");
                    } else {
                        writer.write("ACL 1-Year Contra,,,,\n\"P\",\"FuzzyBVF\",\"TbTh\"" +
                                ",\"TbSp\",\"TbN\"\n");
                    }
                    for(int i = 0; i < writeList.length; i++) {
                        writer.write(writeList[i]);
                    }
                    writer.write(",,,,\n"); //Write a Separator;
                    writer.flush();
                    writer.close();
                
            } catch (FileNotFoundException err) {
                System.out.printf("runTbFormatter was unable to open %s.", args[0]);
            }
            
        }
            
        
    }
}
