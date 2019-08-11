import java.util.List;
import java.util.UUID;

public interface IDBManager {
    void addUserToGroup(Long userId, UUID groupId);
    void setUserState(Long userId, Constants.State state);
    Constants.State getUserState(Long userId);
    List<UUID> getUserGroups(Long userId);
    List<Long> getGroupUsers(UUID groupId);
    void removeUserFromGroup(Long userId, UUID groupId);

    boolean checkUserState(Long userId, Constants.State state);
    boolean checkUserInAnyGroup(Long userId);
    boolean checkUserGroupLimit(Long userId);
    boolean checkGroupExists(UUID groupId);
}
