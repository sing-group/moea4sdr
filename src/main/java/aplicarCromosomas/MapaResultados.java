package aplicarCromosomas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.io.comparator.NameFileComparator;


public class MapaResultados {

	public static void main(String[] args) throws IOException {

		String directorio = "copias_experimentos/youtube_15000_(15ex0y3)/experimentBaseDirectory/SynsetStudy/data/NSGAII/SynsetProblem";
		String fichero = directorio + "/tabla.csv";
		BufferedReader bufferLecturaFun = null;
		BufferedReader bufferLecturaVar = null;
		BufferedReader bufferLecturaResultados = null;
		String SEPARATORFUNVAR = " ";
		String SEPARATORCSV = ";";
		String valores = null;
		ArrayList<Metricas> tabla = new ArrayList<Metricas>();
		ArrayList<String> resultados = new ArrayList<String>();

		//Miramos la lista de ficheros FUN que tenemos en el directorio.
		File dirFun = new File(directorio);
		File[] foundFunFiles = dirFun.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("FUN");
			}
		});

		//Miramos la lista de ficheros VAR que tenemos en el directorio.
		File dirVar = new File(directorio);
		File[] foundVarFiles = dirVar.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("VAR");
			}
		});

		//Ordenamos los ficheros por nombre para que haya correspondencia de datos y cromosomas
		Arrays.sort(foundVarFiles, NameFileComparator.NAME_COMPARATOR);
		Arrays.sort(foundFunFiles, NameFileComparator.NAME_COMPARATOR);

		//		//imprimimos la lista de ficheros FUN
		//		for (File fileFun : foundFunFiles) {
		//			System.out.println("fichero: " + fileFun);
		//		}
		//	
		//		//imprimimos la lista de ficheros VAR
		//		for (File fileVar : foundVarFiles) {
		//			System.out.println("fichero: " + fileVar);
		//		}

		for (int numFicheros=0; numFicheros<foundFunFiles.length; numFicheros++) {
			File fileFun = foundFunFiles[numFicheros];
			File fileVar = foundVarFiles[numFicheros];
			//			System.out.println("Asociacion ficheros:");
			//			System.out.println("Fichero Fun: " + fileFun);
			//			System.out.println("Fihcero Var: " + fileVar);

			try {
				int fila=0;
				//Abrir los ficheros en el buffer de lectura
				bufferLecturaFun = new BufferedReader(new FileReader(fileFun));
				bufferLecturaVar = new BufferedReader(new FileReader(fileVar));


				//Leemos una línea y empezamos a trabajar
				String lineaFun = bufferLecturaFun.readLine();
				String lineaVar = bufferLecturaVar.readLine();
				while (lineaFun != null) {

					//Separar los elementos de la linea Fun y crear las parejas fun-var
					String [] camposFun = lineaFun.split(SEPARATORFUNVAR);
					//Generar la cadena del Fun en enteros y pasarlos a string
					String valoresFun = (int)Float.parseFloat(camposFun[0]) + " " +
							(int)Float.parseFloat(camposFun[1]) + " " +
							(int)Float.parseFloat(camposFun[2]);

					//Creamos las parejas y lo almacenamos
					Metricas parejas = new Metricas(); 
					parejas.setResultados(valoresFun);
					parejas.setCromosoma(lineaVar);
					tabla.add(parejas);

					//leer otra linea
					lineaFun = bufferLecturaFun.readLine();
					lineaVar = bufferLecturaVar.readLine();
				}

			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			finally {
				//cerramos el buffer de lectura
				if (bufferLecturaFun != null) {
					bufferLecturaFun.close();
				}	 
			}
			//System.out.println("Elementos en la tabla: " + tabla.size());

			//Escribimos el contenido de la tabla a un fichero
			String ruta = directorio + "/tabla_mapa.txt";
			File f = new File(ruta);
			FileWriter fw = new FileWriter(f);
			BufferedWriter escritura = new BufferedWriter(fw);
			for(int i=0;i<tabla.size();i++){
				escritura.write((tabla.get(i).getResultados() + "-" + tabla.get(i).getCromosoma()));
				escritura.newLine();

			}
			escritura.close();


		}
		//Aqui termina la lectura de los cromosomas. Seguimos...
//		System.out.print("Cromosoma para metido a manopla 0 314 1450: " );
//		for (int i=0; i<tabla.size(); i++) {
//			if ( tabla.get(i).getResultados().equals("0 314 1450")) System.out.println(tabla.get(i).getCromosoma());; 
//		}
		
		try {
	    	  int fila=0;
	         //Abrir el fichero en el buffer de lectura
	         bufferLecturaResultados = new BufferedReader(new FileReader(fichero));
	        
	         //Leer una línea del archivo y descartarla. Aquí esta la leyenda de los datos
	         String linea = bufferLecturaResultados.readLine();
	         //Leemos otra línea y empezamos a trabajar
	         linea = bufferLecturaResultados.readLine();
	         while (linea != null) {
	        	 
	            //Separar los elementos de la linea
	        	//System.out.println("Contenido de linea: " + linea);
	        	String [] campos = linea.split(SEPARATORCSV);
	        	valores = campos[0] + " " + campos[1] + " " + campos[2];
	            //System.out.println("Valor de la cadena valores: " + valores);
	            resultados.add(valores);
	            //leer otra linea
	            linea = bufferLecturaResultados.readLine();
	            fila++;
	         }
	         
	      } 
	      catch (IOException e) {
	         e.printStackTrace();
	      } 
	      finally {
	         //cerramos el buffer de lectura
	    	  if (bufferLecturaResultados != null) {
	            bufferLecturaResultados.close();
	          }	 
	      }
		for (int v=0; v< resultados.size(); v++) {
			System.out.println("Valor " + v + " de los resultados en disco: " + resultados.get(v));
		};
		
		//Leemos el fichero que tiene los mejores paretos para aplicar los cromosomas al 75% 25%
//		System.out.print("Cromosoma para " + resultados.get(0) + ": ");
//		for (int i=0; i<tabla.size(); i++) {
//			if ( tabla.get(i).getResultados().equals(resultados.get(0))) System.out.println(tabla.get(i).getCromosoma());; 
//		}
		
		for (int cResultados=0; cResultados<resultados.size(); cResultados++) {
			for (int cTabla=0; cTabla<tabla.size(); cTabla++) {
				if (resultados.get(cResultados).equals(tabla.get(cTabla).getResultados()) ) {
					System.out.println("Valor: " + resultados.get(cResultados) + "\t cromosoma: " + tabla.get(cTabla).getCromosoma() );
					break;
				}
			}
		}
	}
}

