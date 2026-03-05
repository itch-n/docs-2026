# API Design

> REST, GraphQL, and RPC - Choosing the right API paradigm

---

## Learning Objectives

By the end of this topic you will be able to:

- Design a REST API with correct resource naming, HTTP method usage, status codes, and pagination
- Implement a GraphQL query resolver with field-level resolution to eliminate over-fetching
- Compare REST, GraphQL, and RPC and choose the appropriate paradigm for a given use case
- Apply idempotency patterns to mutation endpoints to make them safe to retry
- Identify and correct common API mistakes: wrong HTTP methods, missing versioning, poor error structure
- Version an API so that existing clients are not broken when the schema evolves

---

## ELI5: Explain Like I'm 5

<div class="learner-section" markdown>

**Your task:** After implementing different API patterns, explain them simply.

**Prompts to guide you:**

1. **What is REST in one sentence?**
    - REST is a style for building APIs where <span class="fill-in">[resources are identified by ___ and actions are expressed using HTTP ___ like GET, POST, PUT, and DELETE]</span>

2. **Why do we use REST for web APIs?**
    - REST is popular because <span class="fill-in">[it maps naturally onto ___, is ___ to explore without a client library, and its ___ nature means any server can handle any ___]</span>

3. **Real-world analogy for REST:**
    - Example: "REST is like a restaurant menu where..."
    - Think about a menu where each dish is a resource with a name (URL), and you order (GET), create (POST), replace (PUT), or cancel (DELETE).
    - Your analogy: <span class="fill-in">[Fill in]</span>

4. **What is GraphQL in one sentence?**
    - GraphQL is a query language for APIs where <span class="fill-in">[the ___ specifies exactly which ___ it needs, so the server returns only ___ and never ___ or under-fetches]</span>

5. **When would you choose GraphQL over REST?**
    - GraphQL is preferred when <span class="fill-in">[different ___ (mobile vs web) need ___ shapes of data from the same endpoint, making multiple ___ round trips impractical]</span>

6. **Real-world analogy for GraphQL:**
    - Example: "GraphQL is like a buffet where..."
    - Think about a buffet where you pick exactly what you want rather than being served a fixed plate.
    - Your analogy: <span class="fill-in">[Fill in]</span>

7. **What is RPC (gRPC) in one sentence?**
    - gRPC is a framework for calling ___ on a remote service <span class="fill-in">[as if they were ___, using ___ contracts defined in `.proto` files and binary serialisation over ___]</span>

8. **When would you use RPC instead of REST?**
    - RPC is preferred when <span class="fill-in">[services are ___ each other inside the same data centre, latency is critical, and a strict typed ___ between services is more valuable than ___]</span>

</div>

---

## Quick Quiz (Do BEFORE implementing)

!!! tip "How to use this section"
    Complete your predictions now, before reading further. You will revisit and verify each answer after working through the implementations.

<div class="learner-section" markdown>

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **REST API endpoint for listing resources:**
    - What HTTP method should be used? <span class="fill-in">[Your guess: GET/POST/PUT/DELETE]</span>
    - What status code for success? <span class="fill-in">[Your guess: 200/201/204]</span>
    - Verified after learning: <span class="fill-in">[Actual: ?]</span>

2. **GraphQL query that fetches nested data:**
    - How many HTTP requests needed? <span class="fill-in">[Your guess: 1/multiple]</span>
    - Compared to REST for same data: <span class="fill-in">[More/Less/Same requests?]</span>
    - Verified: <span class="fill-in">[Actual]</span>

3. **API pagination parameters:**
    - Common parameter names: <span class="fill-in">[Your guesses]</span>
    - Default limit should be: <span class="fill-in">[Your guess: 10/50/100/unlimited?]</span>
    - Verified: <span class="fill-in">[Actual best practices]</span>

### Scenario Predictions

**Scenario 1:** Design endpoint to get user with their posts and comments

- **REST approach:** How many endpoints? <span class="fill-in">[Your guess]</span>
- **GraphQL approach:** How many endpoints? <span class="fill-in">[Your guess]</span>
- **Which has over-fetching risk?** <span class="fill-in">[REST/GraphQL - Why?]</span>
- **Which has N+1 query risk?** <span class="fill-in">[REST/GraphQL - Why?]</span>

**Scenario 2:** Client needs to update user's email address

- **Which HTTP method?** <span class="fill-in">[GET/POST/PUT/PATCH/DELETE]</span>
- **PUT vs PATCH - what's the difference?** <span class="fill-in">[Fill in]</span>
- **Success status code:** <span class="fill-in">[200/201/204/304]</span>
- **If email already taken, status code:** <span class="fill-in">[400/404/409/500]</span>

**Scenario 3:** API versioning strategy

