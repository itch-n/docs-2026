package com.study.systems.security;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RBACAuthorizerTest {

    @Test
    void testAdminCanDelete() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("alice", RBACAuthorizer.Role.ADMIN);

        assertTrue(rbac.hasPermission("alice", RBACAuthorizer.Permission.DELETE));
    }

    @Test
    void testAdminCanManageUsers() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("alice", RBACAuthorizer.Role.ADMIN);

        assertTrue(rbac.hasPermission("alice", RBACAuthorizer.Permission.MANAGE_USERS));
    }

    @Test
    void testEditorCanWrite() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("bob", RBACAuthorizer.Role.EDITOR);

        assertTrue(rbac.hasPermission("bob", RBACAuthorizer.Permission.WRITE));
    }

    @Test
    void testEditorCannotDelete() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("bob", RBACAuthorizer.Role.EDITOR);

        assertFalse(rbac.hasPermission("bob", RBACAuthorizer.Permission.DELETE));
    }

    @Test
    void testEditorCannotManageUsers() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("bob", RBACAuthorizer.Role.EDITOR);

        assertFalse(rbac.hasPermission("bob", RBACAuthorizer.Permission.MANAGE_USERS));
    }

    @Test
    void testViewerCanRead() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("charlie", RBACAuthorizer.Role.VIEWER);

        assertTrue(rbac.hasPermission("charlie", RBACAuthorizer.Permission.READ));
    }

    @Test
    void testViewerCannotDelete() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("charlie", RBACAuthorizer.Role.VIEWER);

        assertFalse(rbac.hasPermission("charlie", RBACAuthorizer.Permission.DELETE));
    }

    @Test
    void testViewerCannotWrite() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("charlie", RBACAuthorizer.Role.VIEWER);

        assertFalse(rbac.hasPermission("charlie", RBACAuthorizer.Permission.WRITE));
    }

    @Test
    void testUserWithNoRoleHasNoPermissions() {
        RBACAuthorizer rbac = new RBACAuthorizer();

        assertFalse(rbac.hasPermission("unknown", RBACAuthorizer.Permission.READ));
    }

    @Test
    void testGetAdminPermissionsContainsAll() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("alice", RBACAuthorizer.Role.ADMIN);

        Set<RBACAuthorizer.Permission> perms = rbac.getUserPermissions("alice");

        assertTrue(perms.contains(RBACAuthorizer.Permission.READ));
        assertTrue(perms.contains(RBACAuthorizer.Permission.WRITE));
        assertTrue(perms.contains(RBACAuthorizer.Permission.DELETE));
        assertTrue(perms.contains(RBACAuthorizer.Permission.MANAGE_USERS));
    }

    @Test
    void testGetEditorPermissionsContainsReadAndWrite() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("bob", RBACAuthorizer.Role.EDITOR);

        Set<RBACAuthorizer.Permission> perms = rbac.getUserPermissions("bob");

        assertTrue(perms.contains(RBACAuthorizer.Permission.READ));
        assertTrue(perms.contains(RBACAuthorizer.Permission.WRITE));
        assertFalse(perms.contains(RBACAuthorizer.Permission.DELETE));
    }

    @Test
    void testRevokeRoleRemovesPermissions() {
        RBACAuthorizer rbac = new RBACAuthorizer();
        rbac.assignRole("bob", RBACAuthorizer.Role.EDITOR);
        rbac.revokeRole("bob", RBACAuthorizer.Role.EDITOR);

        assertFalse(rbac.hasPermission("bob", RBACAuthorizer.Permission.WRITE));
    }
}
