package Bolera;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class bolos extends Thread {
    String nombre;
   
    Random aleat = new Random();
    ArrayList<String> Marcador = new ArrayList<>();
    ArrayList<Integer> puntos = new ArrayList<>();
    JTable tabla;
    String Ruta;
    int linea=0;
    String[] columnas = {"Jugador", "Tiro1", "Tiro2", "Tiro3", "Tiro4", "Tiro5", "Tiro6", "Tiro7", "Tiro8", "Tiro9", "Tiro10", "Total"};
    Object[][] filas = new Object[1][12];
    public bolos(String nombre, JTable tabla,String ruta) {
        this.nombre = nombre;
        this.tabla = tabla;
        this.Ruta=ruta;
    }

    public bolos() {
    }
    

    @Override
    public void run() {
        
        filas[0][0] = nombre;
        actualizarTabla(columnas, filas);
        try {
                Thread.sleep(aleat.nextInt(2000)+2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(bolos.class.getName()).log(Level.SEVERE, null, ex);
            }
        for (int i = 0; i < 10; i++) {
            int num = aleat.nextInt(11);

            if (num >= 8 || num <= 2) {
                puntos.add(10);
                int num2 = aleat.nextInt(5)+6;
                int num3 = aleat.nextInt(5)+6;
                puntos.add(num2);
                puntos.add(num3);
                Marcador.add("10x" + num2 + "-" + num3);
            } else if (num == 3 || num == 4 || num == 7) {
                puntos.add(10);
                int num2 = aleat.nextInt(5)+6;
                puntos.add(num2);
                Marcador.add("10/" + num2);
            } else {
                int num1 = aleat.nextInt(5)+6;
                puntos.add(num1);
                Marcador.add(String.valueOf(num1));
            }
            

            filas[0][i + 1] = Marcador.get(i);

            actualizarTabla(columnas, filas);

            try {
                Thread.sleep(aleat.nextInt(2000)+2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(bolos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Escritura(Marcador, Ruta);
        int total = 0;
        for (int punto : puntos) {
            total += punto;
        }
        filas[0][11] = total;
        actualizarTabla(columnas, filas);
    }

    private void actualizarTabla(String[] columnas, Object[][] filas) {
            DefaultTableModel modelo = new DefaultTableModel(filas, columnas);
            tabla.setModel(modelo);
        
    }
    
    public void Escritura(ArrayList<String> datos, String ruta){
    FileWriter fichero = null;
    PrintWriter pw = null;
    try {
        fichero = new FileWriter(ruta, true); 
        pw = new PrintWriter(fichero);

        StringBuilder lineaJugador = new StringBuilder();
        lineaJugador.append(nombre).append(";");

        for (String tiro : datos) {
            lineaJugador.append(tiro).append(";");
        }

        
        int total = 0;
        for (int punto : puntos) {
            total += punto;
        }
        lineaJugador.append(total);

        pw.println(lineaJugador.toString()); 

    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        try {
            if (fichero != null) fichero.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    }
    public void leer(JTable tabla, String ruta) {
    String[] columnas = {"Jugador", "Tiro1", "Tiro2", "Tiro3", "Tiro4", "Tiro5", "Tiro6", "Tiro7", "Tiro8", "Tiro9", "Tiro10", "Total"};
    ArrayList<String[]> filas = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");
            if (datos.length == 12) {
                filas.add(datos); 
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    Object[][] data = new Object[filas.size()][12];
    for (int i = 0; i < filas.size(); i++) {
        data[i] = filas.get(i);
    }

    DefaultTableModel modelo = new DefaultTableModel(data, columnas);
    tabla.setModel(modelo);
}
    


}
