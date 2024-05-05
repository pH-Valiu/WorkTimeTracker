package com.worktimetracker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import com.worktimetracker.DataClasses.*;

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
            List<WorkSession> sessions = fileManager.getSessionsOfMonth();
            System.out.println(sessions);
            Period periodTotal = sessions.stream().map( it -> it.getWorkTime()).reduce(new Period(0, 0, 0), (subtotal, element) -> subtotal.add(element));
            System.out.println("Maximum this month:\t\t\t"+maxHours+"h");
            System.out.println("Already worked:\t\t\t\t"+periodTotal.toString());
        } catch (IOException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void displayWeek(int maxHours){

    }
}
