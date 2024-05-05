package com.worktimetracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.DataClasses.DateTime;
import com.worktimetracker.DataClasses.Time;
import com.worktimetracker.DataClasses.WorkSession;

public class FileManager {
    
    public void storeStart(DateTime starDateTime) throws IOException, URISyntaxException{
        createFolderIfNotExist();
        String fileName = starDateTime.toString("YYYY-MM");
        Path monthsFilePath = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months\\"+fileName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(monthsFilePath.toString(), true));
        writer.newLine();
        writer.append(starDateTime.toString("YYYY-MM-DD hh:mm:ss"));
        writer.flush();
        writer.close();
    }
    
    public void storeEnd(Time end) throws IOException, URISyntaxException{
        Optional<String> file = getNewestFile();
        if(file.isEmpty()){
            throw new IOException("No file found. Please start the timer first.");
        }else{
            Path monthsFilePath = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months\\"+file.get());
            BufferedWriter writer = new BufferedWriter(new FileWriter(monthsFilePath.toString(), true));
            writer.append("O"+end.toString("hh:mm:ss"));
            writer.flush();
            writer.close();
        }
    }

    public List<WorkSession> getSessionsOfMonth() throws IOException, URISyntaxException{
        createFolderIfNotExist();
        String fileName = DateTime.now().toString("YYYY-MM");
        Path file = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months\\"+fileName);
        List<String> lines = Files.readAllLines(file);
        List<WorkSession> workSessions = new ArrayList<>();
        for (String line : lines) {
            System.out.println(line);
            String[] parts = line.split("O");
            if(parts.length == 2){
                workSessions.add(new WorkSession(DateTime.fromString(parts[0], "YYYY-MM-DD hh:mm:ss"), Time.fromString(parts[1], "hh:mm:ss")));
            }
        }
        return workSessions;
    }

    private void createFolderIfNotExist() throws IOException, URISyntaxException{
        Path folder = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months");
        Files.createDirectories(folder);
    }

    private Optional<String> getNewestFile() throws IOException, URISyntaxException   {
        createFolderIfNotExist();
        File folder = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months").toFile();
        System.out.println(folder.listFiles());
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
