package com.worktimetracker;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.worktimetracker.DataClasses.WorkSession;

public class FileManager {
    
    public void storeWorkSession(WorkSession session) throws IOException, URISyntaxException{
        String fileName = session.getDate().toString("YYYY-MM")+".txt";
        createFolderIfNotExist();
        Path weeksFilePath = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months\\"+fileName);
    
    }

    private void createFolderIfNotExist() throws IOException, URISyntaxException{
        Path folder = Path.of(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "\\Months");
        Files.createDirectory(folder);
    }
}