- **Version in URL (/v1/users) or header?** <span class="fill-in">[Which is better? Why?]</span>
- **When to create new version?** <span class="fill-in">[Fill in your reasoning]</span>
- **How to deprecate old version?** <span class="fill-in">[Your approach]</span>

### Trade-off Quiz

**Question:** When would REST be BETTER than GraphQL?

- Your answer: <span class="fill-in">[Fill in before implementation]</span>
- Verified answer: <span class="fill-in">[Fill in after learning]</span>

**Question:** What's the MAIN benefit of API pagination?

- [ ] Makes API look professional
- [ ] Prevents database from crashing
- [ ] Reduces response size and improves performance
- [ ] Required by HTTP specification

Verify after implementation: <span class="fill-in">[Which one(s)?]</span>

**Question:** What makes an API idempotent?

- Your answer: <span class="fill-in">[Fill in]</span>
- Example of idempotent operation: <span class="fill-in">[Fill in]</span>
- Example of non-idempotent operation: <span class="fill-in">[Fill in]</span>

</div>

---

## Core Implementation

### Part 1: REST API Design

**Your task:** Implement a simple REST API with proper resource design.

```java
import java.util.*;

/**
 * REST API: Resource-oriented design with HTTP verbs
 *
 * Key principles:
 * - Resources are nouns (users, posts, comments)
 * - HTTP verbs define actions (GET, POST, PUT, DELETE)
 * - Stateless communication
 * - HATEOAS (links to related resources)
 */

// Resource representation
class User {
    String id;
    String name;
    String email;
    List<String> postIds; // Links to posts

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.postIds = new ArrayList<>();
    }
}

class Post {
    String id;
    String authorId; // Link to user
    String title;
    String content;
    long timestamp;

    public Post(String id, String authorId, String title, String content) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
}

public class RESTAPIServer {

    private Map<String, User> users = new HashMap<>();
    private Map<String, Post> posts = new HashMap<>();

    /**
     * GET /users/{id}
     * Retrieve a user by ID
     *
     * TODO: Implement user retrieval
     * - Return user if exists
     * - Return 404 if not found
     * - Include links to user's posts (HATEOAS)
     */
    public Response getUser(String userId) {
        // TODO: Lookup user in users map

        // TODO: Implement iteration/conditional logic
        // Include links: { "posts": "/users/{id}/posts" }

        // TODO: Implement iteration/conditional logic

        return null; // Replace
    }

    /**
     * POST /users
     * Create a new user
     *
     * TODO: Implement user creation
     * - Validate input (name, email required)
     * - Generate unique ID
     * - Store user
     * - Return 201 Created with Location header
     */
    public Response createUser(String name, String email) {
        // TODO: Validate input

        // TODO: Generate ID (UUID)

        // TODO: Create and store user

        // TODO: Return 201 with Location: /users/{id}

        return null; // Replace
    }

    /**
     * PUT /users/{id}
     * Update an existing user
     *
     * TODO: Implement user update
     * - Full replacement of resource
     * - Return 200 if updated, 404 if not found
     */
    public Response updateUser(String userId, String name, String email) {
        // TODO: Check if user exists

        // TODO: Replace user data completely

        // TODO: Return appropriate status

        return null; // Replace
    }

    /**
     * DELETE /users/{id}
     * Delete a user
     *
     * TODO: Implement user deletion
     * - Remove user and cascade delete posts
     * - Return 204 No Content if successful
     */
    public Response deleteUser(String userId) {
        // TODO: Remove user

        // TODO: Remove all user's posts

        // TODO: Return 204

        return null; // Replace
    }

    /**
     * GET /users/{userId}/posts
     * Get all posts by a user (nested resource)
     *
     * TODO: Implement nested resource retrieval
     * - Support pagination (page, limit)
     * - Return list of posts with links
     */
    public Response getUserPosts(String userId, int page, int limit) {
        // TODO: Get user's post IDs

        // TODO: Paginate results

        // TODO: Return posts with pagination metadata

        return null; // Replace
    }

    // Response wrapper
    static class Response {
        int statusCode;
        Object body;
        Map<String, String> headers;

        Response(int statusCode, Object body) {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = new HashMap<>();
        }
    }
}
```

!!! warning "Debugging Challenge — Wrong HTTP Methods"

    The controller below uses HTTP methods incorrectly in two places. Identify which operations are wrong and explain why the incorrect method matters beyond just convention.

    ```java
    // Operation 1: Get user profile
    @PostMapping("/users/{id}/profile")
    public User getProfile(@PathParam("id") String id) {
        return database.getUser(id);
    }

    // Operation 2: Update user's email (pass new email as query param)
    @GetMapping("/users/{id}/updateEmail")
    public Response updateEmail(
        @PathParam("id") String id,
        @RequestParam("email") String newEmail
    ) {
        User user = database.getUser(id);
        user.email = newEmail;
        database.save(user);
        return Response.ok("Email updated");
    }
    ```

    ??? success "Answer"

        **Operation 1 — POST should be GET:**
        `POST /users/{id}/profile` implies creating a new resource. Using GET is correct for a read-only fetch. Additionally, GET responses can be cached by browsers and proxies; POST responses cannot. Using POST for reads bypasses caching and signals to clients that the call has side effects.

        **Operation 2 — GET should be PATCH:**
        Using GET to modify state violates HTTP semantics. GET requests are expected to be safe (no side effects) and idempotent. Search engines, browser pre-fetchers, and monitoring tools routinely issue GET requests — any of these would inadvertently trigger an email update. PATCH (or PUT) signals that the request modifies server state and should not be pre-fetched.

