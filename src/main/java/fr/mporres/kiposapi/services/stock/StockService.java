package fr.mporres.kiposapi.services.stock;

import fr.mporres.kiposapi.services.stock.dto.Stock;
import fr.mporres.yahoofinanceapi.Modules;
import fr.mporres.yahoofinanceapi.YahooFinanceApi;
import fr.mporres.yahoofinanceapi.dto.quoteSummary.QuoteSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service that manages /stocks calls
 */
@Component
public class StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);


    /**
     * User fetching by id
     *
     * @param stockSymbol    stock's symbol
     * @return the corresponding stock if it exists, empty otherwise
     */
    public Optional<Stock> getStockBySymbol(String stockSymbol) {
        QuoteSummary response = YahooFinanceApi.fetch(stockSymbol, Modules.ASSET_PROFILE);
    }
}
