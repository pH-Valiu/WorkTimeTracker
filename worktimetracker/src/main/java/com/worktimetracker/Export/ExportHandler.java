package com.worktimetracker.Export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import com.worktimetracker.DataClasses.DateTime;
import com.worktimetracker.DataClasses.Period;
import com.worktimetracker.DataClasses.Time;
import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.DataClasses.WorkSession;

public record ExportHandler(List<WorkSession> workSessions) {

    public void export(Optional<Path> path) throws IOException{
        writeToFile(path, workSessions);
    }
    
    public void exportCorrected(Optional<Path> path, int totalHours) throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append("sep=,\n");
        sb.append("Tag,Zeitraum von,Bis,Zeit\n");

        List<Date> workDays = getWorkDaysOfMonth();
        Period perDayWorkLoad = new Period(totalHours, 0, 0).divideBy(workDays.size());

        List<WorkSession> sessions = workDays.stream().map((date) -> 
            new WorkSession(
                new DateTime(date, new Time(8, 0, 0)), 
                new Time(perDayWorkLoad).plusTime(8, 0, 0))).toList();

        writeToFile(path, sessions);
    }


    private List<Date> getWorkDaysOfMonth(){
        DateTime now = DateTime.now();
        List<Date> dates = new ArrayList<>();

        for (int i = 0; i < now.date().getTotalDaysOfMonth(); i++) {
            Date date = new Date(now.date().year(), now.date().month(), i+1);
            if(date.getDayOfWeek() <= 5 ){
                dates.add(date);
            }
        }

        return dates;
    }

    private void writeToFile(Optional<Path> path, List<WorkSession> workSessions) throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append("sep=,\n");
        sb.append("Tag,Zeitraum von,Bis,Zeit\n");

        for (WorkSession workSession : workSessions) {
            sb.append(workSession.toCSVString()).append("\n");
        }

        File outputFile = path.orElse(Path.of(System.getProperty("user.home")+File.separator+"Downloads"+File.separator+"export"+DateTime.now().toString("YYYY-MM")+".csv")).toFile();
        FileWriter writer = new FileWriter(outputFile);
        writer.append(sb.toString());
        writer.flush();
        writer.close();
        System.out.println(sb.toString());
    }
}
