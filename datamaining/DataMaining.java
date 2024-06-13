package datamaining;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataMaining {
    public static void main(String[] args) {
        String filename = "src/datamaining/heart.csv";
        String[] header = null;
        double[][] data = null;
        
        // BACA FILE
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);

            // Baca header
            String baris = sc.nextLine();
            header = baris.split(",");
            // System.out.println(Arrays.toString(header));    

            // Baca baris data
            ArrayList<String[]> dataString = new ArrayList<>();
            while (sc.hasNextLine()) {
                baris = sc.nextLine();
                String[] kolom = baris.split(",");
                if (kolom.length == header.length) {
                    dataString.add(kolom);
                }
            }

            // Transformasi data
            double[][] dataDouble = new double[dataString.size()][header.length];
            for (int i = 0; i < dataDouble.length; i++) {
                String[] kolom = dataString.get(i);

                // Age
                String value = kolom[0];
                double age = Double.parseDouble(value);
                dataDouble[i][0] = age;
                
               // sex
                value = kolom[1];
                double sex = Double.parseDouble(value);
                dataDouble[i][1] = sex;
                
                // cp
                value = kolom[2];
                double cp = Double.parseDouble(value);
                dataDouble[i][2] = cp;
                
                // trestbps
                value = kolom[3];
                double trestbps = Double.parseDouble(value);
                dataDouble[i][3] = trestbps;
                
                // chol
                value = kolom[4];
                double chol = Double.parseDouble(value);
                dataDouble[i][4] = chol;
                
                // fbs
                value = kolom[5];
                double fbs = Double.parseDouble(value);
                dataDouble[i][5] = fbs;
                
                // restecg
                value = kolom[6];
                double restecg = Double.parseDouble(value);
                dataDouble[i][6] = restecg;
                
                // thalach
                value = kolom[7];
                double thalach = Double.parseDouble(value);
                dataDouble[i][7] = thalach;
                
                // exang
                value = kolom[8];
                double exang = Double.parseDouble(value);
                dataDouble[i][8] = exang;
                
                // oldpeak
                value = kolom[9];
                double oldpeak = Double.parseDouble(value);
                dataDouble[i][9] = oldpeak;
                
                // slope
                value = kolom[10];
                double slope = Double.parseDouble(value);
                dataDouble[i][10] = slope;
                
                // ca
                value = kolom[11];
                double ca = Double.parseDouble(value);
                dataDouble[i][11] = ca;
                
                // thal
                value = kolom[12];
                double thal = Double.parseDouble(value);
                dataDouble[i][12] = thal;
                
                // target
                value = kolom[13];
                double target = Double.parseDouble(value);
                dataDouble[i][13] = target;
                
                
            }

            // Min-Max
            double[] min = new double[header.length];
            double[] max = new double[header.length];
            for (int j = 0; j < header.length; j++) {
                min[j] = Double.MAX_VALUE;
                max[j] = Double.MIN_VALUE;
            }
            for (int i = 0; i < dataDouble.length; i++) {
                for (int j = 0; j < dataDouble[i].length; j++) {
                    double value = dataDouble[i][j];
                    if (value < min[j]) {
                        min[j] = value;
                    }
                    if (value > max[j]) {
                        max[j] = value;
                    }
                }
            }

            // Normalisasi
            data = new double[dataDouble.length][header.length];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    double value = dataDouble[i][j];
                    double normalValue = (value - min[j]) / (max[j] - min[j]);
                    data[i][j] = normalValue;
                }
            }
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    System.out.print(data[i][j]+" ");
                }
                System.out.println("");
            }
            

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        
        // SPLIT DATA TRAINING DAN DATA TESTING
        int numData = 0;
        int numDataTraining = 0;
        int numDataTesting = 0;
        double percentDataTraining = 90;
        double percentDataTesting = 10;
        double[][] dataTraining = null;
        double[][] dataTesting = null;
        if (header != null && data != null) {
            double totalPercent = percentDataTraining + percentDataTesting;
            numData = data.length;
            numDataTraining = (int) Math.floor(numData * (percentDataTraining / totalPercent));
            numDataTesting = (int) Math.floor(numData * (percentDataTesting / totalPercent));
            dataTraining = new double[numDataTraining][];
            for (int t = 0; t < dataTraining.length; t++) {
                dataTraining[t] = data[t];
            }
            // set data testing
            dataTesting = new double[numDataTesting][];
            for (int t = 0; t < dataTesting.length; t++) {
                dataTesting[t] = data[t + dataTraining.length];
            }
        }
        
        // proses KNN
        
        int K = 3;
        double[][] result = new double[dataTesting.length][2];

        for (int i = 0; i < dataTesting.length; i++) {
            double[][] dataJarak = new double[dataTraining.length][3];
            double id = i;
            for (int j = 0; j < dataTraining.length; j++) {
                double sum = 0;
                for (int k = 0; k < dataTraining[i].length - 1; k++) {
                    double x2x1 = dataTraining[j][k] - dataTesting[i][k];
                    x2x1 = Math.pow(x2x1, 2);
                    sum += x2x1;
                }
                sum = Math.sqrt(sum);
                dataJarak[j][0] = j;
                dataJarak[j][1] = sum;
                dataJarak[j][2] = dataTraining[j][dataTraining[0].length - 1];
            }

            //Sorting
            for (int j = 0; j < dataJarak.length - 1; j++) {
                int i_MIN = j;
                double value = dataJarak[j][1];
                for (int k = i + 1; k < dataJarak.length; k++) {
                    if (value > dataJarak[k][1]) {
                        i_MIN = k;
                        value = dataJarak[k][1];
                    }
                }
                if (i_MIN != j) {
                    double temp_id = dataJarak[j][0];
                    double temp_value = dataJarak[j][0];
                    double temp_class = dataJarak[j][2];

                    dataJarak[j][0] = dataJarak[i_MIN][0];
                    dataJarak[j][1] = dataJarak[i_MIN][1];
                    dataJarak[j][2] = dataJarak[i_MIN][2];

                    dataJarak[i_MIN][0] = temp_id;
                    dataJarak[i_MIN][1] = temp_value;
                    dataJarak[i_MIN][2] = temp_class;
                }
            }

            double[][] k_dataJarak = new double[K][3];
            for (int j = 0; j < K; j++) {
                k_dataJarak[j][0] = dataJarak[j][0];
                k_dataJarak[j][1] = dataJarak[j][1];
                k_dataJarak[j][2] = dataJarak[j][2];
            }

            int safe = 0;
            int not_safe = 0;
            for (int j = 0; j < k_dataJarak.length; j++) {
                if (k_dataJarak[j][2] == 1.0) {
                    safe++;
                } else if (k_dataJarak[j][2] == 0.0) {
                    not_safe++;
                }
            }

            double predict = -1;
            if (safe > not_safe) {
                predict = 1.0;
            } else {
                predict = 0.0;
            }

            result[i][0] = id;
            result[i][1] = predict;
        }
        double TP = 0;
        double FP = 0;
        double FN = 0;
        double TN = 0;

        for (int i = 0; i < dataTesting.length; i++) {
            double actualValue = dataTesting[i][dataTesting[0].length - 1];
            double predictValue = result[i][1];
            if (actualValue == 1.0 && predictValue == 1.0) {
                TP++;
            } else if (actualValue == 0.0 && predictValue == 1.0) {
                FP++;
            } else if (actualValue == 1.0 && predictValue == 0.0){
                FN++;
            } else if (actualValue == 0.0 && predictValue == 0.0){
                TN++;
            }
        }
        System.out.println("Jumlah Data Testing : "+result.length);
        double akurasi = Math.ceil((TP+TN)/(TN+FP+FN+TP)*100);
        double precision = Math.ceil((TP/(TP+FP))*100);
        double recall = Math.ceil((TP/(TP+FN))*100);
        double F_Measure = Math.ceil((2*recall*precision)/(recall+precision));
        System.out.println("----------------------------------------------------");
        for (int i = 0; i < result.length; i++) {
            System.out.println(dataTesting[i][dataTesting[0].length-1]+" - "+result[i][1]);
        }
        
        System.out.println("TP : "+TP);
        System.out.println("FP : "+FP);
        System.out.println("FN : "+FN);
        System.out.println("TN : "+TN);
        System.out.println("Akurasi : "+akurasi);
        System.out.println("Precision : "+precision);
        System.out.println("Recall : "+recall);
        System.out.println("F_Measure : "+F_Measure);
    
    
    }
    
}