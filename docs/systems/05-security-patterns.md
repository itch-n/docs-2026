# 05. Security Patterns

> Authentication, authorization, and securing distributed systems

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing security patterns, explain them simply.

**Prompts to guide you:**

1. **What is authentication in one sentence?**
    - Your answer: _[Fill in after implementation]_

2. **What is authorization in one sentence?**
    - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for authentication:**
    - Example: "Authentication is like showing your ID at the door..."
    - Your analogy: _[Fill in]_

4. **Real-world analogy for authorization:**
    - Example: "Authorization is like having a key to certain rooms..."
    - Your analogy: _[Fill in]_

5. **What is JWT in one sentence?**
    - Your answer: _[Fill in after implementation]_

6. **When should you use JWT vs sessions?**
    - Your answer: _[Fill in after practice]_

---

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your security intuition without looking at code. Answer these, then verify after implementation.

### Security Concept Predictions

1. **Authentication vs Authorization:**
    - Authentication is: _[Your definition]_
    - Authorization is: _[Your definition]_
    - Example scenario: _[Think of real-world example]_

2. **JWT token structure:**
    - Three parts: _[What are they?]_
    - Why is signature needed?: _[Your guess]_
    - Can client modify payload?: _[Yes/No - Why?]_

3. **RBAC complexity:**
    - Checking permission for user with 3 roles: O(?)
    - Better than checking each permission individually?: _[Yes/No - Why?]_

### Scenario Predictions

**Scenario 1:** A user tries to access a protected resource with JWT token

- **What gets validated first?** _[Signature? Expiration? Claims?]_
- **If signature invalid, what does it mean?** _[Token tampered? Expired? Wrong secret?]_
- **What happens if token expired but signature valid?** _[Allow? Deny? Refresh?]_

**Scenario 2:** Implementing RBAC for a blog platform

- **Roles needed:** _[List them]_
- **VIEWER can:** _[What permissions?]_
- **EDITOR can:** _[What permissions beyond VIEWER?]_
- **ADMIN can:** _[Everything or specific permissions?]_

**Scenario 3:** API key gets leaked on GitHub

- **Immediate action:** _[What to do first?]_
- **Why rotation matters:** _[Explain]_
- **How to prevent:** _[Fill in]_

### Security Trade-off Quiz

**Question:** When would Session-based auth be BETTER than JWT?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question:** What's the MAIN advantage of JWT over sessions?

- [ ] More secure
- [ ] Stateless (no server-side storage)
- [ ] Easier to implement
- [ ] Better performance

Verify after implementation: _[Which one(s)?]_

**Question:** Why use HMAC for JWT signature instead of just Base64 encoding?

- Your answer: _[Fill in]_
- Verified: _[Fill in after implementation]_

---

## Before/After: Why Security Patterns Matter

**Your task:** Compare insecure vs secure approaches to understand the security impact.

### Example 1: Authentication - Insecure vs Secure

**Problem:** Implement user authentication for an API.

#### Approach 1: Insecure - Username/Password in Every Request

```java
// INSECURE: Sending credentials with every request
public class InsecureAuth {

    public boolean authenticateRequest(String username, String password) {
        // Problem: Credentials sent with EVERY API call
        // - Exposed in logs, network traffic
        // - No expiration mechanism
        // - Can't revoke access without changing password
        return checkDatabase(username, password);
    }

    // Client code
    public void makeAPICall() {
        // INSECURE: Username and password in every request
        String response = httpClient.get("/api/data",
            "username=john",
            "password=secret123");  // Exposed!
    }
}
```

**Security Issues:**

- Credentials exposed in every request (logs, network, proxy)
- No way to revoke access without password change
- Password transmitted repeatedly (more attack surface)
- Can't implement session timeout
- Difficult to audit (which requests from which session?)

#### Approach 2: Secure - JWT Token-Based Auth

```java
// SECURE: Token-based authentication
public class SecureJWTAuth {

    private final String secret = System.getenv("JWT_SECRET");  // From environment
    private final long expirationMs = 3600000;  // 1 hour

    // Step 1: Login once to get token
    public String login(String username, String password) {
        if (!validateCredentials(username, password)) {
            return null;
        }

        // Generate short-lived token
        return generateJWT(username, expirationMs);
    }

    // Step 2: Use token for subsequent requests
    public boolean authenticateRequest(String token) {
        try {
            String userId = validateJWT(token);
            // Check expiration
            if (isExpired(token)) {
                return false;  // Must re-login
            }
            return userId != null;
        } catch (SecurityException e) {
            return false;  // Invalid signature
        }
    }

    // Client code
    public void makeAPICall() {
        // SECURE: Token in Authorization header
        String token = loginOnce("john", "secret123");
        String response = httpClient.get("/api/data",
            headers: {"Authorization": "Bearer " + token});
        // Password only sent once during login!
    }
}
```

**Security Improvements:**

- Credentials only sent once during login
- Token expires automatically (limited exposure window)
- Can revoke tokens without password change
- Signature prevents tampering
- Stateless (scales horizontally)
- Audit-friendly (track token usage)

#### Performance & Security Comparison

| Aspect | Insecure (Creds Every Request) | Secure (JWT) | Improvement |
|--------|-------------------------------|--------------|-------------|
| Credential exposure | Every request | Login only | 100x less |
| Revocation | Change password | Revoke token | Immediate |
| Expiration | None | Built-in | Auto-logout |
| Tampering protection | None | HMAC signature | Detectable |
| Scalability | Database hit every request | No database lookup | 10-100x faster |
| Audit trail | Difficult | Token ID trackable | Complete |

**Your calculation:** If a user makes 1,000 API calls per day:
- Insecure approach exposes credentials _____ times
- Secure approach exposes credentials _____ time(s)
- Security improvement: _____ x safer

---

### Example 2: Authorization - No Checks vs RBAC

**Problem:** Control who can delete blog posts.

#### Approach 1: No Authorization Checks

```java
// INSECURE: No authorization, anyone can delete
public class NoAuthzBlog {

    public boolean deletePost(String postId, String userId) {
        // BUG: No check if user has permission!
        // Any authenticated user can delete any post
        database.delete("posts", postId);
        return true;
    }

    // Security hole: Attacker can delete all posts
    public void attackExample() {
        // Even a VIEWER role can do this!
        deletePost("important-post", "attacker-user-id");
    }
}
```

**Security Issues:**

- Any authenticated user can perform any action
- No distinction between roles (viewer, editor, admin)
- Privilege escalation: viewer acts as admin
- No audit trail of who did what
- Cannot implement least privilege principle

#### Approach 2: RBAC Authorization

```java
// SECURE: Role-Based Access Control
public class RBACBlog {

    private final RBACAuthorizer rbac;

    public boolean deletePost(String postId, String userId) {
        // Step 1: Check authorization BEFORE action
        if (!rbac.hasPermission(userId, Permission.DELETE)) {
            auditLog.warn("Unauthorized delete attempt", userId, postId);
            throw new SecurityException("Insufficient permissions");
        }

        // Step 2: Additional check - can only delete own posts (unless admin)
        Post post = database.getPost(postId);
        if (!post.authorId.equals(userId) &&
            !rbac.hasRole(userId, Role.ADMIN)) {
            throw new SecurityException("Can only delete own posts");
        }

        // Step 3: Perform action with audit logging
        database.delete("posts", postId);
        auditLog.info("Post deleted", userId, postId);
        return true;
    }

    // Secure example: Permission properly checked
    public void secureExample() {
        try {
            deletePost("important-post", "viewer-user-id");
        } catch (SecurityException e) {
            // Blocked! Viewer doesn't have DELETE permission
            System.out.println("Access denied: " + e.getMessage());
        }
    }
}
```

