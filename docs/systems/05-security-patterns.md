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

**Next:** [06. Rate Limiting →](06-rate-limiting.md)

**Back:** [04. API Design ←](04-api-design.md)
