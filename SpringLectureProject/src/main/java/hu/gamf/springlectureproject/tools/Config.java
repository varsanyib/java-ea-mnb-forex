package hu.gamf.springlectureproject.tools;
import com.oanda.v20.account.AccountID;
import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String URL = "https://api-fxpractice.oanda.com";
    public static final AccountID ACCOUNTID = new AccountID(dotenv.get("ACCOUNT_ID"));
    public static final String TOKEN = dotenv.get("ACCOUNT_TOKEN");
}