**Security Improvements:**

- Explicit permission check before every sensitive action
- Role-based hierarchy (viewer < editor < admin)
- Audit logging for security events
- Fail-secure (deny by default)
- Least privilege principle enforced

#### Security Comparison

| Attack Vector | No Authorization | With RBAC | Prevented? |
|---------------|------------------|-----------|------------|
| Viewer deletes posts | Succeeds | Blocked | ✓ |
| Editor deletes other's posts | Succeeds | Blocked | ✓ |
| User promotes self to admin | Succeeds | Blocked | ✓ |
| Audit trail of actions | None | Complete | ✓ |
| Privilege escalation | Easy | Impossible | ✓ |

**After implementing, explain in your own words:**

- _[Why is "deny by default" important?]_
- _[What happens if you forget one authorization check?]_
- _[How does RBAC prevent privilege escalation?]_

---

### Example 3: Secrets Management - Hardcoded vs Encrypted

**Problem:** Store database password for application use.

#### Approach 1: Hardcoded Credentials

```java
// INSECURE: Hardcoded credentials in source code
public class HardcodedSecrets {

    // BUG: Secret in source code!
    private static final String DB_PASSWORD = "super-secret-pwd-123";

    public Connection connectToDatabase() {
        // Problems:
        // 1. Password in version control (git history)
        // 2. Visible to anyone with code access
        // 3. Can't rotate without redeploying
        // 4. Same password in dev, staging, prod
        return DriverManager.getConnection(
            "jdbc:postgresql://db.example.com/mydb",
            "dbuser",
            DB_PASSWORD  // EXPOSED!
        );
    }
}
```

**Security Issues:**

