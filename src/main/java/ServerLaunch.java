import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class ServerLaunch {

    public static void main(String[] args) {
        staticFileLocation("/public");
        get("/", (req, res) -> {
            res.redirect("index.html");
            return null;
        });
    }
}
