package marketshelfs.detection.service.impl;

import marketshelfs.detection.repository.ShelfRepository;
import marketshelfs.detection.service.ShelfService;
import org.springframework.stereotype.Service;

@Service
public class ShelfServiceImpl implements ShelfService {

    private final ShelfRepository shelfRepository;

    public ShelfServiceImpl(ShelfRepository shelfRepository) {
        this.shelfRepository = shelfRepository;
    }


}
