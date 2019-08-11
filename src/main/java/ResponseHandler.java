import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.UUID;

public class ResponseHandler {
    private static Logger logger = LogManager.getLogger(ResponseHandler.class);

    private final MessageSender sender;
    private DBManager dbManager;

    public ResponseHandler(MessageSender sender, DBManager dbManager) {
        this.sender = sender;
        this.dbManager = dbManager;
    }

    public void replyToStart(Long chatId) {
        if (dbManager.checkUserInAnyGroup(chatId)) {
            printMessage(chatId, Constants.MESSAGE_START, KeyboardFactory.withAllStartButtons());
        } else {
            printMessage(chatId, Constants.MESSAGE_NEW_USER_START, KeyboardFactory.withNewUserStartButtons());
        }

        dbManager.setUserState(chatId, Constants.State.START);
    }

    public void replyToStart(Long chatId, String arg) {
        try {
            UUID groupId = UUID.fromString(arg);

            dbManager.addGroupToUser(chatId, groupId);
            dbManager.addUserToGroup(groupId, chatId);

            printMessage(chatId, Constants.REPLY_START_BY_LINK.replace(Constants.PEOPLE_COUNT, String.valueOf(dbManager.getGroupUsers(groupId).size())));
            replyToStart(chatId);
        } catch (IllegalArgumentException e) {
            logger.warn("Wrong parameter passed to /start dialog. Fall to standard reply", e);
            replyToStart(chatId);
        } catch (Exception e) {
            logger.error("Unknown exception", e);
        }
    }

    public void replyToButtons(Long chatId, String buttonId) {
        switch (buttonId) {
            case Constants.BTN_HAS_GROUP_ID:
                replyToHaveGroupId(chatId);
                break;
            case Constants.BTN_CREATE_GROUP:
                replyToCreateGroup(chatId);
                break;
            case Constants.BTN_REMOVE_GROUP:
                replyToRemoveGroup(chatId);
                break;
            case Constants.BTN_CREATE_POLL:
                break;
            default:
                logger.error("User {} pressed unknown button {}", chatId, buttonId);
        }
    }

    private void replyToRemoveGroup(Long chatId) {
        List<UUID> userGroups = dbManager.getUserGroups(chatId);

        if (userGroups == null || userGroups.size() == 0) {
            printMessage(chatId, Constants.ERROR_NO_GROUPS);
            logger.warn("User {} tried to remove group when he don't have any", chatId);
            replyToStart(chatId);
            return;
        }

        List<UUID> groups = dbManager.getUserGroups(chatId);
        StringBuilder message = new StringBuilder(Constants.MESSAGE_REMOVE_GROUP);
        for (int i = 0; i < groups.size(); ++i) {
            message.append("\n").append(i + 1).append(": ").append(groups.get(i).toString());
        }
        message.append("\n").append(Constants.MESSAGE_REMOVE_GROUP_ADDITIONAL);
        printMessage(chatId, message.toString(), KeyboardFactory.withGroupDeleteButtons(groups.size()));
        dbManager.setUserState(chatId, Constants.State.REMOVING_GROUP);
    }

    private void replyToHaveGroupId(Long chatId) {
        if (dbManager.checkUserState(chatId, Constants.State.START)) {
            if (dbManager.checkUserGroupLimit(chatId)) {
                printMessage(chatId, Constants.REPLY_GROUP_LIMIT.replace(Constants.GROUP_COUNT, String.valueOf(dbManager.getUserGroups(chatId).size())));
                replyToStart(chatId);
                return;
            }

            printMessage(chatId, Constants.MESSAGE_HAS_GROUP_ID);
            dbManager.setUserState(chatId, Constants.State.PROVIDE_GROUP_ID);
        } else {
            logger.warn("User {} accessed registration from state {}", chatId, dbManager.getUserState(chatId));
        }
    }

    private void replyToCreateGroup(Long chatId) {
        if (dbManager.checkUserState(chatId, Constants.State.START)) {
            if (dbManager.checkUserGroupLimit(chatId)) {
                printMessage(chatId, Constants.REPLY_GROUP_LIMIT.replace(Constants.GROUP_COUNT, String.valueOf(dbManager.getUserGroups(chatId).size())));
            } else {
                printMessage(chatId, Constants.REPLY_REGISTRATION);

                UUID newGroupId = UUID.randomUUID();
                printMessage(chatId, newGroupId.toString());
                printMessage(chatId, Constants.REPLY_REGISTRATION_ADDITIONAL);
                printMessage(chatId, Constants.REPLY_INVITE_LINK + newGroupId.toString());

                dbManager.addGroupToUser(chatId, newGroupId);
                dbManager.addUserToGroup(newGroupId, chatId);
            }

            replyToStart(chatId);
        } else {
            logger.warn("User {} accessed registration from state {}", chatId, dbManager.getUserState(chatId));
        }
    }


    public void replyToNonCommand(Long chatId, String firstArg) {
        switch (dbManager.getUserState(chatId)) {
            case REMOVING_GROUP:
                if (firstArg.equals(Constants.BTN_CANCEL)) {
                    replyToStart(chatId);
                    return;
                }
                try {
                    Integer groupNum = Integer.valueOf(firstArg);
                    List<UUID> groups = dbManager.getUserGroups(chatId);

                    if (groups == null || groupNum < 1 || groupNum > groups.size()) {
                        logger.warn("User {} inputs wrong group number: {}", chatId, firstArg);
                        printMessage(chatId, Constants.ERROR_WRONG_GROUP_NUM);
                        return;
                    }

                    dbManager.removeUserFromGroup(chatId, groups.get(groupNum - 1));
                    printMessage(chatId, Constants.REPLY_GROUP_REMOVED);
                    replyToStart(chatId);
                } catch (NumberFormatException e) {
                    logger.warn("User {} inputs not group number", chatId, e);
                    printMessage(chatId, Constants.ERROR_WRONG_GROUP_NUM);
                }
                break;
            case PROVIDE_GROUP_ID:
                try {
                    UUID groupId = UUID.fromString(firstArg);

                    if (dbManager.checkGroupExists(groupId)) {
                        dbManager.addGroupToUser(chatId, groupId);
                        dbManager.addUserToGroup(groupId, chatId);
                        printMessage(chatId, Constants.REPLY_ADDED_TO_GROUP);
                        replyToStart(chatId);
                    } else {
                        logger.warn("User {} inputs non existent group UUID {}", chatId, firstArg);
                        printMessage(chatId, Constants.ERROR_WRONG_GROUP);
                        printMessage(chatId, Constants.MESSAGE_HAS_GROUP_ID);
                    }
                } catch (IllegalArgumentException e) {
                    logger.warn("User {} inputs wrong UUID {}", chatId, firstArg);
                    printMessage(chatId, Constants.ERROR_WRONG_GROUP);
                    printMessage(chatId, Constants.MESSAGE_HAS_GROUP_ID);
                }
                break;
        }
    }

    void printMessage(Long chatId, String message) {
        try {
            sender.execute(new SendMessage()
                    .setText(message)
                    .setChatId(chatId)
            );
        } catch (TelegramApiException e) {
            logger.error("Telegram api exception", e);
        }
    }

    void printMessage(Long chatId, String message, ReplyKeyboard keyboard) {
        try {
            sender.execute(new SendMessage()
                    .setText(message)
                    .setChatId(chatId)
                    .setReplyMarkup(keyboard)
            );
        } catch (TelegramApiException e) {
            logger.error("Telegram api exception", e);
        }
    }
}