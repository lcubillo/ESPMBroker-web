package com.espmail.broker.util;

import java.io.*;

/**
 * User: Luis Cubillo
 * Date: 07-sep-2009
 * Time: 19:17:26
 */
public class CargaCSV implements Cargador {
    
    public String carga(InputStream fuente) {


        BufferedReader br = new BufferedReader(new InputStreamReader(fuente));

        try {
            String linea = br.readLine();
            String sufijo = ""+System.currentTimeMillis();
            String nombreArchivo = "/tmp/listacsv."+sufijo.substring(sufijo.length()-4);

            BufferedWriter fr = new BufferedWriter(new FileWriter(nombreArchivo));
            while(linea!=null){
                fr.write(trataComillas(linea));
                fr.newLine();
                fr.flush();
                linea = br.readLine();
            }
            fr.close();
            return nombreArchivo;

        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }

    private String trataComillas(String linea){
        return linea;
    }
}
