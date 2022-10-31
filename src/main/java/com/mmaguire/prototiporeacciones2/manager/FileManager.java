package com.mmaguire.prototiporeacciones2.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmaguire.prototiporeacciones2.model.DatosComponente;
import com.mmaguire.prototiporeacciones2.model.Punto;
import com.mmaguire.prototiporeacciones2.model.Simulacion;
import com.mmaguire.prototiporeacciones2.model.Sistema;
import com.uppaal.model.core2.Data2D;
import com.uppaal.model.core2.DataSet2D;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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
            Iterator<Sistema> it = historial.iterator();
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            while (it.hasNext()) {
                fileWriter.write(objectMapper.writeValueAsString(it.next()));
                if(it.hasNext())
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
        List<Sistema> result = new ArrayList<>();
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

    public static boolean saveQueryToFile(String query, String path) {
        try{
            File file = new File(path);
            OutputStream fileWriter = new FileOutputStream(file);
            fileWriter.write(query.getBytes());
            fileWriter.close();
            return true;
        }
        catch (IOException ex){
            System.out.println("No se pudo guardar la query de simulación.");
            ex.printStackTrace();
            return false;
        }
    }
    public static boolean saveQueryToFile2(String query, String path) {
        try{
            File file = new File(path);
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            fileWriter.write(query);
            fileWriter.close();
            return true;
        }
        catch (IOException ex){
            System.out.println("No se pudo guardar la query de simulación.");
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean exportSimulationData(Simulacion data, String path) {
        try {
            File file = new File(path);
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            fileWriter.write("### " + data.getTitulo()); fileWriter.newLine();
            for(DatosComponente variableData : data.getDatos()) {
                fileWriter.write("# " + variableData.getTitulo() + " #1"); fileWriter.newLine();
                for(Punto punto : variableData.getDatos()) {
                    fileWriter.write(punto.getX() + " " + punto.getY()); fileWriter.newLine();
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
