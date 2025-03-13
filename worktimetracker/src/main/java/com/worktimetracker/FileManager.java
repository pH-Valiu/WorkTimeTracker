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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.worktimetracker.DataClasses.Date;
import com.worktimetracker.DataClasses.DateTime;
import com.worktimetracker.DataClasses.Time;
import com.worktimetracker.DataClasses.WorkSession;

public class FileManager {
    
    public void storeStart(String project, DateTime starDateTime) throws IOException, URISyntaxException{
        createProjectFolderIfNotExist(project);

        //Get (and or create if necessary) the save file
        String fileName = starDateTime.toString("YYYY-MM");
        Path monthsFilePath = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Projects\\" + project + "\\Months\\"+fileName);
        
        //write into file "\nYYYY-MM-DD hh:mm:ss"
        BufferedWriter writer = new BufferedWriter(new FileWriter(monthsFilePath.toString(), true));
        writer.newLine();
        writer.append(starDateTime.toString("YYYY-MM-DD hh:mm:ss"));
        writer.flush();
        writer.close();
    }
    
    public void storeEnd(String project, Time end) throws IOException, URISyntaxException{
        //try to get the newest, most recently used file
        Optional<String> file = getNewestFile(project);

        //assure that it has been found
        if(file.isEmpty()){
            throw new IOException("No file found. Please start the timer first.");
        }else{
            //Get the file
            Path monthsFilePath = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Projects\\" + project + "\\Months\\"+file.get());
            
            //Append to file "Ohh:mm:ss"
            BufferedWriter writer = new BufferedWriter(new FileWriter(monthsFilePath.toString(), true));
            writer.append("O"+end.toString("hh:mm:ss"));
            writer.flush();
            writer.close();
        }
    }

    /**
     * This is a super important method as it searches the system for the "Month"-file of a certain project.
     * The parameters for month and year further describe which one to take
     * @param project the project to search in
     * @param month the month to query
     * @param year the yaer to query
     * @return List containing all work sessions in that month
     * @throws IOException
     * @throws URISyntaxException
     */
    public List<WorkSession> getSessionsOfMonth(String project, int month, int year) throws IOException, URISyntaxException{
        if (!checkIfProjectExists(project)) throw new IOException("Requested project does not exist: "+project);

        //Get file of month
        DateTime now = DateTime.now();
        //set dateTime to get file from
        DateTime dateTime = new DateTime(year, month, now.date().day(), now.time().hours(), now.time().minutes(), now.time().seconds());

        String fileName = dateTime.toString("YYYY-MM");
        Path file = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Projects\\" + project + "\\Months\\" + fileName);
        
        if (!Files.exists(file)){
            throw new IOException("Requested file does not exist: "+ fileName);
        }
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

    /**
     * Searches the 'Projects' directory for all projects
     * @return a list of all projects as their names
     * @throws URISyntaxException if code domain location could not be found
     */
    public List<String> listAllProjects() throws URISyntaxException{
        Path projectsFolder = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Projects");
        File folder = projectsFolder.toFile();
        
        List<String> projectNames = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.isDirectory()) {
                    projectNames.add(file.getName());
                }
            }
        }
        return projectNames;
    }

    //helper functions

    private boolean checkIfProjectExists(String projectName) throws URISyntaxException{
        return listAllProjects().contains(projectName);
    }


    private void createProjectFolderIfNotExist(String projectName) throws IOException, URISyntaxException{
        Path folder = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Projects\\"+projectName+"\\Months");
        Files.createDirectories(folder);
    }

    /**
     * This method tries to get the newest save file of given project.
     * It retrieves all filenames and sorts them based on their name
     * @param projectName the name of the project to search through
     * @return Optional containing the name of the file if there was one to begin with
     * @throws IOException
     * @throws URISyntaxException
     */
    private Optional<String> getNewestFile(String projectName) throws IOException, URISyntaxException   {
        if (!checkIfProjectExists(projectName)) throw new IOException("Requested project does not exist: "+projectName);
        
        // "/Month" folder as File object
        File folder = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Projects\\" + projectName + "\\Months\\").toFile();
        
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
