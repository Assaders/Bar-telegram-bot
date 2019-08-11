import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KeyboardFactory {
    public static ReplyKeyboard withNewUserStartButtons() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(Constants.BTN_HAS_GROUP_ID).setCallbackData(Constants.BTN_HAS_GROUP_ID));
        rowInline.add(new InlineKeyboardButton().setText(Constants.BTN_CREATE_GROUP).setCallbackData(Constants.BTN_CREATE_GROUP));
        rowsInline.add(rowInline);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public static ReplyKeyboard withAllStartButtons() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(Constants.BTN_HAS_GROUP_ID).setCallbackData(Constants.BTN_HAS_GROUP_ID));
        rowInline.add(new InlineKeyboardButton().setText(Constants.BTN_CREATE_GROUP).setCallbackData(Constants.BTN_CREATE_GROUP));
        rowsInline.add(rowInline);

        rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText(Constants.BTN_REMOVE_GROUP).setCallbackData(Constants.BTN_REMOVE_GROUP));
        rowInline.add(new InlineKeyboardButton().setText(Constants.BTN_CREATE_POLL).setCallbackData(Constants.BTN_CREATE_POLL));
        rowsInline.add(rowInline);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public static ReplyKeyboard withGroupDeleteButtons(int groupCount) {
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowsReply = new ArrayList<>();

        KeyboardRow rowGroups = new KeyboardRow();
        for (int i = 0; i < groupCount; ++i) {
            rowGroups.add(String.valueOf(i + 1));
        }
        rowsReply.add(rowGroups);

        KeyboardRow rowCancel = new KeyboardRow();
        rowCancel.add(Constants.BTN_CANCEL);
        rowsReply.add(rowCancel);

        replyKeyboard.setKeyboard(rowsReply);
        replyKeyboard.setOneTimeKeyboard(true);
        return replyKeyboard;
    }
}