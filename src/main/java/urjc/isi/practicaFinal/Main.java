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
    			"./cast.G.txt", "./cast.mpaa.txt", "./cast.PG.txt",
    			"./cast.PG13.txt", "./cast.all.txt"};
        String categories = "";
        String category = new String();
    	In in;
        try {
        	for (int i = 0; i < docs.length; i++) {
        		in = new In("data/imdb-data/" + docs[i]);
                String bodyDoc = in.readAll();				    	//Leo todo el documento
                if(bodyDoc.contains(movie)) {						//Si el documento contiene la línea añado la categoría
                	switch (docs[i]) {
	                	case "./cast.00-06.txt": category = "Movies release since 2000";
	                	break;
	                	case "./cast.06.txt": category = "Movies release in 2006";
	                	break;
	                	case "./cast.G.txt": category = "Movies rated G by MPAA";
	                	break;
	                	case "./cast.PG.txt": category = "Movies rated PG by MPAA";
	                	break;
	                	case "./cast.PG13.txt": category = "Movies rated PG13 by MPAA";
	                	break;
	                	case "./cast.mpaa.txt": category = "Movies rated by MPAA";
	                	break;
	                	case "./cast.action.txt": category = "Action Movies";
	                	break;
	                	case "./cast.rated.txt": category = "Popular Movies";
	                	break;
	                	case "./cast.all.txt": category = "Over 250,000 movies";
	                	break;
	                	default: category = "NOT FOUND";
	                	break;
                	}
                	categories += category + "<br>";		//Cambiar por concat, Habia ERROR.
                }
        	}
        }catch(IllegalArgumentException e) {
            System.out.println(e);
            throw new IllegalArgumentException();
        }
    	return categories;
    }
    
    
    public static String MoviesOfCategorie(String categorie) {
    	if (categorie == null) {
    		throw new NullPointerException("Categorie null");
    	}
        In in;
        String movies = "";
        try {
        	String path = "data/imdb-data/cast." + categorie + ".txt";
            in = new In(path);
            while (!in.isEmpty()) {
                String line = in.readLine();				    	//Leo linea a linea (cada linea es una película)
                String[] parts = line.split("/");					//Hago un split hasta la primera /
                movies += (parts[0]) + "<br>";					//Concateno todas las películas
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e);
    		throw new IllegalArgumentException("Error al abrir el archivo");
        }
        return movies;
    }
    
    public static String doAinB(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
    	String filePath = "data/other-data/tinyMovies.txt";
    	String delimiter = "/";
    	Graph graph = new Graph(filePath, delimiter);
    	String body = request.body();
    	String[] element1 = body.split("=");
    	String actor1 = element1[1].replace("+", " ");
    	String movies = "";
    	movies = AInB(graph, actor1);
    	return movies;
    }
    
    public static String doDistance(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
    	String filePath = "data/other-data/tinyMovies.txt";
    	String delimiter = "/";
    	Graph graph = new Graph(filePath, delimiter);
    	String body = request.body();
    	String[] elements = body.split("&");
    	String[] elements1 = elements[0].split("=");
    	String actor1 = elements1[1].replace("+", " ");
    	String[] elements2 = elements[1].split("=");
    	String actor2 = elements2[1].replace("+", " ");
    	String movies = "";
    	movies = distanceBetweenElements(graph, actor1, actor2);
    	return movies;
    }
    
    
    public static String FormularyAinB(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
    	String body = "<form action='/AInB' method='post'>" +
        	"<div>" + 
            	"<label for='name'>Actor o película: </label>" +
            	"<input type='text' id='name' name='Element1'/>" +
            "</div>" +
        	"<div class='button'>" +
            	"<button type='submit'>Buscar</button>" +
            "</div>" +
        "</form>";
    	return body;
    }
    
    public static String FormularyDistanceBetweenElements(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
    	String body = "<form action='/DistanceBetweenElements' method='post'>" +
        	"<div>" + 
            	"<label for='name'>Actor o película: </label>" +
            	"<input type='text' id='name' name='Element1'/>" +
            "</div>" +
        	"<div>" + 
        	"<label for='name'>Actor o película: </label>" +
        		"<input type='text' id='name' name='Element2'/>" +
        	"</div>" +
        	"<div class='button'>" +
            	"<button type='submit'>Calcular</button>" +
            "</div>" +
        "</form>";
    	return body;
    }
    
    
    
        
    public static void main(String[] args) throws ClassNotFoundException {
        port(getHerokuAssignedPort());
        staticFileLocation("/public");        
    	get("/", (req, res) ->
    	"<form action='/FormularyAInB' method='post'>" +
    		"<div class='button'>Puedes elegir entre las siguientes opciones:<br/><br/>" +
    			"-Buscar qué actores salen en una película, o en qué películas sale un actor:<br/>" +
    			"<button type='submit'  class='btn btn-default ribbon'>Actor-Película</button>" +
    		"</div>" +
    	"</form>" +
    	"<form action='/FormularyDistanceBetweenElements' method='post'>" +
			"<div class='button'>" +
				"-Calcular la distancia entre dos elementos (peliculas o actores):<br/>" +
				"<button type='submit'>Distancia entre elementos</button>" +
			"</div>" +
		"</form>" +
    	"<form action='/FormularyCategoriesOf' method='post'>" +
			"<div class='button'>" +
				"-Buscar categorías de una película: <br/>" +
				"<button type='submit'>Categorías</button>" +
			"</div>" +
		"</form>" +
    	"<form action='/FormularyOfCategories' method='post'>" +
			"<div class='button'>" +
				"-Buscar películas de una categoría:<br/>" +
				"<button type='submit'>Películas de categoría</button>" +
			"</div>" +
		"</form>");
    	
    	post("/FormularyAInB", Main::FormularyAinB);
    	post("/FormularyDistanceBetweenElements", Main::FormularyDistanceBetweenElements);

    	post("/AInB", Main::doAinB);
    	post("/DistanceBetweenElements", Main::doDistance);
    	
    	
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}