- Secret in git history (can't remove)
- Visible in source code reviews
- Leaked in compiled binaries/JAR files
- Can't rotate without code changes + redeployment
- Same secret across all environments
- Exposed in logs, stack traces, debugging

#### Approach 2: Encrypted Secrets Management

```java
// SECURE: Encrypted secrets with rotation
public class SecureSecretsManagement {

    private final SecretsManager secretsManager;

    public Connection connectToDatabase() {
        // Step 1: Retrieve secret from encrypted store
        // - Master key stored in environment/HSM
        // - Secrets encrypted at rest
        // - Access controlled per service
        String dbPassword = secretsManager.getSecret(
            "db_password",
            getCurrentServiceId()
        );

        // Step 2: Use secret (never log it!)
        return DriverManager.getConnection(
            "jdbc:postgresql://db.example.com/mydb",
            "dbuser",
            dbPassword  // Retrieved securely
        );
        // Step 3: dbPassword cleared from memory after use
    }

    // Rotation without downtime
    public void rotatePassword() {
        // 1. Generate new password
        String newPassword = generateSecurePassword();

        // 2. Update database with new password
        database.updateUserPassword("dbuser", newPassword);

        // 3. Store new version in secrets manager
        secretsManager.rotateSecret("db_password", newPassword);

        // 4. Old version still valid for grace period
        // 5. After grace period, old version deleted
    }
}
```

**Security Improvements:**

- Secrets never in source code or version control
- Encrypted at rest with master key
- Access control (only authorized services can read)
- Audit logging (who accessed what secret when)
- Rotation without code changes or redeployment
- Different secrets per environment (dev/staging/prod)
- Automatic expiration and rotation

#### Security Impact Analysis

| Risk | Hardcoded | Secrets Manager | Mitigation |
|------|-----------|-----------------|------------|
| Git leak | Exposed forever | Not in git | ✓ |
| Code review leak | Visible | Not visible | ✓ |
| Rotation cost | Redeploy | API call | ✓ |
| Audit capability | None | Full logging | ✓ |
| Blast radius | All environments | Isolated | ✓ |
| Post-breach response | Manual everywhere | Rotate instantly | ✓ |

**Real-world impact:** In 2019, 50,000+ hardcoded secrets leaked on GitHub led to major breaches.

**Your reflection after implementation:**

- _[How would you rotate a leaked hardcoded password?]_
- _[What's the blast radius if secrets manager is breached vs. hardcoded?]_
- _[Why is "secrets in environment variables" better than hardcoded but still not ideal?]_

---

## Core Implementation

### Pattern 1: JWT-Based Authentication

**Concept:** Stateless authentication using JSON Web Tokens.

**Use case:** Microservices, API authentication, mobile apps.

```java
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * JWT Authentication: Stateless token-based auth
 *
 * Token structure: header.payload.signature
 * - Header: algorithm and token type
 * - Payload: claims (user data, expiration)
 * - Signature: HMAC of header+payload with secret
 */
public class JWTAuthenticator {

    private final String secret;
    private final long expirationMs;

    public JWTAuthenticator(String secret, long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    /**
     * Generate JWT token for user
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement JWT generation
     * 1. Create header: {"alg": "HS256", "typ": "JWT"}
     * 2. Create payload: {"sub": userId, "exp": expiration, "iat": issuedAt}
     * 3. Base64 encode header and payload
     * 4. Sign with HMAC-SHA256
     * 5. Return header.payload.signature
     */
    public String generateToken(String userId) {
        // TODO: Create header
        //   String header = """
        //     {"alg":"HS256","typ":"JWT"}
        //     """;
        //   String headerBase64 = base64UrlEncode(header);

        // TODO: Create payload with expiration
        //   long now = System.currentTimeMillis();
        //   long exp = now + expirationMs;
        //   String payload = String.format("""
        //     {"sub":"%s","exp":%d,"iat":%d}
        //     """, userId, exp / 1000, now / 1000);
        //   String payloadBase64 = base64UrlEncode(payload);

        // TODO: Create signature
        //   String toSign = headerBase64 + "." + payloadBase64;
        //   String signature = hmacSha256(toSign, secret);

        // TODO: Return JWT
        //   return toSign + "." + signature;

        return null; // Replace
    }

    /**
     * Validate and extract user from JWT
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement JWT validation
     * 1. Split token into parts
     * 2. Verify signature
     * 3. Check expiration
     * 4. Extract and return user ID
     */
    public String validateToken(String token) {
        // TODO: Split token
        //   String[] parts = token.split("\\.");
        //   if (parts.length != 3) return null;

        // TODO: Verify signature
        //   String toVerify = parts[0] + "." + parts[1];
        //   String expectedSig = hmacSha256(toVerify, secret);
        //   if (!expectedSig.equals(parts[2])) return null;

        // TODO: Decode and check expiration
        //   String payload = base64UrlDecode(parts[1]);
        //   // Parse JSON, check exp field

        // TODO: Return userId from payload
        return null; // Replace
    }

    /**
     * Helper: Base64 URL-safe encoding
     *
     * TODO: Implement base64 URL encoding
     */
    private String base64UrlEncode(String input) {
        // TODO: Encode and make URL-safe
        //   Remove padding (=), replace + with -, / with _
        return null; // Replace
    }

    /**
     * Helper: HMAC-SHA256 signature
     *
     * TODO: Implement HMAC signing
     */
    private String hmacSha256(String data, String key) {
        // TODO: Use Mac with HmacSHA256
        //   Mac mac = Mac.getInstance("HmacSHA256");
        //   SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        //   mac.init(secretKey);
        //   byte[] hash = mac.doFinal(data.getBytes());
        //   return base64UrlEncode(new String(hash));
        return null; // Replace
    }
}
```

**Runnable Client Code:**

```java
public class JWTClient {

    public static void main(String[] args) {
        System.out.println("=== JWT Authentication ===\n");

        String secret = "your-256-bit-secret";
        long expirationMs = 3600000; // 1 hour

        JWTAuthenticator auth = new JWTAuthenticator(secret, expirationMs);

        // Test 1: Generate token
        System.out.println("--- Test 1: Generate Token ---");
        String token = auth.generateToken("user123");
        System.out.println("Generated token: " + token);

        // Test 2: Validate token
        System.out.println("\n--- Test 2: Validate Token ---");
        String userId = auth.validateToken(token);
        System.out.println("Extracted user: " + userId);

        // Test 3: Invalid token
        System.out.println("\n--- Test 3: Invalid Token ---");
        String invalidToken = "invalid.token.here";
        String result = auth.validateToken(invalidToken);
        System.out.println("Validation result: " + result);
    }
}
```

---

### Pattern 2: Role-Based Access Control (RBAC)

**Concept:** Authorization based on user roles and permissions.

**Use case:** Multi-tenant systems, enterprise applications, admin panels.

```java
import java.util.*;

/**
 * RBAC: Role-Based Access Control
 *
 * Concepts:
 * - Users have roles (admin, editor, viewer)
 * - Roles have permissions (read, write, delete)
 * - Check permission before allowing action
 */
public class RBACAuthorizer {

    // Role definitions
    enum Role {
        ADMIN, EDITOR, VIEWER
    }

    enum Permission {
        READ, WRITE, DELETE, MANAGE_USERS
    }

    // Role -> Permissions mapping
    private final Map<Role, Set<Permission>> rolePermissions;
    // User -> Roles mapping
    private final Map<String, Set<Role>> userRoles;

    public RBACAuthorizer() {
        this.rolePermissions = new HashMap<>();
        this.userRoles = new HashMap<>();
        initializeRolePermissions();
    }

    /**
     * Initialize default role permissions
     *
     * TODO: Set up role hierarchies
     * - ADMIN: all permissions
     * - EDITOR: read, write
     * - VIEWER: read only
     */
    private void initializeRolePermissions() {
        // TODO: Define ADMIN permissions
        //   rolePermissions.put(Role.ADMIN, EnumSet.allOf(Permission.class));

        // TODO: Define EDITOR permissions
        //   rolePermissions.put(Role.EDITOR,
        //     EnumSet.of(Permission.READ, Permission.WRITE));

        // TODO: Define VIEWER permissions
        //   rolePermissions.put(Role.VIEWER,
        //     EnumSet.of(Permission.READ));
    }

    /**
     * Assign role to user
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement role assignment
     */
    public void assignRole(String userId, Role role) {
        // TODO: Add role to user's role set
        //   userRoles.computeIfAbsent(userId, k -> new HashSet<>())
        //            .add(role);
    }

    /**
     * Check if user has permission
     * Time: O(R) where R = number of roles, Space: O(1)
     *
     * TODO: Implement permission check
     * 1. Get user's roles
     * 2. For each role, check if it has the permission
     * 3. Return true if any role grants permission
     */
    public boolean hasPermission(String userId, Permission permission) {
        // TODO: Get user roles
        //   Set<Role> roles = userRoles.get(userId);
        //   if (roles == null) return false;

        // TODO: Check each role's permissions
        //   for (Role role : roles) {
        //     Set<Permission> perms = rolePermissions.get(role);
        //     if (perms != null && perms.contains(permission)) {
        //       return true;
        //     }
        //   }

        return false; // Replace
    }

    /**
     * Get all permissions for user
     * Time: O(R*P), Space: O(P)
     *
     * TODO: Implement permission aggregation
     */
    public Set<Permission> getUserPermissions(String userId) {
        Set<Permission> allPermissions = new HashSet<>();

        // TODO: Aggregate permissions from all roles
        //   Set<Role> roles = userRoles.get(userId);
        //   if (roles != null) {
        //     for (Role role : roles) {
        //       Set<Permission> perms = rolePermissions.get(role);
        //       if (perms != null) allPermissions.addAll(perms);
        //     }
        //   }

        return allPermissions; // Replace
    }

    /**
     * Remove role from user
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement role revocation
     */
    public void revokeRole(String userId, Role role) {
        // TODO: Remove role from user
        //   Set<Role> roles = userRoles.get(userId);
        //   if (roles != null) roles.remove(role);
    }
}
```

**Runnable Client Code:**

```java
import static RBACAuthorizer.*;

public class RBACClient {

    public static void main(String[] args) {
        System.out.println("=== RBAC Authorization ===\n");

        RBACAuthorizer rbac = new RBACAuthorizer();

        // Test 1: Assign roles
        System.out.println("--- Test 1: Role Assignment ---");
        rbac.assignRole("alice", Role.ADMIN);
        rbac.assignRole("bob", Role.EDITOR);
        rbac.assignRole("charlie", Role.VIEWER);
        System.out.println("Roles assigned");

        // Test 2: Check permissions
        System.out.println("\n--- Test 2: Permission Checks ---");
        System.out.println("Alice (ADMIN) can DELETE: " +
            rbac.hasPermission("alice", Permission.DELETE));
        System.out.println("Bob (EDITOR) can WRITE: " +
            rbac.hasPermission("bob", Permission.WRITE));
        System.out.println("Charlie (VIEWER) can DELETE: " +
            rbac.hasPermission("charlie", Permission.DELETE));

        // Test 3: Get all permissions
        System.out.println("\n--- Test 3: All Permissions ---");
        System.out.println("Alice permissions: " + rbac.getUserPermissions("alice"));
        System.out.println("Bob permissions: " + rbac.getUserPermissions("bob"));
        System.out.println("Charlie permissions: " + rbac.getUserPermissions("charlie"));
    }
}
```

---

### Pattern 3: API Key Authentication

**Concept:** Long-lived tokens for service-to-service authentication.

**Use case:** REST APIs, webhooks, third-party integrations.

```java
import java.util.*;
import java.security.SecureRandom;

/**
 * API Key Authentication
 *
 * Key properties:
 * - Long-lived credentials
 * - Scoped to specific resources
 * - Can be rate-limited per key
 * - Easy to rotate and revoke
 */
public class APIKeyAuth {

    static class APIKey {
        String key;
        String userId;
        Set<String> scopes;
        long createdAt;
        long lastUsedAt;
        int usageCount;

        APIKey(String key, String userId, Set<String> scopes) {
            this.key = key;
            this.userId = userId;
            this.scopes = scopes;
            this.createdAt = System.currentTimeMillis();
            this.lastUsedAt = createdAt;
            this.usageCount = 0;
        }
    }

    private final Map<String, APIKey> keys;
    private final SecureRandom random;

    public APIKeyAuth() {
        this.keys = new HashMap<>();
        this.random = new SecureRandom();
    }

    /**
     * Generate new API key
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement key generation
     * 1. Generate random key (32-byte hex)
     * 2. Store with user ID and scopes
     * 3. Return key
     */
    public String generateKey(String userId, Set<String> scopes) {
        // TODO: Generate secure random key
        //   byte[] bytes = new byte[32];
        //   random.nextBytes(bytes);
        //   String key = bytesToHex(bytes);

        // TODO: Store key
        //   APIKey apiKey = new APIKey(key, userId, scopes);
        //   keys.put(key, apiKey);

        return null; // Replace
    }

    /**
     * Validate API key and check scope
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement key validation
     * 1. Lookup key
     * 2. Check if scope is allowed
     * 3. Update usage metrics
     * 4. Return user ID or null
     */
    public String validateKey(String key, String requiredScope) {
        // TODO: Lookup key
        //   APIKey apiKey = keys.get(key);
        //   if (apiKey == null) return null;

        // TODO: Check scope
        //   if (!apiKey.scopes.contains(requiredScope)) return null;

        // TODO: Update usage
        //   apiKey.lastUsedAt = System.currentTimeMillis();
        //   apiKey.usageCount++;

        return null; // Replace
    }

    /**
     * Revoke API key
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement key revocation
     */
    public boolean revokeKey(String key) {
        // TODO: Remove key from storage
        //   return keys.remove(key) != null;
        return false; // Replace
    }

    /**
     * Get usage statistics for key
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement usage tracking
     */
    public Map<String, Object> getKeyStats(String key) {
        Map<String, Object> stats = new HashMap<>();

        // TODO: Return key statistics
        //   APIKey apiKey = keys.get(key);
        //   if (apiKey != null) {
        //     stats.put("usageCount", apiKey.usageCount);
        //     stats.put("lastUsedAt", apiKey.lastUsedAt);
        //     stats.put("createdAt", apiKey.createdAt);
        //   }

        return stats; // Replace
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class APIKeyClient {

    public static void main(String[] args) {
        System.out.println("=== API Key Authentication ===\n");

        APIKeyAuth apiKeyAuth = new APIKeyAuth();

        // Test 1: Generate keys
        System.out.println("--- Test 1: Generate API Keys ---");
        Set<String> scopes1 = new HashSet<>(Arrays.asList("read", "write"));
        String key1 = apiKeyAuth.generateKey("service1", scopes1);
        System.out.println("Generated key for service1: " + key1);

        Set<String> scopes2 = new HashSet<>(Arrays.asList("read"));
        String key2 = apiKeyAuth.generateKey("service2", scopes2);
        System.out.println("Generated key for service2: " + key2);

        // Test 2: Validate keys
        System.out.println("\n--- Test 2: Validate Keys ---");
        String userId1 = apiKeyAuth.validateKey(key1, "write");
        System.out.println("Key1 with 'write' scope: " + userId1);

        String userId2 = apiKeyAuth.validateKey(key2, "write");
        System.out.println("Key2 with 'write' scope: " + userId2);

        // Test 3: Revoke key
        System.out.println("\n--- Test 3: Revoke Key ---");
        boolean revoked = apiKeyAuth.revokeKey(key1);
        System.out.println("Key1 revoked: " + revoked);
        String userId3 = apiKeyAuth.validateKey(key1, "read");
        System.out.println("Key1 after revocation: " + userId3);
    }
}
```

---

### Pattern 4: Secrets Management

**Concept:** Secure storage and rotation of sensitive credentials.

**Use case:** Database passwords, API keys, encryption keys.

```java
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * Secrets Manager
 *
 * Features:
 * - Encrypted storage
 * - Versioning for rotation
 * - Access control per secret
 * - Audit logging
 */
public class SecretsManager {

    static class Secret {
        String name;
        byte[] encryptedValue;
        int version;
        long createdAt;
        Set<String> authorizedUsers;

        Secret(String name, byte[] encryptedValue, int version, Set<String> authorizedUsers) {
            this.name = name;
            this.encryptedValue = encryptedValue;
            this.version = version;
            this.createdAt = System.currentTimeMillis();
            this.authorizedUsers = authorizedUsers;
        }
    }

    private final Map<String, List<Secret>> secrets; // name -> versions
    private final SecretKey masterKey;

    /**
     * Initialize secrets manager with master encryption key
     *
     * TODO: Set up encryption
     */
    public SecretsManager(SecretKey masterKey) {
        this.secrets = new HashMap<>();
        this.masterKey = masterKey;
    }

    /**
     * Store secret with encryption
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement secret storage
     * 1. Encrypt value with master key
     * 2. Store with version number
     * 3. Set authorized users
     */
    public void storeSecret(String name, String value, Set<String> authorizedUsers) {
        // TODO: Encrypt secret value
        //   byte[] encrypted = encrypt(value.getBytes(), masterKey);

        // TODO: Create new version
        //   List<Secret> versions = secrets.computeIfAbsent(name, k -> new ArrayList<>());
        //   int version = versions.size() + 1;
        //   Secret secret = new Secret(name, encrypted, version, authorizedUsers);
        //   versions.add(secret);
    }

    /**
     * Retrieve secret with authorization check
     * Time: O(V) where V = versions, Space: O(1)
     *
     * TODO: Implement secret retrieval
     * 1. Check authorization
     * 2. Get latest version
     * 3. Decrypt and return
     */
    public String getSecret(String name, String userId) {
        // TODO: Get latest version
        //   List<Secret> versions = secrets.get(name);
        //   if (versions == null || versions.isEmpty()) return null;
        //   Secret latest = versions.get(versions.size() - 1);

        // TODO: Check authorization
        //   if (!latest.authorizedUsers.contains(userId)) {
        //     throw new SecurityException("Unauthorized access");
        //   }

        // TODO: Decrypt and return
        //   byte[] decrypted = decrypt(latest.encryptedValue, masterKey);
        //   return new String(decrypted);

        return null; // Replace
    }

    /**
     * Rotate secret (create new version)
     * Time: O(1), Space: O(1)
     *
     * TODO: Implement secret rotation
     */
    public void rotateSecret(String name, String newValue, String userId) {
        // TODO: Verify authorization to rotate
        // TODO: Create new version with new value
        // TODO: Keep old versions for grace period
    }

    /**
     * Helper: Encrypt data
     *
     * TODO: Implement AES encryption
     */
    private byte[] encrypt(byte[] data, SecretKey key) {
        // TODO: Use AES/GCM for authenticated encryption
        //   Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        //   cipher.init(Cipher.ENCRYPT_MODE, key);
        //   return cipher.doFinal(data);
        return null; // Replace
    }

    /**
     * Helper: Decrypt data
     *
     * TODO: Implement AES decryption
     */
    private byte[] decrypt(byte[] encryptedData, SecretKey key) {
        // TODO: Use AES/GCM for decryption
        return null; // Replace
    }
}
```

**Runnable Client Code:**

```java
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.*;

public class SecretsManagerClient {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Secrets Management ===\n");

        // Generate master key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey masterKey = keyGen.generateKey();

        SecretsManager sm = new SecretsManager(masterKey);

        // Test 1: Store secrets
        System.out.println("--- Test 1: Store Secrets ---");
        Set<String> users1 = new HashSet<>(Arrays.asList("admin", "service1"));
        sm.storeSecret("db_password", "super-secret-pwd", users1);
        System.out.println("Stored db_password");

        Set<String> users2 = new HashSet<>(Arrays.asList("service2"));
        sm.storeSecret("api_key", "sk_live_123456", users2);
        System.out.println("Stored api_key");

        // Test 2: Retrieve secrets
        System.out.println("\n--- Test 2: Retrieve Secrets ---");
        String pwd = sm.getSecret("db_password", "admin");
        System.out.println("Retrieved db_password: " + pwd);

        // Test 3: Unauthorized access
        System.out.println("\n--- Test 3: Unauthorized Access ---");
        try {
            String key = sm.getSecret("api_key", "admin");
            System.out.println("Retrieved api_key: " + key);
        } catch (SecurityException e) {
            System.out.println("Access denied: " + e.getMessage());
        }
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix security bugs in broken implementations. This tests your understanding.

### Challenge 1: Broken JWT Validation

```java
/**
 * This JWT validator has 3 CRITICAL SECURITY BUGS. Find them!
 */
public class BrokenJWTValidator {

    private final String secret = "my-secret-key";

    public String validateToken_Buggy(String token) {
        // BUG 1: What's missing before splitting?
        String[] parts = token.split("\\.");

        // Extract payload
        String payload = parts[1];
        String decodedPayload = base64Decode(payload);

        // BUG 2: What critical check is missing?
        // Parse JSON to get user ID
        String userId = extractUserId(decodedPayload);

        // BUG 3: What about expiration?
        return userId;
    }
}
```

**Your debugging:**

- **Bug 1 location:** _[Which line?]_
- **Bug 1 explanation:** _[What if token is malformed?]_
- **Bug 1 fix:** _[What validation is needed?]_

- **Bug 2 location:** _[Which line?]_
- **Bug 2 explanation:** _[Most critical security flaw - what's missing?]_
- **Bug 2 fix:** _[What MUST be verified?]_

- **Bug 3 location:** _[Which line?]_
- **Bug 3 explanation:** _[What timing issue exists?]_
- **Bug 3 fix:** _[What field to check?]_

**Security impact:** What can an attacker do with these bugs?
- Bug 1 impact: _[Fill in]_
- Bug 2 impact: _[Fill in - this is the worst one!]_
- Bug 3 impact: _[Fill in]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 9):** No null/length check on token or parts. Attacker can send malformed token causing ArrayIndexOutOfBoundsException or NullPointerException.

**Fix:**
```java
if (token == null || token.isEmpty()) return null;
String[] parts = token.split("\\.");
if (parts.length != 3) return null;  // JWT must have 3 parts
```

**Bug 2 (Lines 12-16):** NEVER VALIDATES SIGNATURE! This is critical - attacker can forge any token!

**Fix:**
```java
// Before extracting payload, verify signature
String toVerify = parts[0] + "." + parts[1];
String expectedSig = hmacSha256(toVerify, secret);
if (!expectedSig.equals(parts[2])) {
    throw new SecurityException("Invalid signature");
}
```

**Bug 3 (Line 18):** No expiration check. Token valid forever even after user logout or password change.

**Fix:**
```java
long exp = extractExpiration(decodedPayload);
if (System.currentTimeMillis() / 1000 > exp) {
    throw new SecurityException("Token expired");
}
```

**Security impact:**

- Bug 1: Denial of service, crashes
- Bug 2: **Complete authentication bypass** - attacker can impersonate any user!
- Bug 3: Stolen tokens work forever, can't revoke access
</details>

---

### Challenge 2: RBAC Permission Bypass

```java
/**
 * This RBAC implementation has 2 AUTHORIZATION BUGS.
 */
public class BrokenRBAC {

    private Map<String, Set<Role>> userRoles = new HashMap<>();
    private Map<Role, Set<Permission>> rolePermissions = new HashMap<>();

    public boolean hasPermission_Buggy(String userId, Permission permission) {
        Set<Role> roles = userRoles.get(userId);

        // BUG 1: What if user has no roles?
        for (Role role : roles) {
            Set<Permission> perms = rolePermissions.get(role);
            if (perms.contains(permission)) {
                return true;
            }
        }

        return false;
    }

    public void deleteResource_Buggy(String resourceId, String userId) {
        // BUG 2: What's missing here?
        database.delete(resourceId);
        System.out.println("Deleted: " + resourceId);
    }
}
```

**Your debugging:**

- **Bug 1:** _[What exception occurs?]_
- **Bug 1 exploit:** _[Can attacker use this?]_
- **Bug 1 fix:** _[Add what check?]_

- **Bug 2:** _[What's the security flaw?]_
- **Bug 2 exploit:** _[How can attacker abuse this?]_
- **Bug 2 fix:** _[What MUST happen before delete?]_

**Trace through attack scenario:**

- Attacker with no roles calls `deleteResource("admin-data", "attacker")`
- What happens at Bug 1? _[Fill in]_
- What happens at Bug 2? _[Fill in]_
- Final result: _[Is resource deleted? Should it be?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 13):** NullPointerException if user has no roles. `userRoles.get(userId)` returns null.

**Fix:**
```java
Set<Role> roles = userRoles.get(userId);
if (roles == null || roles.isEmpty()) {
    return false;  // No roles = no permissions
}
```

**Bug 2 (Lines 23-26):** NO PERMISSION CHECK BEFORE DELETION! Classic authorization bypass.

**Fix:**
```java
public void deleteResource_Buggy(String resourceId, String userId) {
    // MUST check permission first
    if (!hasPermission(userId, Permission.DELETE)) {
        throw new SecurityException("Insufficient permissions");
    }
    database.delete(resourceId);
    auditLog.log("Deleted: " + resourceId + " by " + userId);
}
```

**Attack scenario:**

1. Attacker calls `deleteResource("admin-data", "attacker")`
2. Bug 2: No permission check, deletion proceeds
3. Result: **Resource deleted despite no authorization!**

**Key lesson:** EVERY sensitive operation MUST have explicit authorization check. One missing check = security hole.
</details>

---

### Challenge 3: Timing Attack on Token Comparison

```java
/**
 * This token validator has a SUBTLE TIMING ATTACK vulnerability.
 */
public class TimingAttackVulnerable {

    private static final String VALID_API_KEY = "sk_live_a1b2c3d4e5f6";

    public boolean validateAPIKey_Buggy(String providedKey) {
        // BUG: What's wrong with this comparison?
        if (providedKey.equals(VALID_API_KEY)) {
            return true;
        }
        return false;
    }

    // Alternative buggy version using manual comparison
    public boolean validateAPIKey_Buggy2(String providedKey) {
        if (providedKey.length() != VALID_API_KEY.length()) {
            return false;
        }

        // BUG: Early return on mismatch
        for (int i = 0; i < VALID_API_KEY.length(); i++) {
            if (providedKey.charAt(i) != VALID_API_KEY.charAt(i)) {
                return false;  // Returns immediately on first mismatch
            }
        }
        return true;
    }
}
```

**Your debugging:**

- **Bug location:** _[Both versions have the same class of bug]_
- **Bug type:** _[What kind of attack is possible?]_
- **Bug explanation:** _[How does attacker exploit timing differences?]_
- **Why is this dangerous?** _[Can attacker guess the secret?]_

**Attack simulation:**

- Try key: `"sk_live_XXXXXXXX"` (wrong prefix) → Takes ___ time
- Try key: `"sk_live_a1XXXXXX"` (first 2 chars match) → Takes ___ time
- Pattern: _[What does attacker learn from timing?]_

**Your fix:** _[How to compare in constant time?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug:** Both use non-constant-time comparison. String comparison returns early on first mismatch, leaking information about which characters are correct.

**Attack:** Attacker measures response times:
- `"sk_live_XXXXXXXX"` → Fast (fails at 8th char)
- `"sk_live_a1XXXXXX"` → Slightly slower (fails at 10th char)
- Attacker learns: first 2 chars after underscore are "a1"
- Repeat for each character → Bruteforce key character-by-character!

**Fix - Constant-time comparison:**
```java
public boolean validateAPIKey_Secure(String providedKey) {
    if (providedKey == null || providedKey.length() != VALID_API_KEY.length()) {
        return false;
    }

    // Constant-time comparison: always checks all characters
    int result = 0;
    for (int i = 0; i < VALID_API_KEY.length(); i++) {
        result |= providedKey.charAt(i) ^ VALID_API_KEY.charAt(i);
    }

    return result == 0;  // 0 means all characters matched
}
```

Or use Java's built-in:
```java
import java.security.MessageDigest;

public boolean validateAPIKey_Secure(String providedKey) {
    return MessageDigest.isEqual(
        providedKey.getBytes(),
        VALID_API_KEY.getBytes()
    );
}
```

**Key lesson:** String/token comparisons MUST be constant-time to prevent timing attacks. This applies to passwords, API keys, HMAC signatures, etc.
</details>

---

### Challenge 4: Secret Exposure in Logs

```java
/**
 * This code accidentally leaks secrets. Find 3 leak points!
 */
public class SecretLeakage {

    private final String dbPassword = System.getenv("DB_PASSWORD");

    public void connectToDatabase() {
        String connectionUrl = "jdbc:postgresql://db.example.com/mydb" +
            "?user=dbuser&password=" + dbPassword;  // BUG 1: What's wrong?

        System.out.println("Connecting to: " + connectionUrl);  // BUG 2: Logs what?

        try {
            Connection conn = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            // BUG 3: What gets logged in stack trace?
            e.printStackTrace();
            logger.error("Database connection failed: " + e.getMessage());
        }
    }

    public String generateJWT(String userId) {
        String secret = System.getenv("JWT_SECRET");
        String token = createToken(userId, secret);
        logger.info("Generated token for user " + userId + ": " + token);  // BUG 4?
        return token;
    }
}
```

**Your debugging:**

- **Bug 1:** _[What's exposed in the URL?]_
- **Bug 2:** _[What gets printed to console?]_
- **Bug 3:** _[What's in the SQLException details?]_
- **Bug 4:** _[Is logging the token a security issue? Why?]_

**Real-world impact:**

- Logs stored in: _[Where can these secrets end up?]_
- Who can access logs: _[List potential exposure points]_
- Lifetime: _[How long do logs persist?]_

**Your fixes:**

1. _[How to connect without password in URL?]_
2. _[How to log without secrets?]_
3. _[How to handle exceptions securely?]_
4. _[What to log instead of full token?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1 (Line 10):** Password in URL! If URL is logged, password exposed.

**Bug 2 (Line 12):** Prints connection URL with password to console/logs!

**Bug 3 (Lines 17-18):** SQLException stack trace may contain connection URL with password. `e.printStackTrace()` goes to stderr (often logged).

**Bug 4 (Line 24):** Logging full JWT token. If logs compromised, attacker can impersonate user.

**Fixes:**

```java
// Fix 1 & 2: Use Properties, don't put password in URL
public void connectToDatabase_Secure() {
    String url = "jdbc:postgresql://db.example.com/mydb";
    Properties props = new Properties();
    props.setProperty("user", "dbuser");
    props.setProperty("password", dbPassword);  // Not in URL

    // Secure logging - no secrets
    System.out.println("Connecting to: " + url);  // URL only, no password

    try {
        Connection conn = DriverManager.getConnection(url, props);
    } catch (SQLException e) {
        // Secure error handling - don't expose details
        logger.error("Database connection failed", e.getErrorCode());
        // Don't log e.getMessage() - may contain connection details
    }
}

// Fix 4: Log token ID only, not full token
public String generateJWT_Secure(String userId) {
    String secret = System.getenv("JWT_SECRET");
    String token = createToken(userId, secret);

    // Log token ID/fingerprint, not full token
    String tokenId = extractTokenId(token);  // Or hash first 16 chars
    logger.info("Generated token " + tokenId + " for user " + userId);

    return token;
}
```

**Real-world impact:**

- Logs go to: files, centralized logging (Splunk/ELK), monitoring, backups, cloud storage
- Access by: developers, ops, security team, log aggregation services
- Lifetime: Days to years (compliance may require long retention)

**One leaked secret in logs = permanent exposure!**
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found JWT signature validation bypass (Challenge 1)
- [ ] Found RBAC authorization bypass (Challenge 2)
- [ ] Understood timing attack vulnerability (Challenge 3)
- [ ] Found all secret leakage points (Challenge 4)
- [ ] Could explain WHY each bug is dangerous
- [ ] Learned common security mistakes to avoid

**Common security bugs you discovered:**

1. _[List the patterns - e.g., "Missing signature validation"]_
2. _[Fill in]_
3. _[Fill in]_
4. _[Fill in]_

**Your security checklist for code review:**

- [ ] All JWT tokens validated (signature + expiration)
- [ ] All sensitive operations have authorization checks
- [ ] No secrets in logs, URLs, or error messages
- [ ] Constant-time comparison for secrets/tokens
- [ ] Null checks before accessing collections
- [ ] Fail-secure (deny by default)

---

## Decision Framework

**Your task:** Build decision trees for when to use each security pattern.

### Question 1: JWT vs Session-Based Auth?

Answer after implementation:

**Use JWT when:**

- Stateless architecture: _[No session storage needed]_
- Microservices: _[Token contains all necessary data]_
- Mobile/SPA apps: _[Easy to store and send]_
- Cross-domain: _[Can share across services]_

**Use Session-based when:**

- Traditional web apps: _[Server-side sessions]_
- Need to revoke immediately: _[Can invalidate server-side]_
- Large user data: _[Don't want to send in every request]_
- Simpler security model: _[Server controls everything]_

### Question 2: When to use API Keys vs JWT?

**API Keys when:**

- Service-to-service: _[Long-lived credentials]_
- Simple auth: _[Just need to identify caller]_
- Third-party integrations: _[Easy to rotate]_

**JWT when:**

- User authentication: _[Short-lived, contains user claims]_
- Need user context: _[Embedded in token]_
- Stateless: _[No lookup needed]_

### Question 3: RBAC vs ABAC (Attribute-Based)?

**RBAC when:**

- Clear role hierarchy: _[Admin, Editor, Viewer]_
- Simple permissions: _[Read, Write, Delete]_
- Most users: _[70% of access control needs]_

**ABAC when:**

- Complex rules: _[Based on time, location, resource attributes]_
- Fine-grained control: _[User can edit own posts only]_
- Dynamic policies: _[Rules change frequently]_

### Your Decision Tree

Build this after solving practice scenarios:

```
Security Pattern Selection
│
├─ What are you securing?
│   ├─ User sessions → JWT or Session-based
│   ├─ API endpoints → API Keys or JWT
│   └─ Resources → RBAC or ABAC
│
├─ What's the architecture?
│   ├─ Monolith → Session-based + RBAC
│   ├─ Microservices → JWT + RBAC
│   └─ Serverless → JWT + API Keys
│
├─ What's the threat model?
│   ├─ External attackers → Strong encryption, rotation
│   ├─ Internal threats → Audit logging, least privilege
│   └─ Compliance (PCI/HIPAA) → Secrets management, encryption at rest
│
└─ Performance requirements?
    ├─ High throughput → Stateless (JWT, API keys)
    ├─ Strong consistency → Stateful (Sessions, central auth)
    └─ Offline support → JWT with refresh tokens
```

### The "Kill Switch" - Security Anti-Patterns

**Don't do this:**

1. **Store passwords in plain text** - _[Always hash with bcrypt/argon2]_
2. **Use weak secrets** - _[Minimum 32 bytes entropy]_
3. **Hard-code credentials** - _[Use environment vars or secrets manager]_
4. **Send tokens in URL** - _[Use headers or secure cookies]_
5. **Never rotate secrets** - _[Rotate regularly, especially after breaches]_
6. **Trust client-side validation** - _[Always validate on server]_
7. **Use MD5/SHA1 for passwords** - _[Use bcrypt, scrypt, or argon2]_

### The Rule of Three: Alternatives

**Option 1: JWT (Stateless)**

- Pros: _[Scalable, no server state, works across services]_
- Cons: _[Can't revoke easily, token size, need refresh mechanism]_
- Use when: _[Microservices, SPAs, mobile apps]_

**Option 2: Session (Stateful)**

- Pros: _[Easy to revoke, smaller cookies, server controls state]_
- Cons: _[Requires session store, sticky sessions, doesn't scale horizontally easily]_
- Use when: _[Monoliths, need immediate revocation, simpler security model]_

**Option 3: OAuth2/OIDC (Delegated)**

- Pros: _[Industry standard, handles complex flows, third-party integration]_
- Cons: _[Complex implementation, relies on identity provider]_
- Use when: _[Social login, enterprise SSO, third-party services]_

---

## Practice

### Scenario 1: E-commerce API Security

**Requirements:**

- REST API for orders, payments, user data
- Mobile app and web frontend
- Third-party integrations (shipping, payments)
- Must handle 10K requests/sec

**Your security design:**

- Auth mechanism: _[JWT or API keys? Why?]_
- Authorization: _[RBAC setup for customer, admin, partner roles]_
- Secrets: _[How to manage payment gateway keys?]_
- Token expiry: _[Short-lived or long-lived? Refresh strategy?]_
- Rate limiting: _[Per user? Per API key?]_

### Scenario 2: Multi-Tenant SaaS Platform

**Requirements:**

- Tenants: organizations with multiple users
- Data isolation between tenants
- Admin panel for tenant admins
- SSO support for enterprise customers

**Your security design:**

- Tenant isolation: _[How to ensure data separation?]_
- User roles: _[Super admin, tenant admin, user]_
- SSO integration: _[SAML, OAuth2, or both?]_
- Token claims: _[What to include in JWT?]_
- Cross-tenant attacks: _[How to prevent?]_

### Scenario 3: Microservices Internal Auth

**Requirements:**

- 20 microservices
- Services call each other
- Need to track which service made request
- Some services more privileged than others

**Your security design:**

- Service-to-service auth: _[Mutual TLS? JWT? API keys?]_
- Service identity: _[How to identify calling service?]_
- Permission model: _[Service-level RBAC?]_
- Secret distribution: _[How do services get credentials?]_
- Rotation: _[How to rotate without downtime?]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
    - [ ] JWT generation and validation work
    - [ ] RBAC role assignment and permission checks work
    - [ ] API key generation and validation work
    - [ ] Secrets manager encrypt/decrypt work
    - [ ] All client code runs successfully

- [ ] **Understanding**
    - [ ] Filled in all ELI5 explanations
    - [ ] Understand JWT structure and claims
    - [ ] Know difference between authentication and authorization
    - [ ] Understand RBAC role hierarchies
    - [ ] Know when to use each auth mechanism

- [ ] **Security Principles**
    - [ ] Never store passwords in plain text
    - [ ] Always use HTTPS for token transmission
    - [ ] Implement token expiration and refresh
    - [ ] Use strong random for key generation
    - [ ] Validate and sanitize all inputs

- [ ] **Decision Making**
    - [ ] Know when to use JWT vs sessions
    - [ ] Know when to use API keys vs JWT
    - [ ] Completed practice scenarios
    - [ ] Can explain security trade-offs

- [ ] **Mastery Check**
    - [ ] Could implement JWT from memory
    - [ ] Could design auth for new system
    - [ ] Understand security threat models
    - [ ] Know common vulnerabilities (OWASP Top 10)

---

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery of security patterns through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Junior Developer

**Scenario:** A junior developer asks you about authentication vs authorization.

**Your explanation (write it out):**

> "Authentication is..."
>
> _[Fill in your explanation - 2-3 sentences]_
>
> "Authorization is..."
>
> _[Fill in your explanation - 2-3 sentences]_
>
> "Real-world example: When you go to a concert..."
>
> _[Complete the analogy]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by a non-technical person? _[Yes/No]_
- Did you use analogies or real-world examples? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Security Design Exercise

**Task:** Design authentication and authorization for a blog platform without looking at code.

**Requirements:**

- Users can be: viewers, authors, editors, admins
- Viewers can read posts
- Authors can create and edit their own posts
- Editors can edit any post
- Admins can delete posts and manage users

**Your design:**

**1. Authentication mechanism:**

- I would use: _[JWT/Session/API Keys - which and why?]_
- Token expiration: _[How long? Why?]_
- Refresh strategy: _[How to handle expiration?]_

**2. Authorization model:**
```
Role Hierarchy:
- VIEWER: _[List permissions]_
- AUTHOR: _[List permissions]_
- EDITOR: _[List permissions]_
- ADMIN: _[List permissions]_
```

**3. Permission checks:**

- Before creating post: _[What to check?]_
- Before editing post: _[What to check?]_
- Before deleting post: _[What to check?]_

**4. Edge cases:**

- What if author tries to edit another author's post? _[Allow/Deny - Why?]_
- What if admin role is compromised? _[Mitigation strategy?]_
- What if JWT secret is leaked? _[Response plan?]_

**Verification:**

- [ ] Chose appropriate auth mechanism with justification
- [ ] Defined clear role hierarchy
- [ ] Identified all permission check points
- [ ] Considered security edge cases

---

### Gate 3: JWT Deep Dive

**Without looking at your notes, answer:**

**JWT Structure:**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIn0.signature
```

| Part | Name | Contains | Encoded? | Signed? |
|------|------|----------|----------|---------|
| Part 1 | _[?]_ | _[?]_ | _[Yes/No]_ | _[Yes/No]_ |
| Part 2 | _[?]_ | _[?]_ | _[Yes/No]_ | _[Yes/No]_ |
| Part 3 | _[?]_ | _[?]_ | _[Yes/No]_ | _[Yes/No]_ |

**Deep questions:**

1. **Can client read the payload?** _[Yes/No - Explain why]_

2. **Can client modify the payload?** _[What happens if they try?]_

3. **Why use HMAC-SHA256 for signature?** _[What does it prevent?]_

4. **What's in the "exp" claim?** _[Format? Purpose?]_

5. **How to handle expired tokens?** _[Reject? Refresh? Explain]_

**Security scenario:**

An attacker obtains this token:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwicm9sZSI6InZpZXdlciIsImV4cCI6MTczNTY4MDAwMH0.xyz
```

Attacker decodes payload to:
```json
{
  "sub": "user123",
  "role": "viewer",
  "exp": 1735680000
}
```

Attacker changes "role" to "admin" and re-encodes. What happens?

Your answer: _[Explain the attack failure]_

---

### Gate 4: RBAC Implementation Test

**Set a 10-minute timer. Implement without looking at notes:**

```java
/**
 * Implement: Check if user has permission through their roles
 * Must handle: null users, empty roles, role hierarchy
 */
public class RBACChallenge {

    private Map<String, Set<Role>> userRoles;
    private Map<Role, Set<Permission>> rolePermissions;

    public boolean hasPermission(String userId, Permission permission) {
        // Your implementation here










        return false; // Replace
    }
}
```

**Test with:**

- User "alice" has roles [ADMIN]
- ADMIN role has permissions [READ, WRITE, DELETE]
- `hasPermission("alice", Permission.DELETE)` → Expected: `true`

**Verification:**

- [ ] Implemented correctly without looking
- [ ] Handles null userId
- [ ] Handles user with no roles
- [ ] Handles role with no permissions
- [ ] Time complexity is O(R*P) where R=roles, P=permissions per role

---

### Gate 5: Security Threat Analysis

**Scenario:** Your API uses JWT for authentication. An attacker has captured network traffic.

**Threat analysis:**

| Attack Vector | Can attacker succeed? | Why/Why not? | Mitigation |
|---------------|---------------------|--------------|------------|
| Replay captured token | _[Yes/No]_ | _[Explain]_ | _[How to prevent?]_ |
| Modify token payload | _[Yes/No]_ | _[Explain]_ | _[What stops this?]_ |
| Bruteforce HMAC secret | _[Yes/No]_ | _[Explain]_ | _[What makes it hard?]_ |
| Use token after logout | _[Yes/No]_ | _[Explain]_ | _[Stateless problem?]_ |
| Timing attack on validation | _[Yes/No]_ | _[Explain]_ | _[How to prevent?]_ |

**For each "Yes" answer:**

- Severity (Low/Medium/High/Critical): ___
- Your mitigation strategy: _[Explain]_

**Deep question:** If JWT is stateless, how can you implement logout?

Your answer: _[Explain the trade-offs of different approaches]_

---

### Gate 6: Secret Management Decision

**Scenario:** Your microservices need to access database passwords, API keys, and encryption keys.

**Option A:** Environment variables
```bash
export DB_PASSWORD="secret123"
export API_KEY="sk_live_xyz"
```

**Pros:** _[List 3]_
1. _[Fill in]_
2. _[Fill in]_
3. _[Fill in]_

**Cons:** _[List 3]_
1. _[Fill in]_
2. _[Fill in]_
3. _[Fill in]_

**Option B:** Secrets Manager (Vault/AWS Secrets Manager)
```java
String password = secretsManager.getSecret("db_password");
```

**Pros:** _[List 3]_
1. _[Fill in]_
2. _[Fill in]_
3. _[Fill in]_

**Cons:** _[List 3]_
1. _[Fill in]_
2. _[Fill in]_
3. _[Fill in]_

**Your decision:** For production with 20 microservices, I would choose _[A/B]_ because...

_[Fill in your reasoning - consider: rotation, audit, compliance, operations, cost]_

**What would make you change your decision?**

- _[Fill in - what constraints would flip your choice?]_

---

### Gate 7: Bug Hunt - Security Code Review

**You have 5 minutes. Find ALL security bugs:**

```java
public class SecurityReview {

    private String jwtSecret = "secret";

    public String login(String username, String password) {
        if (username.equals(password)) {
            return generateJWT(username);
        }
        return null;
    }

    public String generateJWT(String userId) {
        String header = "{\"alg\":\"HS256\"}";
        String payload = "{\"sub\":\"" + userId + "\"}";
        String signature = hash(header + payload + jwtSecret);
        return base64(header) + "." + base64(payload) + "." + signature;
    }

    public boolean authorize(String token, String action) {
        String[] parts = token.split("\\.");
        String payload = decode(parts[1]);
        return payload.contains("\"sub\":\"admin\"");
    }

    public void deleteUser(String userId) {
        database.execute("DELETE FROM users WHERE id = " + userId);
    }
}
```

**Bugs found:** (List them all)
1. _[Bug + Line number + Why it's dangerous]_
2. _[Fill in]_
3. _[Fill in]_
4. _[Fill in]_
5. _[Fill in]_
6. _[Fill in]_

**Score:** ___/6 bugs found

If you found less than 5, review the Debugging Challenges section.

<details markdown>
<summary>Click to see all bugs</summary>

1. **Line 3:** Hardcoded secret in source code (use environment variable)
2. **Line 6:** Timing attack vulnerable - use constant-time comparison
3. **Line 13:** No expiration in JWT payload
4. **Line 19:** No signature validation in authorize()
5. **Line 20:** Authorization check on string contains (can be spoofed)
6. **Line 24:** SQL injection vulnerability (use prepared statements)

**Additional issues:**

- No input validation (null checks)
- No audit logging
- No rate limiting
</details>

---

### Gate 8: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain to an imaginary person when NOT to use JWT.

Your explanation:

> "You should NOT use JWT when..."
>
> _[Fill in - list 3-4 scenarios and explain why]_

**Examples of when it fails:**

1. _[Scenario where JWT doesn't work well]_
2. _[Scenario where another approach is better]_
3. _[Fill in]_

**Now explain:** "You should NOT use RBAC when..."

> _[Fill in - when is RBAC too simple? When is ABAC better?]_

---

### Mastery Certification

**I certify that I can:**

- [ ] Implement JWT generation and validation from memory
- [ ] Implement RBAC with role hierarchy
- [ ] Explain authentication vs authorization clearly
- [ ] Identify common security vulnerabilities
- [ ] Design auth systems for different architectures
- [ ] Compare trade-offs between auth mechanisms
- [ ] Debug security issues systematically
- [ ] Teach these concepts to someone else

**Security mindset check:**

- [ ] I think "how can this be attacked?" when reviewing code
- [ ] I validate and sanitize ALL inputs
- [ ] I check authorization before sensitive operations
- [ ] I use constant-time comparisons for secrets
- [ ] I never log sensitive data
- [ ] I design with "fail secure" principle
- [ ] I understand the security/usability trade-off

**Self-assessment score:** ___/10

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score ≥ 8:** Congratulations! You've mastered security patterns. Proceed to the next topic.
