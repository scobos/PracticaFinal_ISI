package urjc.isi.practicaFinal;

import static spark.Spark.*;
import spark.Request;
import spark.Response;

import java.beans.Statement;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.StringTokenizer;


public class Main {
	
    private static Connection connection;
    
	public static String distanceBetweenElements(Graph graph,String element1,String element2) {
		if (element1 == null || element2 == null || graph.V() == 0) {
			throw new NullPointerException("Element null");
		}
		String result = new String("");
		try {
			PathFinder pf = new PathFinder(graph, element1);
			graph.validateVertex(element2);
			if (pf.distanceTo(element2) != Integer.MAX_VALUE) {			
				String ruta = new String("");
				for (String v : pf.pathTo(element2)) {
					ruta += v + " -> ";
				}        
				char[] ruta1 = ruta.toCharArray();
				result = new String(ruta1, 0, ruta1.length-4);
				result += "<br>Distancia: " + pf.distanceTo(element2);
			} else {
				result += "Distancia: 0";
			}
		} catch (IllegalArgumentException e) {
			result = "No se han encontrado resultados para su búsqueda.</br>"
					+ "Puede que haya introducido mal alguno de los elementos.";
		}
		return result;
	}
	
	//Método que devuelve que actor está en qué peliculas y viceversa
	public static String AInB(Graph graph, String element) {
		System.out.println("ENTRA AINB");
		if (element == null) {
			throw new NullPointerException("Categorie null");
		}
		
		String result = new String("");
		try {
			for (String v:graph.adjacentTo(element)) {

				if(graph.st.contains(v)) {
					result += v + "</br>";
				}
			}
		}catch (IllegalArgumentException e) {
			result += "No se han encontrado resultados para '" + element + "'";
		}
		return result;
	}

	public static String categoriesOf(String movie) {
		if (movie == null) {
			throw new NullPointerException("Movie null");
		}

		String[] docs = {"cast.00-06.txt", "cast.06.txt", "cast.action.txt",
						"cast.G.txt", "cast.mpaa.txt", "cast.PG.txt",
						"cast.PG13.txt", "cast.rated.txt", "cast.all.txt"};
		String categories = "";
		String category = new String();
		In in;
		try {
			for (int i = 0; i < docs.length; i++) {
				in = new In("data/imdb-data/" + docs[i]);
				String bodyDoc = in.readAll();				    	//Leo todo el documento
				if(bodyDoc.contains(movie)) {						//Si el documento contiene la línea añado la categoría
					switch (docs[i]) {
					case "cast.00-06.txt": category = "Movies release since 2000";
					break;
					case "cast.06.txt": category = "Movies release in 2006";
					break;
					case "cast.G.txt": category = "Movies rated G by MPAA";
					break;
					case "cast.PG.txt": category = "Movies rated PG by MPAA";
					break;
					case "cast.PG13.txt": category = "Movies rated PG13 by MPAA";
					break;
					case "cast.mpaa.txt": category = "Movies rated by MPAA";
					break;
					case "cast.action.txt": category = "Action Movies";
					break;
					case "cast.rated.txt": category = "Popular Movies";
					break;
					case "cast.all.txt": category = "Over 250,000 movies";
					break;
					default: category = "NOT FOUND";
					break;
					}
					categories += category + "<br>";		//Cambiar por concat, Habia ERROR.
				}
			in.close();
			}
			
			if (categories.isEmpty()) {
				categories = "No se han encontrado resultados para '" + movie + "'";
			}
		}catch(IllegalArgumentException e) {
			throw new IllegalArgumentException();
		}
		return categories;
	}


