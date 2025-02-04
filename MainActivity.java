import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.io.IOException;
import fi.iki.elonen.NanoHTTPD;

public class MainActivity extends AppCompatActivity {

    private static final int PORT = 4040;
    private HTTPServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnToggle = findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            startActivity(intent);
        });

        try {
            server = new HTTPServer();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class HTTPServer extends NanoHTTPD {
        public HTTPServer() throws IOException {
            super(PORT);
        }

        @Override
        public Response serve(IHTTPSession session) {
            if ("POST".equals(session.getMethod().name())) {
                Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                startActivity(intent);
                return newFixedLengthResponse(Response.Status.OK, "text/plain", "Toggling Airplane Mode");
            }
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
        }
    }
}
