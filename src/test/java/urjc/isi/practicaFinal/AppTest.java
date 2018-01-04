package urjc.isi.practicaFinal;

import static org.junit.Assert.*;
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
        assertEquals(2, Main.distanceBetweenElements(graph, actor1, actor2));
    }
}
