package hu.gamf.springlectureproject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import soapclient.*;

@Controller
public class BankController {

    @GetMapping("/mnb")
    @ResponseBody
    public String getSite(Model model) {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        try {
            String currencies = service.getCurrencies();
            System.out.println("currencies: " + currencies);
            return currencies;
        } catch (MNBArfolyamServiceSoapGetCurrenciesStringFaultFaultMessage e) {
            return e.getMessage();
        }
    }


}