---

### Part 2: GraphQL Query Engine

**Your task:** Implement a simple GraphQL query resolver.

```java
/**
 * GraphQL: Client specifies exactly what data they need
 *
 * Key principles:
 * - Single endpoint
 * - Client defines query shape
 * - No over-fetching or under-fetching
 * - Strong typing with schema
 */

class GraphQLSchema {
    // Schema definition
    String schema = """
        type User {
            id: ID!
            name: String!
            email: String!
            posts: [Post]
        }

        type Post {
            id: ID!
            title: String!
            content: String!
            author: User!
        }

        type Query {
            user(id: ID!): User
            users: [User]
            post(id: ID!): Post
        }
    """;
}

public class GraphQLResolver {

    private Map<String, User> users = new HashMap<>();
    private Map<String, Post> posts = new HashMap<>();

    /**
     * Resolve a GraphQL query
     *
     * Example query:
     * {
     *   user(id: "123") {
     *     name
     *     posts {
     *       title
     *     }
     *   }
     * }
     *
     * TODO: Implement query resolver
     * 1. Parse query (simplified - assume already parsed)
     * 2. Resolve requested fields only
     * 3. Handle nested relationships
     */
    public Map<String, Object> executeQuery(String query) {
        // TODO: Parse query to understand requested fields

        // TODO: Resolve root field (user, users, post)

        // TODO: Implement iteration/conditional logic

        // TODO: Return only requested data

        return null; // Replace
    }

    /**
     * Field resolver for User.posts
     * Only called if "posts" is in the query
     *
     * TODO: Implement field resolver
     * - Get user's post IDs
     * - Return list of Post objects
     */
    public List<Post> resolveUserPosts(User user) {
        // TODO: Look up posts by user.postIds

        return null; // Replace
    }

    /**
     * Field resolver for Post.author
     * Only called if "author" is in the query
     *
     * TODO: Implement field resolver
     * - Get post's author
     * - Return User object
     */
    public User resolvePostAuthor(Post post) {
        // TODO: Look up user by post.authorId

        return null; // Replace
    }
}
```

### Part 3: RPC (Remote Procedure Call)

**Your task:** Implement a simple RPC server with method invocation.

```java
/**
 * RPC: Call remote methods as if they were local
 *
 * Key principles:
 * - Action-oriented (not resource-oriented)
 * - Direct method invocation
 * - Binary protocols (e.g., Protocol Buffers)
 * - Efficient for service-to-service calls
 */

interface UserService {
    User getUser(String userId);
    String createUser(String name, String email);
    boolean updateUser(String userId, String name, String email);
    boolean deleteUser(String userId);
    List<Post> getUserPosts(String userId);
}

public class RPCServer implements UserService {

    private Map<String, User> users = new HashMap<>();
    private Map<String, Post> posts = new HashMap<>();

    /**
     * RPC Method: getUser
     * Direct method call, no HTTP concepts
     *
     * TODO: Implement user retrieval
     * - Return user object or null
     * - Throw exception if error
     */
    @Override
    public User getUser(String userId) {
        // TODO: Lookup and return user

        return null; // Replace
    }

    /**
     * RPC Method: createUser
     *
     * TODO: Implement user creation
     * - Return new user ID
     * - Throw exception if validation fails
     */
    @Override
    public String createUser(String name, String email) {
        // TODO: Validate and create user

        // TODO: Return generated ID

        return null; // Replace
    }

    /**
     * RPC Method: updateUser
     *
     * TODO: Implement user update
     * - Return true if success, false if not found
     * - Throw exception if validation fails
     */
    @Override
    public boolean updateUser(String userId, String name, String email) {
        // TODO: Update user

        return false; // Replace
    }

    /**
     * RPC Method: deleteUser
     *
     * TODO: Implement user deletion
     * - Return true if deleted, false if not found
     */
    @Override
    public boolean deleteUser(String userId) {
        // TODO: Delete user and posts

        return false; // Replace
    }

    /**
     * RPC Method: getUserPosts
     *
     * TODO: Implement posts retrieval
     * - Return list of posts
     * - Return empty list if user not found
     */
    @Override
    public List<Post> getUserPosts(String userId) {
        // TODO: Get user's posts

        return null; // Replace
    }
}
```

