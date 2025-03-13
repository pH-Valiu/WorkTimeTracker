package com.worktimetracker.Export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import com.worktimetracker.DataClasses.DateTime;
import com.worktimetracker.DataClasses.Pair;
import com.worktimetracker.DataClasses.Period;
import com.worktimetracker.DataClasses.Time;
import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.DataClasses.WorkSession;

public record ExportHandler(List<WorkSession> workSessions) {

    public void export(Optional<Path> path) throws IOException{
        writeToFile(path, workSessions);
    }
    
    public void exportCorrected(Optional<Path> path, int totalHours, int month, int year) throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append("sep=,\n");
        sb.append("Tag,Zeitraum von,Bis,Zeit\n");

        List<Date> workDays = getWorkDaysOfMonth(Optional.of(new Date(year, month, 1)));
        Period totalWorkLoad = new Period(totalHours, 0, 0);
        Random rnd = new Random();

        Map<Integer, WorkSession> sessions = new HashMap<>();
    
        while(!totalWorkLoad.isZero()){
            for (int i = 0; i < workDays.size(); i++) {
                Pair<Period, Period> pair = randomSubset(totalWorkLoad, totalWorkLoad.divideBy(workDays.size()).hours() + 2, 15);
                totalWorkLoad = pair.first();
    
                int startOffset = 8 + rnd.nextInt(3);
                
                WorkSession ses = sessions.get(i);
                if(ses != null){
                    sessions.put(i, new WorkSession(
                        ses.start(), 
                        ses.end().plusTime(pair.second().hours(), pair.second().minutes(), pair.second().seconds())));
                }else{
                    sessions.put(i, new WorkSession(
                        new DateTime(workDays.get(i), new Time(startOffset, 0, 0)), 
                        new Time(pair.second()).plusTime(startOffset, 0, 0)));
                }
            }
        }

        writeToFile(path, sessions.entrySet().stream().map(it -> it.getValue()).toList());
    }


    private List<Date> getWorkDaysOfMonth(Optional<Date> date){
        Date base = date.orElse(DateTime.now().date());
        List<Date> dates = new ArrayList<>();

        for (int i = 0; i < base.getTotalDaysOfMonth(); i++) {
            Date d = new Date(base.year(), base.month(), i+1);
            if(d.getDayOfWeek() <= 5 ){
                dates.add(d);
            }
        }

        return dates;
    }

    private void writeToFile(Optional<Path> path, List<WorkSession> workSessions) throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append("sep=,\n");
        sb.append("Tag,Zeitraum von,Bis,Zeit\n");

        for (WorkSession workSession : workSessions) {
            String csv = workSession.toCSVString();
            if(csv != null){
                sb.append(workSession.toCSVString()).append("\n");
            }
        }

        File outputFile = path.orElse(Path.of(System.getProperty("user.home")+File.separator+"Downloads"+File.separator+"export"+DateTime.now().toString("YYYY-MM")+".csv")).toFile();
        FileWriter writer = new FileWriter(outputFile);
        writer.append(sb.toString());
        writer.flush();
        writer.close();
        //System.out.println(sb.toString());
    }

    private Pair<Period, Period> randomSubset(Period base, int maxHours, int minutesInterval){
        Random rnd = new Random();
        int offsetHours = rnd.nextInt(maxHours);
        int offsetMinutes = rnd.nextInt(3) * minutesInterval;

        Period subset = new Period(offsetHours, offsetMinutes, 0);
        if ( base.compareTo(subset) >= 0){
            Period newRemaining = base.minusWithLowerEnd(subset);
            return new Pair<>(newRemaining, subset);
        }else{
            return new Pair<>(new Period(0,0,0), base);
        }
    }
}
