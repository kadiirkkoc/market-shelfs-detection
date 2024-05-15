package marketshelfs.detection.controller;


import marketshelfs.detection.dtos.ShelfDto;
import marketshelfs.detection.service.ShelfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/shelf")
public class ShelfController {

    private final ShelfService shelfService;

    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @PostMapping()
    public ResponseEntity<Map<String,Object>> addShelf(@ModelAttribute ShelfDto shelfDto ) {
        return new ResponseEntity<>(shelfService.addShelf(shelfDto), HttpStatus.CREATED);
    }
}
