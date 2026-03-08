package com.study.systems.apidesign;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RPCServerTest {

    @Test
    void testCreateUserReturnsNonNullId() {
        RPCServer service = new RPCServer();
        String userId = service.createUser("Bob", "bob@example.com");

        assertNotNull(userId);
    }

    @Test
    void testGetUserAfterCreate() {
        RPCServer service = new RPCServer();
        String userId = service.createUser("Bob", "bob@example.com");

        User user = service.getUser(userId);

        assertNotNull(user);
        assertEquals("Bob", user.name);
        assertEquals("bob@example.com", user.email);
    }

    @Test
    void testGetNonExistentUserReturnsNull() {
        RPCServer service = new RPCServer();

        User user = service.getUser("nonexistent");

        assertNull(user);
    }

    @Test
    void testUpdateExistingUserReturnsTrue() {
        RPCServer service = new RPCServer();
        String userId = service.createUser("Bob", "bob@example.com");

        boolean result = service.updateUser(userId, "Robert", "robert@example.com");

        assertTrue(result);
    }

    @Test
    void testUpdateNonExistentUserReturnsFalse() {
        RPCServer service = new RPCServer();

        boolean result = service.updateUser("nonexistent", "Bob", "bob@example.com");

        assertFalse(result);
    }

    @Test
    void testDeleteExistingUserReturnsTrue() {
        RPCServer service = new RPCServer();
        String userId = service.createUser("Bob", "bob@example.com");

        boolean result = service.deleteUser(userId);

        assertTrue(result);
    }

    @Test
    void testDeleteNonExistentUserReturnsFalse() {
        RPCServer service = new RPCServer();

        boolean result = service.deleteUser("nonexistent");

        assertFalse(result);
    }

    @Test
    void testGetUserPostsReturnsEmptyListForNewUser() {
        RPCServer service = new RPCServer();
        String userId = service.createUser("Bob", "bob@example.com");

        List<Post> posts = service.getUserPosts(userId);

        assertNotNull(posts);
        assertEquals(0, posts.size());
    }

    @Test
    void testGetUserPostsReturnsEmptyListForNonExistentUser() {
        RPCServer service = new RPCServer();

        List<Post> posts = service.getUserPosts("nonexistent");

        assertNotNull(posts);
        assertEquals(0, posts.size());
    }
}
