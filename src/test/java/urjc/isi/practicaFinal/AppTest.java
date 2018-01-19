package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.*;
import spark.Request;
import spark.Response;


public class AppTest {
	
	String filePath = "data/other-data/tinyMovies.txt";
	String delimiter = "/";
    Graph graph = new Graph(filePath, delimiter);	
    String actor1 = "";
	String actor2 = "";
	String movie = "";
	String category = "";
	
	@Before public void setUp()         // set up test fixture
	{  
		actor1 = "Actor A";
		actor2 = "Actor B";
		movie = "100 kilos de plomo (2002)";
		category = "action";
	}
	
	@After public void restaurarFichero()
	{
		File fich = new File("data/cast.G.txt");
		File fich2 = new File("data/imdb-data/cast.G.txt");
		if ( !fich2. exists() && fich.exists()) {
			boolean success = fich.renameTo(fich2);
			if(!success) {
				System.out.println("Error intentando mover el fichero2");
			}
		}
	}
	
	//Uno de los actores es null, FLUJO--> entramos en la excepción
	@Test(expected=NullPointerException.class)
    public void TestdistanceActors1() {
		actor1 = null;
		Main.distanceBetweenElements(graph, actor1, actor2);
    }
	
	//Ambos actores son null
	@Test(expected=NullPointerException.class)
    public void TestdistanceActors2() {
		actor1 = null;
		actor2 = null;
		Main.distanceBetweenElements(graph, actor1, actor2);
    }
	
	//El grafo está vacío
	@Test(expected=NullPointerException.class)
    public void TestdistanceActors3() {
		Graph graph = new Graph();
		Main.distanceBetweenElements(graph, actor1, actor2);
    }

	//Happy path FLUJO--> ENTRA POR EL IF Y EL BUCLE FOR
	@Test()
    public void TestdistanceActors4() {
        assertEquals("Actor A -> Movie 1 -> Actor B<br>Distancia: 2", Main.distanceBetweenElements(graph, actor1, actor2));
    }
	
	//CONTROL DE FLUJO DISTANCE BETWEEN ELEMENTS(No entra en el if ni bucle for)
	@Test()
	public void TEstdistanceActors5() {
		String element1 = "Actor A";
		String element2 = "Actor D";
		assertEquals("Distancia: 0",Main.distanceBetweenElements(graph, element1, element2));
			 
	}
		
		
    //El actor es null, FLUJO --> ENTRA EN EL PRIMER IF
	@Test(expected=NullPointerException.class)
    public void TestAInB1() {
		String answer = "";
		actor1 = null;
		assertEquals(answer, Main.AInB(graph, actor1));
    }
	
	//El grafo está vacío, FLUJO --> ENTRA EN EL CATCH
	@Test()
    public void TestAInB2() {
		String answer = "No se han encontrado resultados para 'Actor A'";
		graph = new Graph();
		assertEquals(answer, Main.AInB(graph, actor1));
    }
	
	//Happy path, FLUJO --> ENTRA POR BUCLE FOR Y SEGUNDO IF
	@Test()
    public void TestAInB3() {
		String answer = "Movie 1</br>Movie 3</br>";
		assertEquals(answer, Main.AInB(graph, actor1));
    }
	
	
	//El nombre de la película es null
	@Test(expected=NullPointerException.class)
	public void testCategoriesOf1() {
		movie = null;
	    Main.categoriesOf(movie);
	}

	//Error al abrir alguno de los ficheros
	@Test(expected=IllegalArgumentException.class)
	public void testCategoriesOf2() {
		File fich = new File("data/imdb-data/cast.G.txt");
		File fich2 = new File("data/cast.G.txt");
		boolean success = fich.renameTo(fich2);
		if (!success) {
			System.out.println("Error intentando mover el fichero2");
		}
		Main.categoriesOf(movie);
	}

	//Happy Path
	@Test()
	public void testCategoriesOf3() {
		String answer = "Movies release since 2000<br>Action Movies<br>Over 250,000 movies<br>";
		assertEquals(answer, Main.categoriesOf(movie));
	}
	
	//La categoría es null
	@Test(expected=NullPointerException.class)
	public void MoviesOfCategory1() {
		category = null;
		Main.MoviesOfCategorie(category);
	}

	//La categoría no coincide con las posibles
	@Test(expected=IllegalArgumentException.class)
	public void MoviesOfCategory2() {
		category = " Categoria Inventada";
		Main.MoviesOfCategorie(category);
	}

	//Happy path
	@Test()
	public void MoviesOfCategory3() throws IOException {
		String categorie = "action";
		In in;
		in = new In("data/other-data/action_movies.txt");
		String bodyDoc = in.readAll();	//para poder comparar el contenido del fichero con la salida del método
		assertEquals(bodyDoc, Main.MoviesOfCategorie(categorie));
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doAInB1() throws ClassNotFoundException, URISyntaxException {
		Request request = null;
		Response response = null;
		Main.doAinB(request, response);
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doDistance1() throws ClassNotFoundException, URISyntaxException {
		Request request = null;
		Response response = null;
		Main.doDistance(request, response);
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doOfCategories1() throws ClassNotFoundException, URISyntaxException {
		Request request = null;
		Response response = null;
		Main.doOfCategories(request, response);
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doCategoriesOf1() throws ClassNotFoundException, URISyntaxException {
		Request request = null;
		Response response = null;
		Main.doCategoriesOf(request, response);
	}
	
	
	
	
	
}
