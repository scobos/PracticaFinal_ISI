package urjc.isi.practicaFinal;

import static spark.Spark.*;

import spark.Request;
import spark.Response;

import java.net.URISyntaxException;

public class Main {
	
	public static int distanceBetweenElements(Graph graph,String element1,String element2) {
		return 0;
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