package urjc.isi.practicaFinal;

import static spark.Spark.*;

import spark.Request;
import spark.Response;

import java.net.URISyntaxException;

public class Main {
	
	public static String distanceBetweenElements(Graph graph,String element1,String element2) {
    	if (element1 == null || element2 == null || graph.V() == 0) {
    		throw new NullPointerException("Element null");
    	}
    	
    	String ruta = new String("");
    	PathFinder pf = new PathFinder(graph, element1);
        for (String v : pf.pathTo(element2)) {
            ruta += v + " -> ";
        }        
        char[] ruta1 = ruta.toCharArray();
        String result = new String(ruta1, 0, ruta1.length-4);
        
        result += "<br>Distancia: " + pf.distanceTo(element2);
    	return result;
    }

    //Método que devuelve que actor está en qué peliculas y viceversa
    public static String AInB(Graph graph, String actor) {
    	if (actor == null) {
    		throw new NullPointerException("Categorie null");
    	}
        String movies = "";
        StringBuilder s = new StringBuilder();
        for (String v : graph.st) {
        	if(v.contains(actor)) {
        		 s.append(v + ": ");
                 for (String w : graph.st.get(v)) {
                     s.append(w + " ");
                     movies += w + "<br>";
                 }
                 s.append('\n');
        	}
        }
        return movies;
    }
    
    public static String categoriesOf(String movie) {
	    	if (movie == null) {
	    		throw new NullPointerException("Movie null");
	    	}
	    	String[] docs = {"./cast.00-06.txt", "./cast.06.txt", "./cast.action.txt",
	    			"./cast.all.txt", "./cast.G.txt", "./cast.mpaa.txt", "./cast.PG.txt",
	    			"./cast.PG13.txt",};
	    	String categories = "";
	    	In in;
	    	try {
	    		for (int i = 0; i < docs.length; i++) {
	    			in = new In("data/imdb-data/" +docs[i]);
	    			String bodyDoc = in.readAll();				    	//Leo todo el documento
	    			if(bodyDoc.contains(movie)) {						//Si el documento contiene la línea añado la categoría
	    				categories = categories + docs[i];				//Cambiar por concat, Habia ERROR.
	    			}
	    		}
	    	}catch(IllegalArgumentException e) {
	    		System.out.println(e);
	    		throw new IllegalArgumentException();
	    	}
	    	categories = categories.replace("./cast.", " ");
	    	categories = categories.replace(".txt", "");			//Me quedo solo con las categorías
	    	return categories;
	  
	}
    
    public static String MoviesOfCategorie(String categorie) {
	    	if (categorie == null) {
				throw new NullPointerException("Categorie null");
		}
	    In in;
	    String movies = "";
	    try {
	    	String path = "resources/data/imdb-data/cast." + categorie + ".txt";
	        in = new In(path);
	        while (!in.isEmpty()) {
	            String line = in.readLine();				    	//Leo linea a linea (cada linea es una película)
	            String[] parts = line.split("/");					//Hago un split hasta la primera /
	            movies = movies + (parts[0]) + "<br>";					//Concateno todas las películas
	        }
	    }
	    catch (IllegalArgumentException e) {
	        System.out.println(e);
				throw new IllegalArgumentException("Error al abrir el archivo");
	    }
	    return movies;
    }
    
    
    public static String doWork(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
	String result = new String("Hello World");

	return result;
    }

    public static void main(String[] args) throws ClassNotFoundException {
    		StdOut.println("######Peliculas de acción#####");
        String categorie = "action";
        String movies = Main.MoviesOfCategorie(categorie);					//Tarda muchísimo.
        StdOut.println(movies);
        port(getHerokuAssignedPort());

        // spark server
        get("/hello", Main::doWork);

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    
    	
}
