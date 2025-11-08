package hu.gamf.springlectureproject.controllers;

import com.oanda.v20.Context;
import com.oanda.v20.account.AccountSummary;
import hu.gamf.springlectureproject.tools.Config;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TradeController {

    @GetMapping("/trade/account_info")
    public String getAccountInfo(Model model) {
        Context ctx = new Context(Config.URL, Config.TOKEN);
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
}
