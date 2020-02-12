package pl.cecherz.calcbill.controller.json.v2;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import pl.cecherz.calcbill.model.json.Owner;
import pl.cecherz.calcbill.utils.JsonDataManager;
import pl.cecherz.calcbill.utils.MessageBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v2/owners")
@Component("OwnerControllerJSON-V2")
public class OwnerController extends HTTPHeaderUtils {
    /* Wprowadzenie identyfikacji klasy dla narzędzia MessageBuilder */
    private MessageBuilder message = new MessageBuilder(pl.cecherz.calcbill.controller.json.v2.OwnerController.class);

    /* Dane zapisane w postaci listy przechowywane są tylko w pamięci tymczasowej */
    private List<Owner> ownersList = JsonDataManager.initOwners();

    /* Zwraca JSON-a reprezentację kolekcji z obiektami użytkowników.
    Przechowuje informacje w cachu przez 5 minut */
    @GetMapping
    public ResponseEntity<List<Owner>> getAllOwners() {
        message.getInfo("V2 :: getAllOwners()", ownersList);
        return ResponseEntity.ok().header("Cache-Control", "max-age" + "=300").body(ownersList);
    }
    /* Zwraca listę użytkowników w postaci HTML-a */
    @GetMapping(value = "/html",produces = MediaType.TEXT_HTML_VALUE, params = "color")
    public String getAllOwnersNames(@RequestParam("color") String color) {
        String style = "<style>p {color: " + color + "}</style>";
        String start_html = "<!DOCTYPE html>\n<html><head>"
                + style +
                "\n<title>Owners name</title></head>\n<body>\n";
        String end_html = "\n</body>\n</html>";
        return start_html +
                ownersList.stream()
                        .flatMap(owner -> Stream.of("<p>" + owner.getName(), owner.getSurname() + "</p>"))
                .collect(Collectors.joining("\n"))
                + end_html;
    }
}
