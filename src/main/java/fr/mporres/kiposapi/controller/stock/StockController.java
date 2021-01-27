package fr.mporres.kiposapi.controller.stock;

import fr.mporres.kiposapi.controller.user.response.UserResponse;
import fr.mporres.kiposapi.exception.ApiException;
import fr.mporres.kiposapi.services.stock.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * User WS
 */
@RestController
@RequestMapping(value = "/stocks")
public class StockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * Stock fetching
     *
     * @param stockSymbol stock's symbol
     * @return the corresponding stock if it exists, 400 error otherwise
     */
    @PreAuthorize("hasAuthority('STOCK_GET')")
    @GetMapping(value = "/{stockSymbol}")
    public @ResponseBody
    UserResponse getUser(@PathVariable(value = "stockSymbol") String stockSymbol) {
        LOGGER.info("Fetching stock {}", stockSymbol);
        return stockService.getStockBySymbol(stockSymbol)
            .map(this::createResponse)
            .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, String.format("Unable to retrieve stock %d. Unknown symbol.", stockSymbol)));
    }
}
