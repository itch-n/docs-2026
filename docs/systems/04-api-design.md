# 04. API Design

> REST, GraphQL, and RPC - Choosing the right API paradigm

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing different API patterns, explain them simply.

**Prompts to guide you:**

1. **What is REST in one sentence?**
   - Your answer: _[Fill in after implementation]_

2. **Why do we use REST for web APIs?**
   - Your answer: _[Fill in after implementation]_

3. **Real-world analogy for REST:**
   - Example: "REST is like a restaurant menu where..."
   - Your analogy: _[Fill in]_

4. **What is GraphQL in one sentence?**
   - Your answer: _[Fill in after implementation]_

5. **When would you choose GraphQL over REST?**
   - Your answer: _[Fill in after implementation]_

6. **Real-world analogy for GraphQL:**
   - Example: "GraphQL is like a buffet where..."
   - Your analogy: _[Fill in]_

7. **What is RPC (gRPC) in one sentence?**
   - Your answer: _[Fill in after implementation]_

8. **When would you use RPC instead of REST?**
   - Your answer: _[Fill in after implementation]_

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

        // TODO: If found, return 200 with user data
        // Include links: { "posts": "/users/{id}/posts" }

        // TODO: If not found, return 404

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

        // TODO: For each requested field:
        //   - Resolve scalar fields (id, name, email)
        //   - Resolve nested objects (posts, author)

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

## Decision Framework

**Questions to answer after implementation:**

### 1. REST vs GraphQL vs RPC

**When to use REST?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use GraphQL?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

**When to use RPC?**
- Your scenario: _[Fill in]_
- Key factors: _[Fill in]_

### 2. Trade-offs

**REST:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**GraphQL:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

**RPC:**
- Pros: _[Fill in after understanding]_
- Cons: _[Fill in after understanding]_

### 3. Your Decision Tree

Build your decision tree after practicing:

```
What kind of API are you building?
├─ Public web API for third parties → ?
├─ Mobile app backend → ?
├─ Service-to-service communication → ?
└─ Complex data fetching with relationships → ?
```

### 4. Kill Switch - Don't use when:

**REST:**
1. _[When does REST fail? Fill in]_
2. _[Another failure case]_

**GraphQL:**
1. _[When does GraphQL fail? Fill in]_
2. _[Another failure case]_

**RPC:**
1. _[When does RPC fail? Fill in]_
2. _[Another failure case]_

### 5. Rule of Three - Alternatives

For each API paradigm, identify alternatives and compare:

**Scenario: Building a mobile app backend**
1. Option A: _[Fill in]_
2. Option B: _[Fill in]_
3. Option C: _[Fill in]_

---

## Practice

### Scenario 1: Design API for a blogging platform

**Requirements:**
- Users can create, edit, delete posts
- Users can comment on posts
- Users can follow other users
- Feed shows posts from followed users

**Your API design:**
- Which paradigm would you choose? _[Fill in]_
- Why? _[Fill in]_
- Key endpoints/queries: _[Fill in]_
- How to handle feed generation? _[Fill in]_

### Scenario 2: Design API for microservices

**Requirements:**
- Order service needs to call Payment service
- Payment service needs to call Notification service
- Low latency required
- Services are within same data center

**Your API design:**
- Which paradigm would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to handle errors? _[Fill in]_
- How to handle retries? _[Fill in]_

### Scenario 3: Design API for mobile app with poor network

**Requirements:**
- Mobile app on slow 3G network
- Needs user profile, posts, and comments
- Different screens need different data
- Want to minimize requests

**Your API design:**
- Which paradigm would you choose? _[Fill in]_
- Why? _[Fill in]_
- How to optimize for mobile? _[Fill in]_
- Caching strategy? _[Fill in]_

---

## Review Checklist

- [ ] REST API implemented with proper resource design
- [ ] GraphQL resolver implemented with field resolution
- [ ] RPC service implemented with method calls
- [ ] Understand when to use each paradigm
- [ ] Can explain trade-offs between approaches
- [ ] Built decision tree for API selection
- [ ] Completed practice scenarios

---

**Next:** [05. Security Patterns →](05-security-patterns.md)

**Back:** [03. Caching Patterns ←](03-caching-patterns.md)
