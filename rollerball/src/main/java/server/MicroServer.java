package server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.accounts.AccountManager;
import server.api.*;
import server.matches.*;
import server.notifications.NotificationManager;
import spark.Request;
import spark.Response;
import spark.Spark;
import static spark.Spark.secure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MicroServer {

    private final Logger log = LoggerFactory.getLogger(MicroServer.class);

    MicroServer(int serverPort) {
        configureServer(serverPort);
        serveStaticPages();
        processRestfulAPIrequests();

    }

    private void configureServer(int serverPort) {
        Spark.port(serverPort);
        String keystoreFile = System.getenv("KEYSTORE_FILE");
        String keystorePassword = System.getenv("KEYSTORE_PASSWORD");
        if (keystoreFile != null && keystorePassword != null) {
            secure(keystoreFile, keystorePassword, null, null);
        }
    }


    private void serveStaticPages() {
        String path = "/public/";
        Spark.staticFileLocation(path);
        Spark.get("/", (req, res) -> { res.redirect("index.html"); return null; });
    }

    private void processRestfulAPIrequests() {
        Spark.get("/api/echo", this::echoHTTPrequest);
        Spark.post("/api/register", this::handleRegisterRequest);
        Spark.post("/api/login", this::handleLoginRequest);
        Spark.post("/api/notifications", this::handleNotificationsRequest);
        Spark.post("/api/message", this::handleMessageRequest);
        Spark.post("/api/inviteAnswer", this::handleInviteResponses);
    }

    private Object handleInviteResponses(Request request, Response response) {
        response.type("application/json");
        Gson gson = new GsonBuilder().registerTypeAdapter(Match.class, new Match.MatchSerializer()).create();

        InviteAnswer answer = gson.fromJson(request.body(), InviteAnswer.class);
        if(!answer.verify()){
            response.status(401);
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.success = false;
            messageResponse.errorMessage = "Authentication Error";
            return gson.toJson(messageResponse);
        }
        return gson.toJson(MatchManager.createMatchFromInvite(answer));
    }

    private String handleMessageRequest(Request request, Response response) {
        response.type("application/json");
        Gson gson = new GsonBuilder().create();
        MessageRequest messageRequest = gson.fromJson(request.body(), MessageRequest.class);
        MessageResponse messageResponse;
        if(!messageRequest.verify()){
            response.status(401);
            messageResponse = new MessageResponse();
            messageResponse.success = false;
            messageResponse.errorMessage = "Authentication Error";
        }
        else{
            messageResponse = NotificationManager.sendMessage(messageRequest);
        }
        return gson.toJson(messageResponse);
    }


    private String handleNotificationsRequest(Request request, Response response) {
        response.type("application/json");
        response.header("Access-Control-Allow-Origin", "*");
        Gson gson = new GsonBuilder().create();
        NotificationsRequest notificationsRequest = gson.fromJson(request.body(), NotificationsRequest.class);
        if(!notificationsRequest.verify()){
            response.status(401);
            return "{\"message\": \"Authentication Error\"}";
        }
        return gson.toJson(NotificationManager.getRecentOrUnreadNotifications(notificationsRequest.getAccountId()));
    }

    private String echoHTTPrequest(Request request, Response response) {
        response.type("application/json");
        response.header("Access-Control-Allow-Origin", "*");
        return HTTPrequestToJson(request);
    }

    private String handleRegisterRequest(Request request, Response response){
        response.type("application/json");
        Gson gson = new GsonBuilder().create();
        RegistrationRequest registrationRequest= gson.fromJson(request.body(), RegistrationRequest.class);
        return gson.toJson(AccountManager.registerUser(registrationRequest));
    }

    private String handleLoginRequest(Request request, Response response){
        response.type("application/json");
        response.header("Access-Control-Allow-Origin", "*");
        Gson gson = new GsonBuilder().create();
        LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
        return gson.toJson(AccountManager.loginUser(loginRequest));
    }

    private String handleMoveRequest(Request request, Response response){
	response.type("application/json");
	Gson gson = new GsonBuilder().create();
	MoveRequest moveRequest = gson.fromJson(request.body(), MoveRequest.class);
	Match match = MatchManager.getMatchById(moveRequest.matchId, moveRequest.getAccountId());
	MoveResponse r = null;
	if (moveRequest.forfeit)
	{
	    r = new MoveResponse();
	    r.success = true;
	    r.errorMessage = null;
	    r.gameOver = "RESIGN";
	}
	else if (match == null)
	{
	    r = new MoveResponse();
	    r.success = false;
	    r.errorMessage = "Failed to find your match";
	    r.gameOver = null;
	}
	else
	{
	    r = match.makeMove(moveRequest);
	}
	if (r.gameOver == null || r.gameOver.equals(""))
	{
	    //there was a winner, we need to update history and the sql server accordingly
	    //possibly add a notification to both users.
	}
	return gson.toJson(response);
    }

    private String HTTPrequestToJson(Request request) {
        return "{\n"
                + "\"attributes\":\"" + request.attributes() + "\",\n"
                + "\"body\":\"" + request.body() + "\",\n"
                + "\"contentLength\":\"" + request.contentLength() + "\",\n"
                + "\"contentType\":\"" + request.contentType() + "\",\n"
                + "\"contextPath\":\"" + request.contextPath() + "\",\n"
                + "\"cookies\":\"" + request.cookies() + "\",\n"
                + "\"headers\":\"" + request.headers() + "\",\n"
                + "\"host\":\"" + request.host() + "\",\n"
                + "\"ip\":\"" + request.ip() + "\",\n"
                + "\"params\":\"" + request.params() + "\",\n"
                + "\"pathInfo\":\"" + request.pathInfo() + "\",\n"
                + "\"serverPort\":\"" + request.port() + "\",\n"
                + "\"protocol\":\"" + request.protocol() + "\",\n"
                + "\"queryParams\":\"" + request.queryParams() + "\",\n"
                + "\"requestMethod\":\"" + request.requestMethod() + "\",\n"
                + "\"scheme\":\"" + request.scheme() + "\",\n"
                + "\"servletPath\":\"" + request.servletPath() + "\",\n"
                + "\"session\":\"" + request.session() + "\",\n"
                + "\"uri()\":\"" + request.uri() + "\",\n"
                + "\"url()\":\"" + request.url() + "\",\n"
                + "\"userAgent\":\"" + request.userAgent() + "\"\n"
                + "}";
    }

    public static void main(String[] args){
        MicroServer myServer = new MicroServer(8085);

    }

}
