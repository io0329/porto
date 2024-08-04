import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    static final Logger logger = LogManager.getLogger(App.class.getName());

    public static void main(String[] args) throws IOException {
        // 서버를 설정하고 시작합니다.
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // 기본 executor를 사용합니다.
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            // User-Agent 헤더를 가져옵니다.
            String userAgent = t.getRequestHeaders().getFirst("User-Agent");

            // 로그 기록
            System.out.println("Logging User-Agent: " + userAgent);

            // 취약점 발생 지점: User-Agent 로그 기록
            logger.error(userAgent);

            String response = "Logged User-Agent: " + userAgent;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
