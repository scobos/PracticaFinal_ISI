package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.junit.*;
import spark.Request;
import spark.Response;
import static org.mockito.Mockito.*;

public class AppTest {
	
	String filePath = "data/other-data/tinyMovies.txt";
	String delimiter = "/";
    Graph graph = new Graph(filePath, delimiter);	
    String actor1 = "";
	String actor2 = "";
	String movie = "";
	String category = "";
	Request request = null;
	Response response = null;
	
	
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
	public void TestdistanceActors5() {
		String element1 = "Actor A";
		String element2 = "Actor D";
		assertEquals("Distancia: 0",Main.distanceBetweenElements(graph, element1, element2));
			 
	}
	
	//Uno de los elementos no se encuentra en la lista
	@Test()
	public void TestdistanceActors6() {
		String element1 = "Act";
		String element2 = "Actor A";
		String answer = "No se han encontrado resultados para su búsqueda.</br>"
					  + "Puede que haya introducido mal alguno de los elementos.";
		assertEquals(answer,Main.distanceBetweenElements(graph, element1, element2));

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
	
	
	//El nombre de la película es null, FLUJO--> ENTRA EN EL PRIMER IF
	@Test(expected=NullPointerException.class)
	public void testCategoriesOf1() {
		movie = null;
	    Main.categoriesOf(movie);
	}

	//Error al abrir alguno de los ficheros, FLUJO --> entramos en la excepción
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

	//Happy Path, FLUJO --> ENTRA EN EL BUCLE FOR, SEGUNDO IF Y ALGÚN CAMPO DEL SWITCH
	@Test()
	public void testCategoriesOf3() {
		String answer = "Movies release since 2000<br>Action Movies<br>";
		assertEquals(answer, Main.categoriesOf(movie));
	}
	//No encuentra categoría, FLUJO --> ENTRA EN EL TERCER IF
	@Test() 
	public void testCategoriesOf4() {
		String movie = "Pelicula inventada";
		String answer = "No se han encontrado resultados para '" + movie + "'";
		assertEquals(answer,Main.categoriesOf(movie));
	}
	
	//La categoría es null, FLUJO --> ENTRA EN EL PRIMER IF
	@Test(expected=NullPointerException.class)
	public void MoviesOfCategory1() {
		category = null;
		Main.MoviesOfCategorie(category);
	}

	//La categoría no coincide con las posibles, FLUJO--> entra en el catch
	@Test(expected=IllegalArgumentException.class)
	public void MoviesOfCategory2() {
		category = " Categoria Inventada";
		Main.MoviesOfCategorie(category);
	}

	//Happy path, FLUJO --> no entra en el segundo if y entra en el while
	@Test()
	public void MoviesOfCategory3() throws IOException {
		String categorie = "action";
		In in;
		in = new In("data/other-data/action_movies.txt");
		String bodyDoc = in.readAll();	//para poder comparar el contenido del fichero con la salida del método
		assertEquals(bodyDoc, Main.MoviesOfCategorie(categorie));
	}
	
	//Se selecciona la opción elige una categoría, FLUJO--> Entra en el segundo if
	@Test()
	public void MoviesofCategory4() {
		String category = "NotCategory";
		String answer = "Por favor, vuelve atrás y selecciona una categoría.";
		assertEquals(answer, Main.MoviesOfCategorie(category));
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doAInB1() throws ClassNotFoundException, URISyntaxException {
		Main.doAinB(request, response);
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doDistance1() throws ClassNotFoundException, URISyntaxException {
		Main.doDistance(request, response);
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doOfCategories1() throws ClassNotFoundException, URISyntaxException {
		Main.doOfCategories(request, response);
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doCategoriesOf1() throws ClassNotFoundException, URISyntaxException {
		Main.doCategoriesOf(request, response);
	}
	
	//Request and Response null
	@Test(expected= NullPointerException.class)
	public void doPrepareDataBase1() throws ClassNotFoundException, URISyntaxException, SQLException {
		Main.doPrepareDataBase(request, response);
	}
	
	
	@Test()
	public void In1() {
		In in = new In(filePath);
		while(in.hasNextLine()) {
			in.readLine();
		}
		assertEquals(null, in.readLine());
	}
	
	/*@Test()
	public void select1() {
		 = mock(String.class);
		String film = "101 Dalmatians (1996)";
		String answer = "Movies rated G by MPAA<br>Movies rated by MPAA<br>Popular Movies<br><br/>";
		assertEquals(answer, Main.select(film));
	}*/
	

}
