package hu.gamf.springlectureproject.controllers;

import com.oanda.v20.Context;
import com.oanda.v20.ExecuteException;
import com.oanda.v20.RequestException;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import hu.gamf.springlectureproject.models.ActualPriceDTO;
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
            model.addAttribute("error", e.getMessage());
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
    public String getActualPrices(@ModelAttribute ActualPriceDTO actualPriceDTO, Model model) {
        model.addAttribute("dto", actualPriceDTO);

        try {
            PricingGetRequest request = new PricingGetRequest(Config.ACCOUNTID, new ArrayList<>(Collections.singletonList(actualPriceDTO.getInstrument())));
            PricingGetResponse response =  ctx.pricing.get(request);

            model.addAttribute("data", response.getPrices());

            return "trade/actual_prices_result";
        } catch (RequestException | ExecuteException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
