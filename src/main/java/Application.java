import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Application {
    private static Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            ApiContextInitializer.init();
            TelegramBotsApi botsApi = new TelegramBotsApi();

            // Set up Http proxy
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

            botOptions.setProxyHost(Constants.PROXY_HOST);
            botOptions.setProxyPort(Constants.PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

            // Register your newly created AbilityBot
            BarInvitationBot bot = new BarInvitationBot(botOptions);
            botsApi.registerBot(bot);
            logger.info("BarInvitationBot registered!");

        } catch (TelegramApiException e) {
            logger.error("Telegram api exception", e);
        }
    }
}
