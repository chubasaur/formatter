package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class runTbFormatter {
    /** Todo: Write something that automatically swaps out patient records.
     *  Fix Tibia*/
    
    private static boolean tb = false; 
    private static boolean replace = false;

    public static void main(String []args) throws IOException{
        File file = null;
        BufferedReader reader = null;
        if (args.length != 3  || args.length != 5) {
            System.out.printf("Usage: runTbFormatter -tn -r [tbDataFile] [replacementTbData] [saveDestination]\n\n" +
            		"\t-t = tibia" +
            		"\n\t-n = not tibia" +
            		"\n\t-r = replace, default is do not replace");
            
        } else {
            try {
                file = new File(args[0]);
                reader = new BufferedReader(new FileReader(file));
                if (args[0].equals("-t")) {
                    tb = true;
                }

                TbFormatter formatter = new TbFormatter(reader);
                formatter.setTb(tb);

                if (args[1].equals("-r")) {
                    replace = true;
                    formatter.setUpdate(replace);
                    BufferedReader replacementReader = new BufferedReader(new FileReader(new File(args[4])));
                    formatter.setReplacement(replacementReader);
                }
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
