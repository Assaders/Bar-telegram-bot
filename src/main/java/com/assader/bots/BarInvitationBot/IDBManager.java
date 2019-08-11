package com.assader.bots.BarInvitationBot;

import java.util.List;
import java.util.UUID;

import static com.assader.bots.BarInvitationBot.Constants.*;

public interface IDBManager {
    void addUserToGroup(Long userId, UUID groupId);
    void setUserState(Long userId, State state);
    State getUserState(Long userId);
    List<UUID> getUserGroups(Long userId);
    List<Long> getGroupUsers(UUID groupId);
    void removeUserFromGroup(Long userId, UUID groupId);

    boolean checkUserState(Long userId, State state);
    boolean checkUserInAnyGroup(Long userId);
    boolean checkUserGroupLimit(Long userId);
    boolean checkGroupExists(UUID groupId);
}