---

## Client Code

```java
public class APIDesignClient {

    public static void main(String[] args) {
        testRESTAPI();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testGraphQL();
        System.out.println("\n" + "=".repeat(50) + "\n");
        testRPC();
    }

    static void testRESTAPI() {
        System.out.println("=== REST API Test ===\n");

        RESTAPIServer server = new RESTAPIServer();

        // Test: Create user
        System.out.println("POST /users");
        RESTAPIServer.Response createResp = server.createUser("Alice", "alice@example.com");
        System.out.println("Status: " + createResp.statusCode);
        System.out.println("Body: " + createResp.body);

        // Test: Get user
        System.out.println("\nGET /users/123");
        RESTAPIServer.Response getResp = server.getUser("123");
        System.out.println("Status: " + getResp.statusCode);
        System.out.println("Body: " + getResp.body);

        // Test: Get user's posts
        System.out.println("\nGET /users/123/posts?page=1&limit=10");
        RESTAPIServer.Response postsResp = server.getUserPosts("123", 1, 10);
        System.out.println("Status: " + postsResp.statusCode);
        System.out.println("Body: " + postsResp.body);
    }

    static void testGraphQL() {
        System.out.println("=== GraphQL Test ===\n");

        GraphQLResolver resolver = new GraphQLResolver();

        // Test: Query user with specific fields
        String query = """
            {
              user(id: "123") {
                name
                email
                posts {
                  title
                }
              }
            }
        """;

        System.out.println("Query:");
        System.out.println(query);

        Map<String, Object> result = resolver.executeQuery(query);
        System.out.println("\nResult: " + result);
    }

    static void testRPC() {
        System.out.println("=== RPC Test ===\n");

        UserService service = new RPCServer();

        // Test: Create user (direct method call)
        System.out.println("createUser(\"Bob\", \"bob@example.com\")");
        String userId = service.createUser("Bob", "bob@example.com");
        System.out.println("Returned ID: " + userId);

        // Test: Get user
        System.out.println("\ngetUser(\"" + userId + "\")");
        User user = service.getUser(userId);
        System.out.println("Returned: " + (user != null ? user.name : "null"));

        // Test: Get user posts
        System.out.println("\ngetUserPosts(\"" + userId + "\")");
        List<Post> posts = service.getUserPosts(userId);
        System.out.println("Returned: " + posts.size() + " posts");
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken API implementations. This tests your understanding.

### Challenge 1: Broken API Versioning

```java
/**
 * API versioning attempt - but has 2 CRITICAL BUGS.
 * Find them!
 */

@RestController
public class UserController {

    // Version 1 endpoint
    @GetMapping("/v1/users/{id}")
    public UserV1 getUserV1(String id) {
        User user = database.getUser(id);
        return convertToV1(user);
    }

    // Version 2 endpoint - BUG 1: What's wrong with this route?
    @GetMapping("/users/{id}")
    public UserV2 getUserV2(String id) {
        User user = database.getUser(id);
        return new UserV2(user.id, user.firstName, user.lastName, user.email);
    }

    // Update endpoint - BUG 2: Version handling issue
    @PutMapping("/users/{id}")
    public Response updateUser(String id, @RequestBody UpdateUserRequest req) {
        // Updates internal model
        User user = database.getUser(id);
        user.firstName = req.firstName;  // What if v1 client calls this?
        user.lastName = req.lastName;
        database.save(user);

        return Response.ok(user);
    }
}
```

**Your debugging:**

- Bug 1: <span class="fill-in">[What's the bug?]</span>

- Bug 2: <span class="fill-in">[What's the bug?]</span>

??? success "Answer"

    **Bug 1:** Version 2 endpoint uses `/users/{id}` instead of `/v2/users/{id}`. This creates ambiguity - which version is
    the "default"? Clients expect explicit versioning.

    **Fix:**

    ```java
    @GetMapping("/v2/users/{id}")
    public UserV2 getUserV2(String id) {
        // Now explicit
    }
    ```

    **Bug 2:** Single update endpoint doesn't handle version differences. V1 clients send `name` field, V2 clients send
    `firstName`/`lastName`. Need separate endpoints or request versioning.

    **Fix:**

    ```java
    @PutMapping("/v1/users/{id}")
    public Response updateUserV1(String id, @RequestBody UpdateUserV1Request req) {
        User user = database.getUser(id);
        // Parse single name into firstName/lastName
        String[] parts = req.name.split(" ", 2);
        user.firstName = parts[0];
        user.lastName = parts.length > 1 ? parts[1] : "";
        database.save(user);
        return Response.ok(convertToV1(user));
    }

    @PutMapping("/v2/users/{id}")
    public Response updateUserV2(String id, @RequestBody UpdateUserV2Request req) {
        User user = database.getUser(id);
        user.firstName = req.firstName;
        user.lastName = req.lastName;
        database.save(user);
        return Response.ok(new UserV2(user));
    }
    ```

---

### Challenge 2: Broken Pagination

```java
/**
 * Pagination implementation with 3 BUGS.
 * Two are logic errors, one is a performance issue.
 */

