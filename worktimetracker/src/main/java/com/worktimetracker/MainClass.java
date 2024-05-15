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
                    if(args.length < 2){
                        displayMonth(40, DateTime.now().date().month(), false);
                    }else{
                        if(args.length < 3){
                            displayMonth(Integer.parseInt(args[1]), DateTime.now().date().month(), false);
                        }else{
                            displayMonth(Integer.parseInt(args[1]), Integer.parseInt(args[2]), (Integer.parseInt(args[2]) == DateTime.now().date().month()) ? false : true);
                        }
                    }
                }
                case "week", "-week", "--week" -> {
                    if(args.length < 2){
                        displayWeek(10, DateTime.now().date().getWeekOfMonth(), DateTime.now().date().month());
                    }else{
                        if(args.length < 3){
                            displayWeek(Integer.parseInt(args[1]), DateTime.now().date().getWeekOfMonth(), DateTime.now().date().month());
                        }else{
                            if (args.length < 4){
                                displayWeek(Integer.parseInt(args[1]), Integer.parseInt(args[2]), DateTime.now().date().month());
                            }else{
                                displayWeek(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
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

                Commands:
                    help                Shows this page
                    start               Start a work session
                    stop                Stop the current work session
                    month <maxHours> <month>        
                                        Display the stats of the current month. 
                                        You can specify how many hours you want to work in total. (Default: 40)
                                        You can specify which month to display. (Default: current month)
                    week <maxHours> <weekOfMonth> <month>   
                                        Display the stats of the current week.
                                        You can specify how many hours you want to work in total. (Default 10)
                                        This does not take in consideration that a certain week can be in two different months.
                                        Each week 'part' will count as its own 'week'.
                                        You can specify which week of the current month to display. (Default: current week)
                                        You can also specify the month whose week to disply. (Default: current month)
                """);
        System.out.println("----------------------------------------------------------");
    }

    private static void startTimer(){
        try {
            fileManager.storeStart(DateTime.now());
            System.out.println("Started WorkTimeTracker");
        } catch (IOException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }

    }
    private static void stopTimer(){
        try {
            fileManager.storeEnd(DateTime.now().time());
            System.out.println("Stopped WorkTimeTracker");
        } catch (URISyntaxException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void displayMonth(int maxHours, int month, boolean old){
        try {
            DateTime now = DateTime.now();
            Date date;
            if(old){
                date = new Date(now.date().year(), month, 1);
            }else{
                date = new Date(now.date().year(), month, now.date().day());
            }
            WorkMonth workMonth = new WorkMonth(fileManager.getSessionsOfMonth(month), date);
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

    private static void displayWeek(int maxHours, int week, int month){
        try{
            DateTime now = DateTime.now();
            Date date;
            if(now.date().getWeekOfMonth() == week && now.date().month() == month){
                date = new Date(now.date().year(), month, now.date().day());

            }else {
                date = new Date(now.date().year(), month, Date.getFirstDayOfWeek(now.date().year(), month, week));

            }
            WorkWeek workWeek = new WorkWeek(fileManager.getSessionsOfMonth(month), week, date);
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
}
