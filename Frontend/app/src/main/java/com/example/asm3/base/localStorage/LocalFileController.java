package com.example.asm3.base.localStorage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocalFileController {
    private String fileName;
    private Context context;

    public LocalFileController(String fileName, Context context) {
        this.fileName = fileName;
        this.context = context;
    }


    public ArrayList<String> readFile() {
        FileInputStream inputStream = null;
        ArrayList<String> lines = new ArrayList<String>();
        try {
            inputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

        } catch(FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }

        return lines;
    }

    public void writeFile(ArrayList<String> lines) {
        String fileContent = "";
        FileOutputStream outputStream = null;

        for (String line: lines) {
            fileContent = fileContent + line + "\n";
        }

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}
