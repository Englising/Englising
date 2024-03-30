package org.englising.com.englisingbe.auth.stomp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final Map<String, Set<UserInfo>> subscriptions = new ConcurrentHashMap<>();

    public void addSubscription(String topic, String sessionId, Long userId) {
        subscriptions.computeIfAbsent(topic, k -> ConcurrentHashMap.newKeySet()).add(new UserInfo(sessionId, userId));
    }

    public void removeSubscription(String topic, String sessionId, Long userId) {
        Set<UserInfo> sessionIds = subscriptions.getOrDefault(topic, ConcurrentHashMap.newKeySet());
        sessionIds.remove(new UserInfo(sessionId, userId));
        if (sessionIds.isEmpty()) {
            subscriptions.remove(topic);
        }
    }

    public Set<UserInfo> getSubscribers(String topic) {
        return subscriptions.getOrDefault(topic, Collections.emptySet());
    }

    public static class UserInfo{
        public UserInfo() {
        }

        public UserInfo(String sessionId, Long userId) {
            this.sessionId = sessionId;
            this.userId = userId;
        }

        public String sessionId;
        public Long userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserInfo userInfo = (UserInfo) o;
            return Objects.equals(sessionId, userInfo.sessionId) && Objects.equals(userId, userInfo.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId, userId);
        }
    }
}