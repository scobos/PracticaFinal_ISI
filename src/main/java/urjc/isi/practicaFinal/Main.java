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
    		return "";
    }
    
    
    public static String doWork(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
	String result = new String("Hello World");

	return result;
    }

    public static void main(String[] args) throws ClassNotFoundException {
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
