import org.telegram.abilitybots.api.db.DBContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DBManager implements IDBManager {
    private final Map<Long, Constants.State> chatStates;
    private final Map<UUID, List<Long>> groupUsers;
    private final Map<Long, List<UUID>> userGroups;

    public DBManager(DBContext db) {
        chatStates = db.getMap(Constants.CHAT_STATES);
        userGroups = db.getMap(Constants.USER_GROUPS);
        groupUsers = db.getMap(Constants.GROUP_USERS);
    }

    public void addUserToGroup(Long userId, UUID groupId) {
        List<UUID> groups = userGroups.get(userId);
        if (groups == null) {
            groups = new ArrayList<>();
        }
        groups.add(groupId);

        userGroups.put(userId, groups);

        List<Long> users = groupUsers.get(groupId);
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(userId);

        groupUsers.put(groupId, users);
    }

    public void setUserState(Long userId, Constants.State state) {
        chatStates.put(userId, state);
    }

    public Constants.State getUserState(Long userId) {
        return chatStates.get(userId);
    }

    public boolean checkUserState(Long userId, Constants.State state) {
        Constants.State userState = getUserState(userId);
        return userState != null && userState.equals(state);
    }

    public List<UUID> getUserGroups(Long userId) {
        return userGroups.get(userId);
    }

    public List<Long> getGroupUsers(UUID groupId) {
        return groupUsers.get(groupId);
    }

    public boolean checkUserInAnyGroup(Long userId) {
        List<UUID> userGroups = getUserGroups(userId);
        return userGroups != null && !userGroups.isEmpty();
    }

    public boolean checkUserGroupLimit(Long userId) {
        List<UUID> userGroups = getUserGroups(userId);
        return userGroups != null && userGroups.size() >= Constants.GROUP_LIMIT;
    }

    public void removeUserFromGroup(Long userId, UUID groupId) {
        List<Long> users = getGroupUsers(groupId);
        if (users != null) {
            users.remove(userId);
            if (users.isEmpty()) {
                groupUsers.remove(groupId);
            } else {
                groupUsers.put(groupId, users);
            }
        }

        List<UUID> groups = getUserGroups(userId);
        if (groups != null) {
            groups.remove(groupId);
            if (groups.isEmpty()) {
                userGroups.remove(userId);
            } else {
                userGroups.put(userId, groups);
            }
        }
    }

    public boolean checkGroupExists(UUID groupId) {
        return groupUsers.get(groupId) != null;
    }
}
