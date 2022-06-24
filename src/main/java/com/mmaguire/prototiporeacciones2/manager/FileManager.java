package com.mmaguire.prototiporeacciones2.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmaguire.prototiporeacciones2.model.Sistema;
import com.uppaal.model.core2.Data2D;
import com.uppaal.model.core2.DataSet2D;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.List;

public class FileManager {

    public static boolean saveSystemToFile(Sistema sistema, String path) {
        try{
            File file = new File(path);
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            ObjectMapper objectMapper = new ObjectMapper();
            fileWriter.write(objectMapper.writeValueAsString(sistema));
            fileWriter.close();
            return true;
        }
        catch (IOException ex){
            System.out.println("No se pudo guardar el sistema de reacciones.");
            ex.printStackTrace();
            return false;
        }
    }

    public static Sistema loadSystemFromFile(String path){
        Sistema result = null;
        try {
            File file = new File(path);
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            ObjectMapper objectMapper = new ObjectMapper();
            String line = fileReader.readLine();
            result = objectMapper.readValue(line, Sistema.class);
        }
        catch (IOException ex){
            System.out.println("No se pudo cargar el sistema de reacciones.");
            ex.printStackTrace();
        }
        return result;
    }

    public static boolean saveHistoryToFile(List<Sistema> historial, String path) {
        try{
            File file = new File(path);
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            for (Sistema sistema : historial) {
                fileWriter.write(objectMapper.writeValueAsString(sistema));
                fileWriter.newLine();
            }
            fileWriter.close();
            return true;
        }
        catch (IOException ex){
            System.out.println("No se pudo guardar el sistema de reacciones.");
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Sistema> loadHistoryFromFile(String path){
        List<Sistema> result = null;
        try {
            File file = new File(path);
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String line;
            while((line = fileReader.readLine()) != null) {
                result.add(objectMapper.readValue(line, Sistema.class));
            }
        }
        catch (IOException ex){
            System.out.println("No se pudo cargar el sistema de reacciones.");
            ex.printStackTrace();
        }
        return result;
    }

    public static boolean exportSimulationData(DataSet2D data, String path) {
        try {
            File file = new File(path);
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            fileWriter.write("### " + data.getTitle()); fileWriter.newLine();
            for(Data2D variableData : data) {
                fileWriter.write("# " + variableData.getTitle() + " #1"); fileWriter.newLine();
                for(Point2D.Double punto : variableData) {
                    fileWriter.write(punto.x + " " + punto.y); fileWriter.newLine();
                }
            }
            fileWriter.close();
            return true;
        }
        catch (IOException ex){
            System.out.println("No se pudo crear el archivo y exportar los datos de simulación falló.");
            ex.printStackTrace();
            return false;
        }
    }
}