@GetMapping("/users/{userId}/posts")
public PaginatedResponse getPosts(
    @PathParam("userId") String userId,
    @RequestParam("page") int page,    @RequestParam("limit") int limit) {
    // Calculate offset
    int offset = page * limit;
    List<Post> posts = database.query(
        "SELECT * FROM posts WHERE user_id = ? LIMIT ? OFFSET ?",
        userId, limit, offset
    );

    int total = database.query(
        "SELECT COUNT(*) FROM posts WHERE user_id = ?",
        userId
    );

    return new PaginatedResponse(posts, page, limit, total);
}
```

**Your debugging:**

**Bug 1:** <span class="fill-in">[What happens if client doesn't send page/limit parameters?]</span>

- Impact: <span class="fill-in">[What error occurs?]</span>
- Fix: <span class="fill-in">[What should be added?]</span>

**Bug 2:** <span class="fill-in">[What if client sends limit=999999?]</span>

- Impact: <span class="fill-in">[What problem does this cause?]</span>
- Fix: <span class="fill-in">[How to prevent abuse?]</span>

**Bug 3:** <span class="fill-in">[Is the offset calculation correct?]</span>

- Test: page=1, limit=10 → offset = <span class="fill-in">___</span>
- Expected: offset should be ___
- Fix: <span class="fill-in">[Correct formula?]</span>

??? success "Answer"

    **Bug 1:** Missing default values. If client doesn't send parameters, request fails with 400 Bad Request.

    **Fix:**

    ```java
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "20") int limit
    ```

    **Bug 2:** No limit validation. Malicious client could send `limit=999999` and DOS the database.

    **Fix:**

    ```java
    if (limit > 100) limit = 100;  // Cap at max
    if (limit < 1) limit = 20;     // Minimum 1
    ```

    **Bug 3:** Offset calculation is off by one. Page 1 should start at offset 0, not 10.

    **Correct calculation:**

    - Page 1, limit 10: offset = (1-1) * 10 = 0 (items 0-9)
    - Page 2, limit 10: offset = (2-1) * 10 = 10 (items 10-19)

    **Fix:**

    ```java
    int offset = (page - 1) * limit;
    ```

---

### Challenge 3: Poor Error Handling

```java
/**
 * Error handling that makes debugging IMPOSSIBLE.
 * Find all the problems!
 */

@PostMapping("/users")
public Response createUser(@RequestBody CreateUserRequest req) {
    try {
        // Validation
        if (req.email == null) {
            return Response.status(500).body("Error");        }

        if (!req.email.contains("@")) {
            return Response.status(200).body(null);        }

        // Check duplicate
        User existing = database.findByEmail(req.email);
        if (existing != null) {
            return Response.status(400).body("Error");        }

        // Create user
        User user = database.createUser(req);
        return Response.status(200).body(user);
    } catch (Exception e) {
        return Response.status(500).body("Something went wrong");
    }
}
```

**Your debugging:**

**Bug 1:** Missing email should return status ___ with message ___

**Bug 2:** Invalid email format should return status ___ with message ___

**Bug 3:** Duplicate email should return status ___ with message ___

- Current: 400 Bad Request
- Correct: <span class="fill-in">[Fill in]</span>
- Why: <span class="fill-in">[Explain the difference]</span>

**Bug 4:** Successful creation should return status ___

- Current: 200 OK
- Correct: <span class="fill-in">[Fill in]</span>
- Why: <span class="fill-in">[Explain]</span>

**Bug 5:** Generic catch block problems:

1. <span class="fill-in">[What's lost?]</span>
2. <span class="fill-in">[How to fix?]</span>
3. <span class="fill-in">[What should be logged?]</span>

??? success "Answer"

    **Status code fixes:**

    - Bug 1: 400 Bad Request (not 500) — client sent invalid data, not a server error
    - Bug 2: 400 Bad Request with a descriptive message (not 200 with null body)
    - Bug 3: **409 Conflict** (not 400) — resource conflict, not a generic bad request
    - Bug 4: **201 Created** (not 200) — a new resource was created
    - Bug 5: Always log exceptions with context; provide structured `ApiError` objects; differentiate database vs unexpected errors

---

### Challenge 4: Missing Idempotency

```java
/**
 * Payment processing without idempotency.
 * What could go wrong?
 */

