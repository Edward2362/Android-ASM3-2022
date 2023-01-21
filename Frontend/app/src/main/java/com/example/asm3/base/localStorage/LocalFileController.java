package com.example.asm3.base.localStorage;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LocalFileController<T> {
    private String fileName;
    private Context context;

    public LocalFileController(String fileName, Context context) {
        this.fileName = fileName;
        this.context = context;
    }

    public ArrayList<T> readFile() {
        ObjectInputStream input;
        ArrayList<T> object;

        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);

            input = new ObjectInputStream(fileInputStream);
            object = (ArrayList<T>) input.readObject();

            input.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            object = new ArrayList<T>();
        }
        return object;
    }

    public void writeFile(ArrayList<T> object) {
        ObjectOutputStream out;

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fileOutputStream);

            out.writeObject(object);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
