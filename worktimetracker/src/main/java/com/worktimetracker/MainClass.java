package com.worktimetracker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.worktimetracker.DataClasses.*;
import com.worktimetracker.Export.ExportHandler;
import com.worktimetracker.WorkCalendar.*;

public class MainClass 
{
    private static FileManager fileManager = new FileManager();
    public static void main( String[] args )
    {
        if(args.length < 1){
            System.out.println("Too few arguments. See --help for details regarding the usage.");
        }else{
            switch (args[0]) {
                case "--help", "-help", "-h", "help" -> showHelpPage();
                case "start", "-start", "--start" -> {
                    if(args.length < 2){
                        System.out.println("Project specification needed. See 'list' command to view all active projects");
                    }else{
                        startTimer(args[1]);
                    }
                }
                case "stop", "-stop", "--stop" -> {
                    if(args.length < 2){
                        System.out.println("Project specification needed. See 'list' command to view all active projects");
                    }else{
                        stopTimer(args[1]);
                    }
                }
                case "list", "-list", "--list", "ls", "-l", "l" -> listAllProjects();
                case "month", "-month", "--month" -> {
                    if(args.length < 2){
                        System.out.println("Project specification needed. See 'list' command to view all active projects");
                    }else{
                        if(args.length < 3){
                            displayMonth(args[1], 40, DateTime.now().date().month(), false);
                        }else{
                            if(args.length < 4){
                                displayMonth(args[1], Integer.parseInt(args[2]), DateTime.now().date().month(), false);
                            }else{
                                displayMonth(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), (Integer.parseInt(args[3]) == DateTime.now().date().month()) ? false : true);
                            }
                        }
                    }
                }
                case "week", "-week", "--week" -> {
                    if(args.length < 2){
                        System.out.println("Project specification needed. See 'list' command to view all active projects");
                    }else{
                        if(args.length < 3){
                            displayWeek(args[1], 10, DateTime.now().date().getWeekOfMonth(), DateTime.now().date().month());
                        }else{
                            if(args.length < 4){
                                displayWeek(args[1], Integer.parseInt(args[2]), DateTime.now().date().getWeekOfMonth(), DateTime.now().date().month());
                            }else{
                                if (args.length < 5){
                                    displayWeek(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), DateTime.now().date().month());
                                }else{
                                    displayWeek(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                                }
                            }
                        }
                    }
                }
                case "export", "-export", "--export", "exp", "-exp", "--exp", "e" -> {
                    if(args.length < 2){
                        System.out.println("Project specification needed. See 'list' command to view all active projects");
                    }else{
                        if(args.length < 3){
                            export(args[1], Optional.empty());
                        }else{
                            export(args[1], Optional.of(args[2]));
                        }
                    }
                }
                case "exportCorrected", "-exportCorrected", "--exportCorrected", "expCor", "-expCor", "--expCor", "eC", "ec" -> {
                    if(args.length < 2){
                        System.out.println("Project specification needed. See 'list' command to view all active projects");
                    }else{
                        if(args.length < 3){
                            exportCorrected(args[1], Optional.empty(), 40);
                        }else{
                            if (args.length < 4){
                                exportCorrected(args[1], Optional.of(args[2]), 40);
                            }else{
                                exportCorrected(args[1], Optional.of(args[2]), Integer.parseInt(args[3]));
                            }
                        }
                    }
                }
                default -> System.out.println("Argument unkown. See --help for details regarding the usage.");
            }
        }
    }

    private static void showHelpPage(){
        System.out.println("------------------- Work Time Tracker --------------------\n");
        System.out.println("""
                This is a tool to assist you in keeping track of your work time to not create overtime.
                The field <project> is mandatary for all commands which list that field.

                Commands:
                    help                Shows this page
                    start <project>     Start a work session for given project
                    stop <project>      Stop the current work session for given project
                    list                List all current active projects
                    month <project> <maxHours> <month>        
                                        Display the stats of the current month for given project.
                                        You can specify how many hours you want to work in total. (Default: 40)
                                        You can specify which month to display. (Default: current month)
                    week <project> <maxHours> <weekOfMonth> <month>   
                                        Display the stats of the current week for given project.
                                        You can specify how many hours you want to work in total. (Default 10)
                                        This does not take in consideration that a certain week can be in two different months.
                                        Each week 'part' will count as its own 'week'.
                                        You can specify which week of the current month to display. (Default: current week)
                                        You can also specify the month whose week to disply. (Default: current month)
                    export <project> <directory>  
                                        Export the work time of the current month for given project as .csv file.
                                        You can specify where to put that file. (Default: Downloads folder) 
                    exportCorrected <project> <directory> <totalHours>
                                        Export the corrected version of the work time of the current month for given project as .csv file.
                                        You can specify where to put that file. (Default: Downloads folder).
                                        You can also specify how many work hours you should have had in the current month. (Default: 40)
                """);
        System.out.println("----------------------------------------------------------");
    }

    private static void startTimer(String project){
        try {
            DateTime now = DateTime.now();
            fileManager.storeStart(project, now);
            System.out.println("Started WorkTimeTracker for project "+ANSI_COLORS.ANSI_CYAN+project+ANSI_COLORS.ANSI_RESET+" at "+ANSI_COLORS.ANSI_CYAN+now.toString()+ANSI_COLORS.ANSI_RESET);
        } catch (IOException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }

    }
    private static void stopTimer(String project){
        try {
            DateTime now = DateTime.now();
            fileManager.storeEnd(project, now.time());
            System.out.println("Stopped WorkTimeTracker for project "+ANSI_COLORS.ANSI_CYAN+project+ANSI_COLORS.ANSI_RESET+" at "+ANSI_COLORS.ANSI_CYAN+now.toString()+ANSI_COLORS.ANSI_RESET);
        } catch (URISyntaxException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The old parameter is necessary to set the display as if it were day 1 of the month.
     * Therefore optimal distribution for prior weeks will be visible.
     * @param project project name
     * @param maxHours wished for maximum work hours
     * @param month month to display
     * @param old whether it is not the current one or not
     */
    private static void displayMonth(String project, int maxHours, int month, boolean old){
        try {
            DateTime now = DateTime.now();
            Date date;
            if(old){
                date = new Date(now.date().year(), month, 1);
            }else{
                date = new Date(now.date().year(), month, now.date().day());
            }
            WorkMonth workMonth = new WorkMonth(fileManager.getSessionsOfMonth(project, month), date);
            Period remainingWorkLoad = new Period(maxHours, 0, 0).minusWithLowerEnd(workMonth.getWorkedOffTotal());
            System.out.println("\n-----------------------------------------"+ANSI_COLORS.ANSI_BLUE+"Work Time Tracker"+ANSI_COLORS.ANSI_RESET+"---------------------------------------\n");
            System.out.println("Total work load for "+ ANSI_COLORS.ANSI_CYAN+date.getMonthAsString()+ANSI_COLORS.ANSI_RESET+":\t\t\t"+ANSI_COLORS.ANSI_CYAN+maxHours+ANSI_COLORS.ANSI_RESET+"h");
            System.out.println("Already worked off:\t\t\t\t"+workMonth.getWorkedOffTotal().toStringOrDash(ANSI_COLORS.ANSI_GREEN));
            System.out.println("Still have to work off:\t\t\t\t"+remainingWorkLoad.toStringOrDash(ANSI_COLORS.ANSI_RED));
            if(remainingWorkLoad.isZero()) System.out.println(ANSI_COLORS.ANSI_GREEN+"Monthly duties fulfilled. Relax!"+ANSI_COLORS.ANSI_RESET);
            System.out.println("Weekly Distribution:");
            System.out.println(workMonth.getDistributionForWorkedOffOfOptimalAsString(new Period(maxHours, 0, 0)));
            System.out.println();
            if(!remainingWorkLoad.isZero()){
                System.out.println("Weekly distribution for remaining work:\t\t");
                System.out.println(workMonth.getDistributionForRemainingWorkLoadAsString(remainingWorkLoad));
                System.out.println();
            }
            System.out.println("-------------------------------------------------------------------------------------------------");
        } catch (IOException | URISyntaxException e) {
            System.out.println(ANSI_COLORS.ANSI_RED+ e.getMessage() + ANSI_COLORS.ANSI_RESET);
        }
    }

    private static void displayWeek(String project, int maxHours, int week, int month){
        try{
            DateTime now = DateTime.now();
            Date date;
            if(now.date().getWeekOfMonth() == week && now.date().month() == month){
                date = new Date(now.date().year(), month, now.date().day());

            }else {
                date = new Date(now.date().year(), month, Date.getFirstDayOfWeek(now.date().year(), month, week));

            }
            WorkWeek workWeek = new WorkWeek(fileManager.getSessionsOfMonth(project, month), week, date);
            Period remainingWorkLoad = new Period(maxHours, 0, 0).minusWithLowerEnd(workWeek.getWorkedOffByWeek(week));
            System.out.println("\n-----------------------------------------"+ANSI_COLORS.ANSI_BLUE+"Work Time Tracker"+ANSI_COLORS.ANSI_RESET+"-----------------------------------------------------------------------------\n");
            System.out.println("Total work load for "+ ANSI_COLORS.ANSI_CYAN+date.getMonthAsString()+ANSI_COLORS.ANSI_RESET+", Week "+ANSI_COLORS.ANSI_CYAN + week + ANSI_COLORS.ANSI_RESET+":\t\t"+ANSI_COLORS.ANSI_CYAN+maxHours+ANSI_COLORS.ANSI_RESET+"h");
            System.out.println("Already worked off:\t\t\t\t"+workWeek.getWorkedOffByWeek(week).toStringOrDash(ANSI_COLORS.ANSI_GREEN));
            System.out.println("Still have to work off:\t\t\t\t"+remainingWorkLoad.toStringOrDash(ANSI_COLORS.ANSI_RED));
            if(remainingWorkLoad.isZero()) System.out.println(ANSI_COLORS.ANSI_GREEN+"Weekly duties fulfilled. Relax!"+ANSI_COLORS.ANSI_RESET);
            System.out.println("Daily Distribution:");
            //System.out.println(workWeek.getDistributionForWorkedOffOfOptimalAsString(new Period(maxHours, 0, 0)));
            System.out.println(workWeek.getDistributionChart());
            System.out.println();
            System.out.println(workWeek.getDistributionForWorkedOffOfOptimalAsString(new Period(maxHours, 0, 0)));
            System.out.println();
            if(!remainingWorkLoad.isZero()){
                System.out.println("Daily distribution for remaining work:\t\t");
                System.out.println(workWeek.getDistributionForRemainingWorkLoadAsString(remainingWorkLoad));
            }
            System.out.println();
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
        }catch (IOException | IllegalArgumentException | URISyntaxException e){
            System.out.println(ANSI_COLORS.ANSI_RED + e.getMessage() + ANSI_COLORS.ANSI_RESET);
        }
    }

    private static void export(String project, Optional<String> directory){
        try{
            DateTime now = DateTime.now();

            new ExportHandler(fileManager.getSessionsOfMonth(project, now.date().month())).export(directory.map((s) -> Path.of(s)));
        }catch(IOException | URISyntaxException e){
            System.out.println(ANSI_COLORS.ANSI_RED + e.getMessage() + ANSI_COLORS.ANSI_RESET);

        }
    }

    private static void exportCorrected(String project, Optional<String> directory, int totalHours){
        try{
            DateTime now = DateTime.now();

            new ExportHandler(fileManager.getSessionsOfMonth(project, now.date().month())).exportCorrected(directory.map(s -> Path.of(s)), totalHours);
        }catch(IOException | URISyntaxException e){
            System.out.println(ANSI_COLORS.ANSI_RED + e.getMessage() + ANSI_COLORS.ANSI_RESET);

        }
    }

    private static void listAllProjects(){
        try {
            List<String> projects = fileManager.listAllProjects();
            System.out.println(ANSI_COLORS.ANSI_CYAN+"Active projects:"+ANSI_COLORS.ANSI_RESET);
            for (String p : projects) {
                System.out.println(" - "+p);
            }
        } catch (URISyntaxException e) {
            System.out.println(ANSI_COLORS.ANSI_RED + e.getMessage() + ANSI_COLORS.ANSI_RESET);
        }
        
    }
}
