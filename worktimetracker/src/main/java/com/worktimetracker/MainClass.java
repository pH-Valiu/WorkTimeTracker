package com.worktimetracker;

import java.io.IOException;
import java.net.URISyntaxException;

import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.DataClasses.DateTime;
import com.worktimetracker.DataClasses.Time;
import com.worktimetracker.DataClasses.WorkSession;

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
                case "month", "-month", "--month" -> displayMonth();
                case "week", "-week", "--week" -> displayWeek();
                case "setHoursPerMonth", "-setHoursPerMonth", "--setHoursPerMonth", "set", "-set", "--set" -> {
                    if(args.length < 2) System.out.println("Parameter missing: <hours>");
                    else setMaxHoursPerMonth(Integer.parseInt(args[1]));
                }
                default -> System.out.println("Argument unkown. See --help for details regarding the usage.");
            }
        }
    }

    private static void showHelpPage(){
        System.out.println("------------------- Work Time Tracker --------------------");
        //TODO help page
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
    private static void displayMonth(){

    }
    private static void displayWeek(){

    }
    private static void setMaxHoursPerMonth(int hours){

    }
}
