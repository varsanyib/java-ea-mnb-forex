package hu.gamf.springlectureproject.controllers;

import com.oanda.v20.Context;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.instrument.CandlestickGranularity;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.trade.Trade;
import com.oanda.v20.trade.TradeCloseRequest;
import com.oanda.v20.trade.TradeSpecifier;
import com.oanda.v20.transaction.OrderFillTransaction;
import com.oanda.v20.transaction.TransactionID;
import hu.gamf.springlectureproject.models.ActualPriceDTO;
import hu.gamf.springlectureproject.models.ClosePositionDTO;
import hu.gamf.springlectureproject.models.HistPriceDTO;
import hu.gamf.springlectureproject.models.OpenPositionDTO;
import hu.gamf.springlectureproject.tools.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class TradeController {
    private static final Context ctx = new Context(Config.URL, Config.TOKEN);

    @GetMapping("/trade/account_info")
    public String getAccountInfo(Model model) {
        try {
            AccountSummary summary = ctx.account.summary(Config.ACCOUNTID).getAccount();

            model.addAttribute("account", summary);
            return "trade/account";
        } catch (Exception e) {
            System.out.println("Hiba történt a felhasználói fiók adatainak lekérdezése közben!" + e.getMessage());
            model.addAttribute("errors", e);
            return "error";
        }
    }

    @GetMapping("/trade/actual_prices")
    public String getActualPrices(Model model) {
        model.addAttribute("dto", new ActualPriceDTO());
        model.addAttribute("instruments", new ArrayList<>(Arrays.asList("EUR_USD", "USD_JPY", "GBP_USD", "USD_CHF")));
        return  "trade/actual_prices_form";
    }

    @PostMapping("/trade/actual_prices")
    public String showActualPrices(@ModelAttribute ActualPriceDTO actualPriceDTO, Model model) {
        model.addAttribute("dto", actualPriceDTO);

        try {
            PricingGetRequest request = new PricingGetRequest(Config.ACCOUNTID, new ArrayList<>(Collections.singletonList(actualPriceDTO.getInstrument())));
            PricingGetResponse response =  ctx.pricing.get(request);

            model.addAttribute("data", response.getPrices());

            return "trade/actual_prices_result";
        } catch (RequestException | ExecuteException e) {
            model.addAttribute("errors", e);
            return "error";
        }
    }

    @GetMapping("/trade/hist_prices")
    public String getHistPrices(Model model) {
        model.addAttribute("dto", new HistPriceDTO());
        model.addAttribute("instruments", new ArrayList<>(Arrays.asList("EUR_USD", "USD_JPY", "GBP_USD", "USD_CHF")));
        model.addAttribute("granularities", new ArrayList<>(Arrays.asList("M1", "H1", "D", "W", "M")));

        return "trade/hist_prices_form";
    }

    @PostMapping("/trade/hist_prices")
    public String showHistPrices(@ModelAttribute HistPriceDTO histPriceDTO, Model model) {
        try {

            InstrumentCandlesRequest request = new InstrumentCandlesRequest(new InstrumentName(histPriceDTO.getInstrument()));
            switch (histPriceDTO.getGranularity()) {
                case "M1":
                    request.setGranularity(CandlestickGranularity.M1);
                    break;
                case "H1":
                    request.setGranularity(CandlestickGranularity.H1);
                    break;
                case "D":
                    request.setGranularity(CandlestickGranularity.D);
                    break;
                case "W":
                    request.setGranularity(CandlestickGranularity.W);
                    break;
                case "M":
                    request.setGranularity(CandlestickGranularity.M);
                    break;
            }

            request.setCount(10L);
            InstrumentCandlesResponse response =  ctx.instrument.candles(request);

            model.addAttribute("dto", histPriceDTO);
            model.addAttribute("data", response.getCandles());

            return "trade/hist_prices_result";

        } catch (Exception e) {
            model.addAttribute("errors", e);
            return "error";
        }
    }

    @GetMapping("/trade/open_position")
    public String getOpenPosition(Model model) {
        model.addAttribute("dto", new OpenPositionDTO());
        model.addAttribute("instruments", new ArrayList<>(Arrays.asList("EUR_USD", "USD_JPY", "GBP_USD", "USD_CHF")));
        return "trade/open_position_form";
    }

    @PostMapping("/trade/open_position")
    public String showOpenPosition(@ModelAttribute OpenPositionDTO openPositionDTO, Model model) {
        try {
            InstrumentName instrument = new InstrumentName(openPositionDTO.getInstrument());
            OrderCreateRequest request = new OrderCreateRequest(Config.ACCOUNTID);
            MarketOrderRequest marketOrderRequest = new MarketOrderRequest();
            marketOrderRequest.setInstrument(instrument);
            marketOrderRequest.setUnits(openPositionDTO.getUnits());
            request.setOrder(marketOrderRequest);

            OrderCreateResponse response =  ctx.order.create(request);
            OrderFillTransaction transaction = response.getOrderFillTransaction();
            if (transaction == null) {
                throw new Exception("Tranzakció létrehozása sikertelen! (Csak hétköznap lehet tranzakciót létrehozni!)");
            }


            model.addAttribute("dto", openPositionDTO);
            model.addAttribute("data", transaction);

            return "trade/open_position_result";

        } catch (Exception e) {
            model.addAttribute("errors", e);
            return "error";
        }
    }

    @GetMapping("/trade/positions")
    public String getPositions(Model model) {
        try {
            List<Trade> trades = ctx.trade.listOpen(Config.ACCOUNTID).getTrades();
            model.addAttribute("data", trades);

            return "trade/positions";
        } catch (Exception e) {
            model.addAttribute("errors", e);
            return "error";
        }
    }

    @GetMapping("/trade/close_position")
    public String getClosePosition(Model model) {
        model.addAttribute("dto", new ClosePositionDTO());
        return "trade/close_position_form";
    }

    @PostMapping("/trade/close_position")
    public String showClosePosition(@ModelAttribute ClosePositionDTO closePositionDTO, Model model) {
        try {
            model.addAttribute("dto", closePositionDTO);
            ctx.trade.close(new TradeCloseRequest(Config.ACCOUNTID, new TradeSpecifier(closePositionDTO.getTradeIdInString())));

            return "trade/close_position_result";

        } catch (Exception e) {
            model.addAttribute("errors", e);
            return "error";
        }
    }

}
