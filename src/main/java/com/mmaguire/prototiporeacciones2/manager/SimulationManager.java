package com.mmaguire.prototiporeacciones2.manager;

import com.mmaguire.prototiporeacciones2.model.DatosComponente;
import com.mmaguire.prototiporeacciones2.model.Punto;
import com.mmaguire.prototiporeacciones2.model.Simulacion;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import static com.mmaguire.prototiporeacciones2.manager.Helper.generateCommand;
import static com.mmaguire.prototiporeacciones2.manager.Helper.getTempDirectory;

public class SimulationManager {

    public static int executeSimulation(String tempDir, ArrayList<String> out) throws InterruptedException, IOException {
        // Ejecutar comando de simulación con el xml y la query
        String os = System.getProperty("os.name");
        String currentDir = System.getProperty("user.dir");
        ProcessBuilder builder = new ProcessBuilder();
        Process procSimulacion;
        builder.directory(new File(currentDir));
        String[] commando = generateCommand(currentDir, tempDir, os);
        System.out.println(Arrays.toString(commando));
        builder.command(commando);
        procSimulacion = builder.start();
        builder.redirectError(new File(tempDir + "error.log"));

        // Leer el output de la consola
        // Se debe leer ANTES del waitFor para evitar problemas con el buffer.
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(procSimulacion.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            out.add(s);
        }

        return procSimulacion.waitFor();
    }

    public static void fillSimulationChart(LineChart<Number,Number> lineChart, Simulacion simulacion) {
        lineChart.setCreateSymbols(false);
        for (DatosComponente datosComponente : simulacion.getDatos()) {
            XYChart.Series<Number,Number> serie = new XYChart.Series<>();
            serie.setName(datosComponente.getTitulo());
            for (Punto punto : datosComponente.getDatos()) {
                serie.getData().add(new XYChart.Data<>(punto.getX(), punto.getY()));
            }
            lineChart.getData().add(serie);
        }
    }


}