@PostMapping("/payments")
public Response processPayment(@RequestBody PaymentRequest req) {
    // Create payment record
    Payment payment = new Payment();
    payment.userId = req.userId;
    payment.amount = req.amount;
    payment.status = "PENDING";
    database.save(payment);

    // Charge credit card
    try {
        creditCardService.charge(req.cardToken, req.amount);
        payment.status = "COMPLETED";
        database.save(payment);
        return Response.status(201).body(payment);
    } catch (PaymentFailedException e) {
        payment.status = "FAILED";
        database.save(payment);
        return Response.status(400).body("Payment failed");
    }
}
```

**Your debugging:**

**Scenario 1:** Client submits payment, network timeout before response

- Client doesn't know if payment succeeded
- Client retries request
- What happens? <span class="fill-in">[Fill in]</span>
- Impact: <span class="fill-in">[Fill in]</span>

**Scenario 2:** Request processed, card charged, but database save fails

- Card was charged: <span class="fill-in">[Yes/No]</span>
- Payment record exists: <span class="fill-in">[Yes/No]</span>
- User's money: <span class="fill-in">[What happened?]</span>

**Fixes needed:**

1. Add <span class="fill-in">[what field?]</span> to ensure idempotency
2. Check <span class="fill-in">[what?]</span> before processing
3. Handle <span class="fill-in">[what scenario?]</span> to prevent double charging

??? success "Answer"

    **Problems:**

    1. No idempotency = double charging on retry
    2. No transaction = money charged but no record
    3. No way to safely retry

    **Key fixes:**

    1. Add `Idempotency-Key` request header (unique UUID per payment attempt)
    2. Look up existing payment by idempotency key before processing; return cached result if found
    3. Wrap card charge and database save in a transaction — if the save fails, roll back so the charge is not recorded against a ghost record
    4. With idempotency key, safe retries return the original result without re-charging

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all versioning issues
- [ ] Fixed pagination bugs and understood why they matter
- [ ] Improved error handling with proper status codes and structured errors
- [ ] Implemented idempotency for critical operations

**Common API mistakes you discovered:**

1. <span class="fill-in">[List the patterns you noticed]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

**Production-critical issues:**

- Which bug could cause financial loss? <span class="fill-in">[Fill in]</span>
- Which bug would break clients immediately? <span class="fill-in">[Fill in]</span>
- Which bug would cause performance issues at scale? <span class="fill-in">[Fill in]</span>

---

## Before/After: Why Good API Design Matters

**Your task:** Compare poorly designed vs well-designed APIs to understand the impact.

### Example 1: API Versioning

**Problem:** API needs to change user field from `name` to `firstName` and `lastName`.

#### Approach 1: Breaking Change (Bad)

```java
// Version 1 (deployed to production)
class User {
    String id;
    String name;        // Single name field
    String email;
}

// Suddenly changed to Version 2 - BREAKS ALL CLIENTS!
class User {
    String id;
    String firstName;   // Broken change
    String lastName;    // Broken change
    String email;
}
```

All existing clients that reference `user.name` break immediately with no migration path.

#### Approach 2: Versioned API (Good)

```java
// Version 1 - Still supported
// GET /v1/users/123
class UserV1 {
    String id;
    String name;        // Still works for old clients
    String email;
}

// Version 2 - New clients can opt in
// GET /v2/users/123
class UserV2 {
    String id;
    String firstName;   // New field
    String lastName;    // New field
    String email;
}

// API routes both versions
@GetMapping("/v1/users/{id}")
public UserV1 getUserV1(String id) {
    User user = database.getUser(id);
    // Convert to V1 format
    return new UserV1(user.id,
                      user.firstName + " " + user.lastName,
                      user.email);
}

