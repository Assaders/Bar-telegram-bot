import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Consumer;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

public class BarInvitationBot extends AbilityBot {

    private final ResponseHandler responseHandler;
    private final DBManager dbManager;

    public BarInvitationBot(DefaultBotOptions options) {
        super(Constants.BOT_TOKEN, Constants.BOT_USERNAME, options);
        dbManager = new DBManager(db);
        responseHandler = new ResponseHandler(sender, dbManager);
    }

    @Override
    public int creatorId() {
        return Constants.CREATOR_ID;
    }

    public Ability replyToStart() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                            if (ctx.arguments().length == 0) {
                                responseHandler.replyToStart(ctx.chatId());
                            } else {
                                responseHandler.replyToStart(ctx.chatId(), ctx.firstArg());
                            }
                        }
                )
                .build();
    }

    public Ability replyToNonCommand() {
        return Ability
                .builder()
                .name(DEFAULT)
                .locality(ALL)
                .privacy(PUBLIC)
                .input(1)
                .action(ctx -> responseHandler.replyToNonCommand(ctx.chatId(), ctx.firstArg()))
                .build();
    }

    public Reply replyToButtons() {
        Consumer<Update> action = upd -> responseHandler.replyToButtons(getChatId(upd), upd.getCallbackQuery().getData());
        return Reply.of(action, Flag.CALLBACK_QUERY);
    }
}
