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
                            displayMonth(args[1], 40, DateTime.now().date().month(), DateTime.now().date().year());
                        }else{
                            if(args.length < 4){
                                displayMonth(args[1], Integer.parseInt(args[2]), DateTime.now().date().month(), DateTime.now().date().year());
                            }else{
                                if(args.length < 5){
                                    displayMonth(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), DateTime.now().date().year());
                                }else{
                                    displayMonth(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                                }
                            }
                        }
                    }
                }
                case "week", "-week", "--week" -> {
                    if(args.length < 2){
                        System.out.println("Project specification needed. See 'list' command to view all active projects");
                    }else{
                        if(args.length < 3){
                            displayWeek(args[1], 10, DateTime.now().date().getWeekOfMonth(), DateTime.now().date().month(), DateTime.now().date().year());
                        }else{
                            if(args.length < 4){
                                displayWeek(args[1], Integer.parseInt(args[2]), DateTime.now().date().getWeekOfMonth(), DateTime.now().date().month(), DateTime.now().date().year());
                            }else{
                                if (args.length < 5){
                                    displayWeek(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), DateTime.now().date().month(), DateTime.now().date().year());
                                }else{
                                    if (args.length < 6){
                                        displayWeek(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), DateTime.now().date().year());
                                    }else{
                                        displayWeek(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    }
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
                            export(args[1], Optional.empty(), DateTime.now().date().month(), DateTime.now().date().year());
                        }else{
                            if(args.length < 4){
                                export(args[1], Optional.of(args[2]), DateTime.now().date().month(), DateTime.now().date().year());
                            }else{
                                if(args.length < 5){
                                    export(args[1], Optional.of(args[2]), Integer.parseInt(args[3]), DateTime.now().date().year());
                                }else{
                                    export(args[1], Optional.of(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                                }
                            }
                        }
                    }
                }
                case "exportCorrected", "-exportCorrected", "--exportCorrected", "expCor", "-expCor", "--expCor", "eC", "ec" -> {
                    if(args.length < 2){
                        System.out.println("Project specification needed. See 'list' command to view all active projects");
                    }else{
                        if(args.length < 3){
                            exportCorrected(args[1], Optional.empty(), 40, DateTime.now().date().month(), DateTime.now().date().year());
                        }else{
                            if (args.length < 4){
                                exportCorrected(args[1], Optional.of(args[2]), 40, DateTime.now().date().month(), DateTime.now().date().year());
                            }else{
                                if(args.length < 5){
                                    exportCorrected(args[1], Optional.of(args[2]), Integer.parseInt(args[3]), DateTime.now().date().month(), DateTime.now().date().year());
                                }else{
                                    if(args.length < 6){
                                        exportCorrected(args[1], Optional.of(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), DateTime.now().date().year());
                                    }else{
                                        exportCorrected(args[1], Optional.of(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                                    }
                                }
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
                When calling 'start' and adding a project name, a new project will automatically be created
                The field <project> is mandatary for all commands which list that field.

                Commands:
                    help                Shows this page

                    list                List all current active projects

                    start <project>     Start a work session for given project

                    stop <project>      Stop the current work session for given project
                    
                    month <project> <maxHours> <month> <year>
                                        Display the stats of the current month for given project.
                                        You can specify how many hours you want to work in total. (Default: 40)
                                        You can specify which month to display. (Default: current month)
                                        You can specify the year whose month to display. (Default: current year)

                    week <project> <maxHours> <weekOfMonth> <month> <year>
                                        Display the stats of the current week for given project.
                                        You can specify how many hours you want to work in total. (Default 10)
                                        This does not take in consideration that a certain week can be in two different months.
                                        Each week 'part' will count as its own 'week'.
                                        You can specify which week of the current month to display. (Default: current week)
                                        You can specify the month whose week to disply. (Default: current month)
                                        You can specify the year whose month to display. (Default: current year)

                    export <project> <directory> <month> <year>
                                        Export the work time of the current month for given project as .csv file.
                                        You can specify where to put that file. (Default: Downloads folder) 
                                        You can specify the month to export. (Default: current month)
                                        You can specify the year whose month to export. (Default: current year)

                    exportCorrected <project> <directory> <totalHours> <month> <year>
                                        Export the corrected version of the work time of the current month for given project as .csv file.
                                        You can specify where to put that file. (Default: Downloads folder).
                                        You can specify how many work hours you should have had in the current month. (Default: 40)
                                        You can specify the month to export. (Default: current month)
                                        You can specify the year whose month to export. (Default: current year)
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
     * Display weeks of specifed month
     * @param project project name
     * @param maxHours wished for maximum work hours
     * @param month month to display
     * @param yaer year to display
     */
    private static void displayMonth(String project, int maxHours, int month, int year){
        try {
            DateTime now = DateTime.now();
            Date date;

            // check whether we display an old month.
            // if so (first clause) set the date to 1st day
            // this is so that the optimal distribution for all weeks will be shown
            if(now.date().year() != year || now.date().month() != month){
                date = new Date(year, month, 1);
            }else{
                date = new Date(year, month, now.date().day());
            }
            WorkMonth workMonth = new WorkMonth(fileManager.getSessionsOfMonth(project, month, year), date);
            Period remainingWorkLoad = new Period(maxHours, 0, 0).minusWithLowerEnd(workMonth.getWorkedOffTotal());
            System.out.println("\n-----------------------------------------"+ANSI_COLORS.ANSI_BLUE+"Work Time Tracker"+ANSI_COLORS.ANSI_RESET+"---------------------------------------\n");
            System.out.println("Total work load for "+ ANSI_COLORS.ANSI_CYAN+date.getMonthAsString()+" ("+year+")"+ANSI_COLORS.ANSI_RESET+":\t\t\t"+ANSI_COLORS.ANSI_CYAN+maxHours+ANSI_COLORS.ANSI_RESET+"h");
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

    private static void displayWeek(String project, int maxHours, int week, int month, int year){
        try{
            DateTime now = DateTime.now();
            Date date;

            // same check as in displayMonth but vice versa
            // second clause is for the case of old weeks
            if(now.date().getWeekOfMonth() == week && now.date().month() == month && now.date().year() == year){
                date = new Date(year, month, now.date().day());

            }else {
                date = new Date(year, month, Date.getFirstDayOfWeek(year, month, week));

            }
            WorkWeek workWeek = new WorkWeek(fileManager.getSessionsOfMonth(project, month, year), week, date);
            Period remainingWorkLoad = new Period(maxHours, 0, 0).minusWithLowerEnd(workWeek.getWorkedOffByWeek(week));
            System.out.println("\n-----------------------------------------"+ANSI_COLORS.ANSI_BLUE+"Work Time Tracker"+ANSI_COLORS.ANSI_RESET+"-----------------------------------------------------------------------------\n");
            System.out.println("Total work load for "+ ANSI_COLORS.ANSI_CYAN+date.getMonthAsString()+" ("+year+")"+ANSI_COLORS.ANSI_RESET+", Week "+ANSI_COLORS.ANSI_CYAN + week + ANSI_COLORS.ANSI_RESET+":\t\t"+ANSI_COLORS.ANSI_CYAN+maxHours+ANSI_COLORS.ANSI_RESET+"h");
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

    private static void export(String project, Optional<String> directory, int month, int year){
        try{
            new ExportHandler(fileManager.getSessionsOfMonth(project, month, year)).export(directory.map((s) -> Path.of(s)));
        }catch(IOException | URISyntaxException e){
            System.out.println(ANSI_COLORS.ANSI_RED + e.getMessage() + ANSI_COLORS.ANSI_RESET);

        }
    }

    private static void exportCorrected(String project, Optional<String> directory, int totalHours, int month, int year){
        try{
            new ExportHandler(fileManager.getSessionsOfMonth(project, month, year)).exportCorrected(directory.map(s -> Path.of(s)), totalHours, month, year);
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
