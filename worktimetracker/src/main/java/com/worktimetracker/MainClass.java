package com.worktimetracker;

import java.io.IOException;
import java.net.URISyntaxException;

import com.worktimetracker.DataClasses.*;
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
                case "start", "-start", "--start" -> startTimer();
                case "stop", "-stop", "--stop" -> stopTimer();
                case "month", "-month", "--month" -> {
                    if(args.length < 2) displayMonth(40);
                    else displayMonth(Integer.parseInt(args[1]));
                }
                case "week", "-week", "--week" -> {
                    if(args.length < 2) displayWeek(10);
                    else displayWeek(Integer.parseInt(args[1]));
                }
                default -> System.out.println("Argument unkown. See --help for details regarding the usage.");
            }
        }
    }

    private static void showHelpPage(){
        System.out.println("------------------- Work Time Tracker --------------------\n");
        System.out.println("""
                This is a tool to assist you in keeping track of your work time to not create overtime.

                Commands:
                    help                    Shows this page
                    start                   Start a work session
                    stop                    Stop the current work session
                    month <maxHours>        Display the stats of the current month. 
                                            You can specify how many hours you want to work in total. (Default: 40)
                    week <maxHours>         Display the stats of the current week.
                                            You can specify how many hours you want to work in total. (Default 10)
                                            This does not take in consideration that a certain week can be in two different months.
                                            Each week 'part' will count as its own 'week'.
                """);
        System.out.println("----------------------------------------------------------");
    }

    private static void startTimer(){
        try {
            fileManager.storeStart(DateTime.now());
        } catch (IOException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }

    }
    private static void stopTimer(){
        try {
            fileManager.storeEnd(DateTime.now().time());
        } catch (URISyntaxException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void displayMonth(int maxHours){
        try {
            WorkMonth workMonth = new WorkMonth(fileManager.getSessionsOfMonth());
            Period remainingWorkLoad = new Period(maxHours, 0, 0).minusWithLowerEnd(workMonth.getWorkedOffTotal());
            System.out.println("\n-----------------------------------------"+ANSI_COLORS.ANSI_BLUE+"Work Time Tracker"+ANSI_COLORS.ANSI_RESET+"---------------------------------------\n");
            System.out.println("Total work load for "+ ANSI_COLORS.ANSI_CYAN+DateTime.now().date().getMonthAsString()+ANSI_COLORS.ANSI_RESET+":\t\t\t"+ANSI_COLORS.ANSI_CYAN+maxHours+ANSI_COLORS.ANSI_RESET+"h");
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
            System.out.println(e.getMessage());
        }
    }

    private static void displayWeek(int maxHours){
        try{
            DateTime now = DateTime.now();
            int weekNr = WorkMonth.calculateWeekNrBasedOnWeekAndMonth(now.date().getDayOfWeek(), now.date().day());
            
            WorkWeek workWeek = new WorkWeek(fileManager.getSessionsOfMonth(), weekNr);
            Period remainingWorkLoad = new Period(maxHours, 0, 0).minusWithLowerEnd(workWeek.getWorkedOffByWeek(weekNr));
            System.out.println("\n-----------------------------------------"+ANSI_COLORS.ANSI_BLUE+"Work Time Tracker"+ANSI_COLORS.ANSI_RESET+"-----------------------------------------------------------------------------\n");
            System.out.println("Total work load for "+ ANSI_COLORS.ANSI_CYAN+now.date().getMonthAsString()+ANSI_COLORS.ANSI_RESET+", Week "+ANSI_COLORS.ANSI_CYAN + weekNr + ANSI_COLORS.ANSI_RESET+":\t\t"+ANSI_COLORS.ANSI_CYAN+maxHours+ANSI_COLORS.ANSI_RESET+"h");
            System.out.println("Already worked off:\t\t\t\t"+workWeek.getWorkedOffByWeek(weekNr).toStringOrDash(ANSI_COLORS.ANSI_GREEN));
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
        }catch (IOException | URISyntaxException e){
            System.out.println(e.getMessage());
        }
    }
}
