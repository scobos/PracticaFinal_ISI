package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.*;


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
	
	//Uno de los actores es null
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

	//Happy path
	@Test()
    public void TestdistanceActors4() {
        assertEquals("Actor A -> Movie 1 -> Actor B<br>Distancia: 2", Main.distanceBetweenElements(graph, actor1, actor2));
    }
    //El actor es null
	@Test(expected=NullPointerException.class)
    public void TestAInB1() {
		String answer = "";
		actor1 = null;
		assertEquals(answer, Main.AInB(graph, actor1));
    }
	
	//El grafo está vacío
	@Test()
    public void TestAInB2() {
		String answer = "";
		graph = new Graph();
		assertEquals(answer, Main.AInB(graph, actor1));
    }
	
	//Happy path
	@Test()
    public void TestAInB3() {
		String answer = "Movie 1<br>Movie 3<br>";
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
	        File fichero = new File ("data/other-data/action_movies.txt");
	        BufferedWriter bw;
	        bw = new BufferedWriter(new FileWriter(fichero)); 
	        bw.write(Main.MoviesOfCategorie(categorie)); //Escribo en el fichero el contenido del método
	        bw.close(); 
	        In in;
	        in = new In("data/other-data/action_movies.txt");
			String bodyDoc = in.readAll();	//para poder comparar el contenido del fichero con la salida del método
			assertEquals(bodyDoc, Main.MoviesOfCategorie(categorie));
	    }
		
		
}
