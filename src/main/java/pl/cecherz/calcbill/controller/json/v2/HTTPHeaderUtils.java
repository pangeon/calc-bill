package pl.cecherz.calcbill.controller.json.v2;

import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Objects;

public class HTTPHeaderUtils {

    private MessageBuilder message = new MessageBuilder(HTTPHeaderUtils.class);

    /* metoda zwraca domyślną wartość wartości nagłówka w postaci JSON-a
    wykorzystano narzędzie: import com.google.gson.Gson
    <dependency>
		<groupId>com.google.code.gson</groupId>
		<artifactId>gson</artifactId>
		<version>2.8.4</version>
	</dependency>
	*/
    @GetMapping(value = "/all-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity showHeaders(@RequestHeader Map<String, String> headers) {
        Gson DATA_JSON_TEMPATE = new Gson();
        headers.forEach((key, value) ->
                message.getInfo(String.format("showHeaders() -- '%s' = %s", key, value))
        );
        return ResponseEntity.ok(DATA_JSON_TEMPATE.toJson(headers));
    }
    @GetMapping(value = "/url-info", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getBaseUrl(@RequestHeader HttpHeaders headers) {
        InetSocketAddress host = headers.getHost();
        String URL = "http://" + Objects.requireNonNull(host).getHostName() + ":" + host.getPort();
        return ResponseEntity.ok(URL);
    }
}
