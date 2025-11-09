package hu.gamf.springlectureproject.controllers;
import hu.gamf.springlectureproject.models.BankDTO;
import hu.gamf.springlectureproject.models.Currency;
import hu.gamf.springlectureproject.tools.CustomXMLParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import soapclient.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BankController {

    @GetMapping("/mnb")
    public String getSite(Model model) {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        model.addAttribute("dto", new BankDTO());

        try {
            NodeList nodes = CustomXMLParser.parse(service.getCurrencies()).getElementsByTagName("Curr");

            List<String> currencies = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                currencies.add(nodes.item(i).getTextContent());
            }

            //HUF - HUF exchange does not exist
            if (currencies.contains("HUF")) {
                currencies.remove("HUF");
            }

            model.addAttribute("data", currencies);
            return "bank/form";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/mnb")
    public String postSite(@ModelAttribute BankDTO bankDTO, Model model) {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        try {
            Document doc = CustomXMLParser.parse(service.getExchangeRates(bankDTO.getStartDate(), bankDTO.getEndDate(), bankDTO.getSelectedCurrency()));
            NodeList dayNodes = doc.getElementsByTagName("Day");

            ArrayList<Currency> exchangeRates = new ArrayList<>();
            //Reverse (newest to last)
            for (int i = dayNodes.getLength() - 1; i >= 0; i--) {
                Element dayElement = (Element) dayNodes.item(i);
                exchangeRates.add(new Currency(dayElement.getAttribute("date"), dayElement.getTextContent()));
            }
            model.addAttribute("data", exchangeRates);
            model.addAttribute("chartLabels", exchangeRates.stream().map(Currency::getDate).toList());
            model.addAttribute("chartData", exchangeRates.stream().map(Currency::getCurrency).toList());
            return "bank/view";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }

    }


}
