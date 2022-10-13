package aplicarCromosomas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

public class TablaPareto {

	public static final String SEPARATOR = ";";
	public static final String fichero = "copias_experimentos/youtube_15000_(15ex0y3)/experimentBaseDirectory/SynsetStudy/data/NSGAII/SynsetProblem/tabla.csv";
	public static final String directorio = "copias_experimentos/youtube_15000_(15ex0y3)/experimentBaseDirectory/SynsetStudy/data/NSGAII/SynsetProblem/";
	public static int [][] tablaPareto = new int[1000][3]; 
	
	
	public static void main(String[] args) throws IOException {		
		
		BufferedReader bufferLectura = null;
		
		//Miramos la lista de ficheros FUN que tenemos en el directorio. Esto se puede quitar.
        File dir = new File(directorio);
        File[] foundFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("FUN");
            }
        });
		
	    try {
	    	  int fila=0;
	         //Abrir el fichero en el buffer de lectura
	         bufferLectura = new BufferedReader(new FileReader(fichero));
	        
	         //Leer una línea del archivo y descartarla. Aquí esta la leyenda de los datos
	         String linea = bufferLectura.readLine();
	         
	         //Leemos otra línea y empezamos a trabajar
	         linea = bufferLectura.readLine();
	         while (linea != null) {
	        	 
	            //Separar los elementos de la linea
	        	String [] campos = linea.split(SEPARATOR);
	        	tablaPareto[fila][0] = Integer.parseInt(campos[0]);
	        	tablaPareto[fila][1] = Integer.parseInt(campos[1]);
	        	tablaPareto[fila][2] = Integer.parseInt(campos[2]);
	        	
	            //System.out.println(Arrays.toString(campos));
	            System.out.println("matrix1:" + tablaPareto[fila][0] + " matrix2:" + tablaPareto[fila][1] + " matrix3:" + tablaPareto[fila][2]);
	            //leer otra linea
	            linea = bufferLectura.readLine();
	            fila++;
	         }
	         
	      } 
	      catch (IOException e) {
	         e.printStackTrace();
	      } 
	      finally {
	         //cerramos el buffer de lectura
	    	  if (bufferLectura != null) {
	            bufferLectura.close();
	          }	 
	      }

	 // Aquí la carpeta donde queremos buscar
        String path = directorio; 

        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles(); 

        for (int i = 0; i < listOfFiles.length; i++)         {

            if (listOfFiles[i].isFile())             {
                files = listOfFiles[i].getName();
                System.out.println(files);
            }
        }
        

        
        System.out.println(foundFiles.length);

        for (File file : foundFiles) {
            // Process file
        	System.out.println("fichero: " + file);
        } 
        
        
        
        }
        
        
        
        
        
}