@GetMapping("/v2/users/{id}")
public UserV2 getUserV2(String id) {
    User user = database.getUser(id);
    return new UserV2(user.id, user.firstName, user.lastName, user.email);
}
```

Old clients hit `/v1/` and continue working. New clients opt into `/v2/`. A deprecation header (`Deprecation: true`, `Sunset: Dec 2024`) signals when to migrate.

**After implementing, answer:**

<div class="learner-section" markdown>

- Why is versioning critical for APIs? <span class="fill-in">[Your answer]</span>
- When would you create a new API version? <span class="fill-in">[Your answer]</span>
- How long should you support old versions? <span class="fill-in">[Your answer]</span>

</div>

!!! info "Loop back"
    Return to the Quick Quiz now and fill in your verified answers.

---

## Case Studies: API Design in the Wild

### Stripe API: The Gold Standard for REST

- **Paradigm:** REST.
- **How it works:** Stripe's API is a model for resource-oriented design. Resources are represented as nouns (e.g.,
  `/v1/customers`, `/v1/charges`, `/v1/subscriptions`). It uses HTTP verbs correctly (e.g., `POST /v1/charges` to create
  a new charge, `GET /v1/charges/{id}` to retrieve it). It also excels at developer experience with clear error
  messages, idempotent request handling, and versioning in the URL.
- **Key Takeaway:** For a public-facing API where predictability, scalability, and a wide range of client support are
  essential, a well-documented REST architecture is a powerful and reliable choice. It sets clear boundaries and is
  easily explorable.

### GitHub API: GraphQL for Flexibility

- **Paradigm:** GraphQL.
- **How it works:** GitHub's v4 API uses GraphQL to allow developers to craft precise queries for the exact data they
  need. Instead of making multiple REST calls to get a repository, its pull requests, and their review comments, a
  developer can write a single GraphQL query that specifies this nested structure.
- **Key Takeaway:** GraphQL is ideal for applications with complex data models and varied client needs (like mobile vs.
  web). It solves the over-fetching and under-fetching problems common in REST, but adds complexity to the server-side
  with query parsing and execution.

### Google & Netflix: gRPC for Internal Microservices

- **Paradigm:** RPC (specifically, gRPC).
- **How it works:** In a microservices architecture, services need to communicate with each other at very high speed.
  Google developed gRPC for this purpose. Services define their interfaces using Protocol Buffers (`.proto` files),
  which act as a strict contract. gRPC then generates client and server code, enabling efficient, low-latency, binary
  communication over HTTP/2.
- **Key Takeaway:** For internal service-to-service communication where performance is critical and contracts need to be
  strictly enforced, gRPC is often superior to REST. The focus is not on human-readable resources but on
  high-performance procedure calls.

---

## Common Misconceptions

!!! warning "REST requires JSON responses"
    REST is a *style*, not a format. A REST API can return XML, protobuf, plain text, or any media type — the `Content-Type` header tells the client what it received. JSON became the default because it is easy to consume from JavaScript, not because REST mandates it. `Accept: application/xml` is a perfectly valid REST request.

!!! warning "GraphQL automatically prevents the N+1 query problem"
    GraphQL's resolver model can actually *introduce* N+1 queries. If resolving `User.posts` executes one database query per user, fetching 100 users with their posts triggers 101 database queries. Tools like the DataLoader pattern are required to batch these into a single query. GraphQL does not solve N+1 by itself.

!!! warning "HTTP 404 means the server is broken"
    404 Not Found is a completely normal, expected response. It means the requested resource does not exist at that URL — nothing more. Clients should handle 404 gracefully. The distinction that matters in practice is: 404 (resource never existed or has been removed) vs 410 Gone (resource existed and was intentionally deleted) vs 503 (server is temporarily unavailable).

---

## Decision Framework

<div class="learner-section" markdown>

**Questions to answer after implementation:**

### 1. REST vs GraphQL vs RPC

**When to use REST?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use GraphQL?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

**When to use RPC?**

- Your scenario: <span class="fill-in">[Fill in]</span>
- Key factors: <span class="fill-in">[Fill in]</span>

### 2. Trade-offs

**REST:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

**GraphQL:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

**RPC:**

- Pros: <span class="fill-in">[Fill in after understanding]</span>
- Cons: <span class="fill-in">[Fill in after understanding]</span>

### 3. Your Decision Tree

Build your decision tree after practicing:
```mermaid
flowchart LR
    Start["What kind of API are you building?"]

    N1["?"]
    Start -->|"Public web API for third parties"| N1
    N2["?"]
    Start -->|"Mobile app backend"| N2
    N3["?"]
    Start -->|"Service-to-service communication"| N3
    N4["?"]
    Start -->|"Complex data fetching with relationships"| N4
