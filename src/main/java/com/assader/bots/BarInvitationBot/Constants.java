package com.assader.bots.BarInvitationBot;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class Constants {
    private static Logger logger = LogManager.getLogger(Constants.class);

    /**
     * Init constants
     */
    public static final String BOT_USERNAME = "com.assader.bots.BarInvitationBot.BarInvitationBot";
    public static String BOT_TOKEN;

    static {
        try {
            BOT_TOKEN = FileUtils.readFileToString(new File("secret"), Charset.forName("UTF-8"));
        } catch (IOException e) {
            logger.error("Could not read secret from file. Probably you forgot to include it?", e);
        }
    }

    public static final String PROXY_HOST = ;
    public static final Integer PROXY_PORT = ;
    public static final int CREATOR_ID = ;

    /**
     * Replace constants
     */
    public static final String PEOPLE_COUNT = "%people_count%";
    public static final String GROUP_COUNT = "%group_count%";

    /**
     * Messages
     */
    public static final String START_DESCRIPTION = "Начало использования бота";
    public static final String MESSAGE_NEW_USER_START = "Привет, с помощью меня можно позвать своих дорогих друзей прибухнуть в бар. Для начала нужно создать новую или вступить в существующую группу.";
    public static final String MESSAGE_START = "Привет, что будем делать сейчас?";
    public static final String MESSAGE_HAS_GROUP_ID = "Напиши ключ твоей группы, чтобы вступить в нее. /start чтобы вернуться назад.";
    public static final String MESSAGE_REMOVE_GROUP = "Выбери номер группы для удаления:";
    public static final String MESSAGE_REMOVE_GROUP_ADDITIONAL = "Отмена: возврат к меню.";
    public static final String REPLY_REGISTRATION = "Хорошо, вот идентификатор твоей группы! Отправь его своим друзьям, чтобы ты мог позвать иx в бар.";
    public static final String REPLY_REGISTRATION_ADDITIONAL = "Или отправь эту ссылку для более удобного присоединения друзей!";
    public static final String REPLY_START_BY_LINK = "Вы вступили в группу собутыльников по ссылке!\nЧеловек в группе: " + PEOPLE_COUNT + ".";
    public static final String REPLY_INVITE_LINK = "http://t.me/com.assader.bots.BarInvitationBot.BarInvitationBot?start=";
    public static final String REPLY_GROUP_LIMIT = "Вы достигли лимита по группам (" + GROUP_COUNT + "). Чтобы создать новую группу, выйдите из одной из существующих.";
    public static final String REPLY_GROUP_REMOVED = "Группа успешно удалена!";
    public static final String REPLY_ADDED_TO_GROUP = "Вы успешно добавились в группу!";
    public static final String ERROR_NO_GROUPS = "У вас нет групп!";
    public static final String ERROR_WRONG_GROUP = "Непрвильный формат идентификатора. Проверьте ввод.";
    public static final String ERROR_WRONG_GROUP_NUM = "Непрвильный номер группы. Проверьте ввод.";

    /**
     * Buttons
     */
    public static final String BTN_HAS_GROUP_ID = "Хочу вступить в группу";
    public static final String BTN_REMOVE_GROUP = "Удали меня из группы";
    public static final String BTN_CREATE_GROUP = "Создай новую группу";
    public static final String BTN_CREATE_POLL = "Го в бар!";
    public static final String BTN_CANCEL = "Отмена";

    /**
     * Map names
     */
    public static final String CHAT_STATES = "CHAT_STATES";
    public static final String USER_GROUPS = "USER_GROUPS";
    public static final String GROUP_USERS = "GROUP_USERS";

    /**
     * Limits
     */
    public static final Integer GROUP_LIMIT = 3;

    /**
     * States
     */
    public enum State {
        START, PROVIDE_GROUP_ID, REMOVING_GROUP
    }
}
