package Workload;
import Entity.Job;
import Manager.Controller;
import Settings.Configurations;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {

    public static ArrayList<Long> jobArrivalTimes = new ArrayList<>();

    public static void parseJobs() {

        BufferedReader br = null;
        String line;
        // use comma as a separator in CSV file
        String csvSplitBy = ",";

        try {
            String workloadFileStr;
            if(Configurations.jobArrival==1)
            {
                workloadFileStr="/jobs_drl.csv";
            }
            else
            {
                workloadFileStr="/jobs_drl_burst.csv";
            }
            br = new BufferedReader(new FileReader(Configurations.simulatorHome +workloadFileStr));
            //for skipping the header row
            br.readLine();
            //jobs
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] parsedStr = line.split(csvSplitBy);
                Job job = new Job();
                job.setJobID(parsedStr[0]);
                job.setC(Integer.parseInt(parsedStr[1]));
                job.setM(Integer.parseInt(parsedStr[2]));
                job.setE(Integer.parseInt(parsedStr[3]));
                job.setT_A(Long.parseLong(parsedStr[4]));
                job.setT_est(Long.parseLong(parsedStr[5]));
                job.setT_D(Long.parseLong(parsedStr[6]));
                job.setType(Integer.parseInt(parsedStr[7]));
                job.setT_W(0);
                Controller.jobList.add(job);
                i++;
            }
            Configurations.jobTotal=i;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