```

</div>

---

## Practice

<div class="learner-section" markdown>

### Scenario 1: Design API for a blogging platform

**Requirements:**

- Users can create, edit, delete posts
- Users can comment on posts
- Users can follow other users
- Feed shows posts from followed users

**Your API design:**

- Which paradigm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- Key endpoints/queries: <span class="fill-in">[Fill in]</span>
- How to handle feed generation? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the feed generation service becomes unavailable and clients retry aggressively? <span class="fill-in">[Fill in]</span>
- How does your design behave when a post deletion must propagate across all cached feed responses? <span class="fill-in">[Fill in]</span>

### Scenario 2: Design API for microservices

**Requirements:**

- Order service needs to call Payment service
- Payment service needs to call Notification service
- Low latency required
- Services are within same data center

**Your API design:**

- Which paradigm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- How to handle errors? <span class="fill-in">[Fill in]</span>
- How to handle retries? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the Payment service is down when the Order service calls it mid-transaction? <span class="fill-in">[Fill in]</span>
- How does your design behave when a Notification service call times out and the retry triggers a duplicate notification? <span class="fill-in">[Fill in]</span>

### Scenario 3: Design API for mobile app with poor network

**Requirements:**

- Mobile app on slow 3G network
- Needs user profile, posts, and comments
- Different screens need different data
- Want to minimize requests

**Your API design:**

- Which paradigm would you choose? <span class="fill-in">[Fill in]</span>
- Why? <span class="fill-in">[Fill in]</span>
- How to optimize for mobile? <span class="fill-in">[Fill in]</span>
- Caching strategy? <span class="fill-in">[Fill in]</span>

**Failure modes:**

- What happens if the mobile client loses connectivity mid-request and retries with the same payload? <span class="fill-in">[Fill in]</span>
- How does your design behave when the server returns a partial response due to a timeout on a slow 3G connection? <span class="fill-in">[Fill in]</span>

</div>

---

## Test Your Understanding

Answer these without referring to your notes or implementation.

1. A REST `DELETE /users/123` is called twice. The first call succeeds and deletes the user. The second call finds no user. Should the second call return 200, 204, or 404? Justify your answer in terms of idempotency.

    ??? success "Rubric"
        A complete answer addresses: (1) both 204 and 404 are defensible — the choice depends on API contract; 404 is the more common production choice, (2) the key insight is that idempotency means the server state is identical after multiple identical requests, not that the status code must be identical, (3) returning 404 on the second call is acceptable because the resource is gone; documenting this behaviour in the API contract is the critical requirement.

2. A mobile client needs a user's profile, their last 5 posts, and the like count on each post — all on one screen. Compare the number of HTTP round trips required with REST versus GraphQL, and explain which wins here and why.

    ??? success "Rubric"
        A complete answer addresses: (1) REST requires at least 3 round trips: GET /users/{id}, GET /users/{id}/posts, then N GET /posts/{id}/likes calls (N+1 problem), (2) GraphQL requires 1 round trip with a nested query specifying exactly the fields needed, (3) on a slow mobile network the latency saving from 1 vs 3+ round trips outweighs GraphQL's added server complexity, making GraphQL the better choice here.

3. Your team is adding a `middleName` field to the User resource. Is this a breaking change? What about removing the `age` field? Explain the difference and what versioning strategy each requires.

    ??? success "Rubric"
        A complete answer addresses: (1) adding an optional field is non-breaking — existing clients that ignore unknown fields continue to work, so no new version is needed, (2) removing an existing field is breaking — any client that reads `age` will receive null or an error, requiring a new API version (e.g., `/v2/users`), (3) a sunset header on the old version gives clients a migration window before the old endpoint is decommissioned.

4. A payment endpoint uses `POST /payments` without an idempotency key. A client sends a request, receives a network timeout, and retries. Describe exactly what can go wrong and what the correct fix looks like.

    ??? success "Rubric"
        A complete answer addresses: (1) the server may have processed the first request and charged the card before the timeout, so the retry creates a second charge — double billing the customer, (2) the fix is an `Idempotency-Key` header (a client-generated UUID) stored by the server; if the same key arrives again, return the cached response without re-processing, (3) the idempotency key store must be durable so a server restart does not lose the record and allow re-processing.

5. A colleague says "We should use GraphQL for our internal microservice communication because it's more flexible than REST." What is the better choice and why, and what does GraphQL not provide that internal services actually need?

    ??? success "Rubric"
        A complete answer addresses: (1) gRPC (RPC with Protocol Buffers) is the better choice for internal microservice communication because it provides binary serialisation, HTTP/2 multiplexing, and generated typed clients, (2) GraphQL's flexibility is designed for external clients with varied data needs, not for internal services with known, stable interfaces, (3) GraphQL does not provide the strongly-typed generated contracts and low-latency binary protocol that internal services need for high-throughput inter-service calls.

---

## Your API Design Principles (Write Your Own)

Based on everything you've learned, write your personal API design checklist:

**My Top 5 API Design Principles:**

1. <span class="fill-in">[Fill in]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>
4. <span class="fill-in">[Fill in]</span>
5. <span class="fill-in">[Fill in]</span>

**My Top 3 API Anti-Patterns to Avoid:**

1. <span class="fill-in">[Fill in]</span>
2. <span class="fill-in">[Fill in]</span>
3. <span class="fill-in">[Fill in]</span>

**When I review an API, I check:**

- [ ] <span class="fill-in">[Your checklist item]</span>
- [ ] <span class="fill-in">[Your checklist item]</span>
- [ ] <span class="fill-in">[Your checklist item]</span>
- [ ] <span class="fill-in">[Your checklist item]</span>
- [ ] <span class="fill-in">[Your checklist item]</span>

---

## Connected Topics

!!! info "Where this topic connects"

    - **07. Security Patterns** — all external APIs require authentication (JWT or API keys) and authorization (RBAC); API design choices affect token scope and validation overhead → [07. Security Patterns](07-security-patterns.md)
    - **08. Rate Limiting** — rate limiting protects API endpoints from abuse; API versioning strategy affects whether limits are applied per-version or globally → [08. Rate Limiting](08-rate-limiting.md)
    - **03. Networking Fundamentals** — HTTP versions covered in topic 03 determine which API features are possible (e.g. HTTP/2 enables server push and header compression) → [03. Networking Fundamentals](03-networking-fundamentals.md)