	public static String MoviesOfCategorie(String category) {
		if (category == null) {
			throw new NullPointerException("Category null");
		}
		In in;
		String result = "";
		if ("NotCategory".equals(category)) {
			result = "Por favor, vuelve atrás y selecciona una categoría.";
		} else {
			try {
				String path = "data/imdb-data/cast." + category + ".txt";
				in = new In(path);
				while (!in.isEmpty()) {
					String line = in.readLine();				    	//Leo linea a linea (cada linea es una película)
					String[] parts = line.split("/");					//Hago un split hasta la primera /
					result += (parts[0]) + "<br>";					//Concateno todas las películas
				}
			}
			catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Error al abrir el archivo");
			}
		}
		return result;
	}

	public static String doAinB(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
		String filePath = "data/imdb-data/cast.all.2.txt";
		String delimiter = "/";
		Graph graph = new Graph(filePath, delimiter);
		String element = request.queryParams("Element1");
		String result = AInB(graph, element);
		return result;
	}

	public static String doDistance(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
		String filePath = "data/imdb-data/cast.all.2.txt";
		String delimiter = "/";
		Graph graph = new Graph(filePath, delimiter);
		String element1 = request.queryParams("Element1");
		String element2 = request.queryParams("Element2");
		String distance = distanceBetweenElements(graph, element1, element2);
		return distance;
	}

	public static String doOfCategories(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
		String category = request.queryParams("Categoria");
		String movies = MoviesOfCategorie(category);
		return movies;
	}

	public static String doCategoriesOf(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
		String movie = request.queryParams("Movie");
    	String category = categoriesOf(movie);
    	return category;
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

	public static String FormularyOfCategories(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
		String body = "<form action='/OfCategories' method='post'>" +
						  "<div>" + 
						  	  "<select name='Categoria'>\n\t<option selected value=NotCategory>Elige categoría</option>" +
						  	  "<option value=00-06.2>Movies released since 2000</option>" +
						  	  "<option value=06>Movies release in 2006</option>" +
						  	  "<option value=G>Movies rated G by MPAA</option>" +
						  	  "<option value=PG>Movies rated PG by MPAA</option>" +
						  	  "<option value=PG13>Movies rated PG13 by MPAA</option>" +
						  	  "<option value=mpaa.2>Movies rated by MPAA</option>" +
						  	  "<option value=action.2>Action Movies</option>" +
						  	  "<option value=rated>Popular Movies</option>" +
						  	  "<option value=all.2>Over 250,000 movies</option>" +
						  	  "</select><input class='button' type='submit' value='Buscar'>"+
						  "</div>" +
					   "</form>";
		return body;
	}

	public static String FormularyCategoriesOf(Request request, Response response) throws ClassNotFoundException, URISyntaxException {
		String body = "<form action='/CategoriesOf' method='post'>" +
						  "<div>" + 
						  	  "<label for='name'>Película: </label>" +
						  	  "<input type='text' id='name' name='Movie'/>" +
						  "</div>" +
						  "<div class='button'>" +
						  	  "<button type='submit'>Buscar</button>" +
						  "</div>" +
					  "</form>";
		return body;
	}

	
	
	public static void prepareDataBase() throws SQLException{
		System.out.println("ENTRA PREPAREDATABASE");
		String[] docs = {"cast.00-06.txt", "cast.06.txt", "cast.action.txt",
				"cast.G.txt", "cast.mpaa.txt", "cast.PG.txt",
				"cast.PG13.txt", "cast.rated.txt", "cast.all.txt"};					//Preparo la lista de documentos donde buscaré las películas
		In in;
		In inGeneral;
		inGeneral = new In("data/imdb-data/all.2.txt");
		String s;

		while ((s = inGeneral.readLine()) != null) {
		    StringTokenizer tokenizer = new StringTokenizer(s, "/");				//Tokenizo cada línea por la /
		    String film = tokenizer.nextToken();									//Me quedo con el primer elemento de cada línea, la película
		    
		    String categories = "";
			String category = new String();
			try {
				Statement statement = (Statement) connection.createStatement();

				// This code only works for PostgreSQL
				((java.sql.Statement) statement).executeUpdate("drop table if exists films");
				((java.sql.Statement) statement).executeUpdate("create table films (film text, actor text)");

				for (int i = 0; i < docs.length; i++) {					//Busco en todos los documentos cada una de las películas, si aparece agrego esta categoría
					in = new In("data/imdb-data/" + docs[i]);
					String bodyDoc = in.readAll();				    	//Leo todo el documento
					if(bodyDoc.contains(film)) {						//Si el documento contiene la línea añado la categoría
						switch (docs[i]) {
						case "cast.00-06.txt": 
							category = "Movies release since 2000";
							break;
						case "cast.06.txt": 
							category = "Movies release in 2006";
							break;
						case "cast.G.txt": 
							category = "Movies rated G by MPAA";
							break;
						case "cast.PG.txt": 
							category = "Movies rated PG by MPAA";
							break;
						case "cast.PG13.txt": 
							category = "Movies rated PG13 by MPAA";
							break;
						case "cast.mpaa.txt": 
							category = "Movies rated by MPAA";
							break;
						case "cast.action.txt": 
							category = "Action Movies";
							break;
						case "cast.rated.txt": 
							category = "Popular Movies";
							break;
						case "cast.all.txt": 
							category = "Over 250,000 movies";
							break;
						default: 
							category = "NOT FOUND";
							break;
						}
						categories += category + "<br>";		//Cambiar por concat, Habia ERROR.
					}
				in.close();
				}
				// Now get film and categories and insert them
				insert(connection, film, categories);
			}catch(IllegalArgumentException e) {
				System.out.println(e);
				throw new IllegalArgumentException();
			}
		}
	}
	
    public static void insert(Connection conn, String film, String categories) {
    	String sql = "INSERT INTO films(film, categories) VALUES(?,?)";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, film);
			pstmt.setString(2, categories);
			pstmt.executeUpdate();
	    } catch (SQLException e) {
	    	System.out.println(e.getMessage());
	    }
    }
    
    public static String select(Connection conn, String table, String film) {
    	String sql = "SELECT F.categories FROM films F WHERE F.film=?";
    	String result = new String();
    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, film);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    		    // read the result set
    		    result += "film = " + rs.getString("film") + "\n";
    		    System.out.println("film = "+rs.getString("film") + "\n");
    		}
    	}catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
    }

	public static void main(String[] args) throws ClassNotFoundException, SQLException, URISyntaxException {
		port(getHerokuAssignedPort());
		staticFileLocation("/public");
		
		URI dbUri = new URI(System.getenv("DATABASE_URL"));
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
		connection = DriverManager.getConnection(dbUrl, username, password);
		prepareDataBase();
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

		get("/FormularyAInB", Main::FormularyAinB);
		get("/FormularyDistanceBetweenElements", Main::FormularyDistanceBetweenElements);
		get("/FormularyOfCategories", Main::FormularyOfCategories);
		get("/FormularyCategoriesOf", Main::FormularyCategoriesOf);
		
		post("/FormularyAInB", Main::FormularyAinB);
		post("/FormularyDistanceBetweenElements", Main::FormularyDistanceBetweenElements);
		post("/FormularyOfCategories", Main::FormularyOfCategories);
		post("/FormularyCategoriesOf", Main::FormularyCategoriesOf);

		post("/AInB", Main::doAinB);
		post("/DistanceBetweenElements", Main::doDistance);
		post("/OfCategories", Main::doOfCategories);
		post("/CategoriesOf", Main::doCategoriesOf);


	}
//Para la base de datos coger las 9999 peliculas de all.2, buscar con el codigo de 
	// CategoriesOf las categorias de cada pelicula y añadir un campo de pelicula y otro de categorias.
	//
	static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
	}
}