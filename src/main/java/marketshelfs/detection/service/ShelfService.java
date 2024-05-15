package marketshelfs.detection.service;

import marketshelfs.detection.dtos.ShelfDto;

import java.util.Map;

public interface ShelfService {

    Map<String, Object> addShelf(ShelfDto shelfDto);
}
