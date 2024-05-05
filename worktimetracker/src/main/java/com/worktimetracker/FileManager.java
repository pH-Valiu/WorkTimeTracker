package com.worktimetracker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.DataClasses.DateTime;
import com.worktimetracker.DataClasses.Time;
import com.worktimetracker.DataClasses.WorkSession;

public class FileManager {
    
    public void storeStart(DateTime starDateTime) throws IOException, URISyntaxException{
        createFolderIfNotExist();

        //Get (and or create if necessary) the save file
        String fileName = starDateTime.toString("YYYY-MM");
        Path monthsFilePath = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months\\"+fileName);
        
        //write into file "\nYYYY-MM-DD hh:mm:ss"
        BufferedWriter writer = new BufferedWriter(new FileWriter(monthsFilePath.toString(), true));
        writer.newLine();
        writer.append(starDateTime.toString("YYYY-MM-DD hh:mm:ss"));
        writer.flush();
        writer.close();
    }
    
    public void storeEnd(Time end) throws IOException, URISyntaxException{
        //try to get the newest, most recently used file
        Optional<String> file = getNewestFile();

        //assure that it has been found
        if(file.isEmpty()){
            throw new IOException("No file found. Please start the timer first.");
        }else{
            //Get the file
            Path monthsFilePath = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months\\"+file.get());
            
            //Append to file "Ohh:mm:ss"
            BufferedWriter writer = new BufferedWriter(new FileWriter(monthsFilePath.toString(), true));
            writer.append("O"+end.toString("hh:mm:ss"));
            writer.flush();
            writer.close();
        }
    }

    public List<WorkSession> getSessionsOfMonth() throws IOException, URISyntaxException{
        createFolderIfNotExist();

        //Get file of month
        String fileName = DateTime.now().toString("YYYY-MM");
        Path file = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months\\"+fileName);
        
        //Read all lines
        List<String> lines = Files.readAllLines(file);
        
        //Try and convert each line into a WorkSession object by spliiting the line with delimiter "O" to get start and end.
        List<WorkSession> workSessions = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("O");
            if(parts.length == 2){
                workSessions.add(new WorkSession(DateTime.fromString(parts[0], "YYYY-MM-DD hh:mm:ss"), Time.fromString(parts[1], "hh:mm:ss")));
            }
        }
        return workSessions;
    }


    //helper functions

    private void createFolderIfNotExist() throws IOException, URISyntaxException{
        Path folder = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months");
        Files.createDirectories(folder);
    }

    /**
     * This method tries to get the newest save file.
     * It retrieves all filenames and sorts them based on their name
     * @return Optional containing the name of the file if there was one to begin with
     * @throws IOException
     * @throws URISyntaxException
     */
    private Optional<String> getNewestFile() throws IOException, URISyntaxException   {
        createFolderIfNotExist();
        
        // "/Month" folder as File object
        File folder = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months").toFile();
        
        //Stream the fileNames of all files in the folder
        //Sort them based on following directive
        //Try and get the first element
        return Stream.of(folder.listFiles())
            .sorted((arg0, arg1) -> {
                    //größer (1) heißt älter in der vergangenheit
                    //kleinstes (-1) ist das jüngste oder die Zukunft
                    Date d1 = Date.fromYearMonthString(arg0.getName());
                    Date d2 = Date.fromYearMonthString(arg1.getName());

                    int res = d1.compareTo(d2);
                    if(res == 1)        res = -1;
                    else if(res == -1)  res = 1;
                    else                res = 0;
                    return res;
                })

            .findFirst()
            .map((it) -> it.getName());
    }
}
