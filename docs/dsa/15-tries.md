# 14. Tries (Prefix Trees)

> Efficient storage and retrieval of strings with common prefixes using tree structure

---

## ELI5: Explain Like I'm 5

**Your task:** After implementing all patterns, explain them simply.

**Prompts to guide you:**

1. **What is a trie in one sentence?**
    - Your answer: _[Fill in after implementation]_

2. **How is a trie different from a hash table for strings?**
    - Your answer: _[Fill in after implementation]_

3. **Real-world analogy:**
    - Example: "A trie is like how you organize words in a dictionary by first letter, then second letter..."
    - Your analogy: _[Fill in]_

4. **When does this pattern work?**
    - Your answer: _[Fill in after solving problems]_

5. **What's the space-time tradeoff with tries?**
    - Your answer: _[Fill in after implementation]_

---

## Quick Quiz (Do BEFORE implementing)

**Your task:** Test your intuition without looking at code. Answer these, then verify after implementation.

### Complexity Predictions

1. **HashMap search for word in dictionary of n words:**
    - Time complexity: _[Your guess: O(?)]_
    - Verified after learning: _[Actual: O(?)]_

2. **Trie search for word of length m in dictionary of n words:**
    - Time complexity: _[Your guess: O(?)]_
    - Space complexity for entire trie: _[Your guess: O(?)]_
    - Verified: _[Actual]_

3. **Prefix search comparison:**
    - Find all words starting with "app" using HashMap: _[Time: O(?)]_
    - Find all words starting with "app" using Trie: _[Time: O(?)]_
    - Which is faster: _[HashMap/Trie - Why?]_

### Scenario Predictions

**Scenario 1:** Dictionary has ["apple", "app", "application", "apply"]

- **How many nodes in the trie?** _[Count them]_
- **Which nodes have isEndOfWord = true?** _[List them]_
- **How many nodes share the prefix "app"?** _[Count]_
- **Memory saved compared to storing each word separately?** _[Estimate]_

**Scenario 2:** Search for "appl" in above dictionary

- **Using search(): returns** _[true/false - Why?]_
- **Using startsWith(): returns** _[true/false - Why?]_
- **What's the difference?** _[Fill in your reasoning]_

**Scenario 3:** Autocomplete with prefix "ap"

- **Which words match?** _[List them]_
- **Path through trie:** _[Describe the traversal]_
- **Why is trie better than checking each word?** _[Fill in]_

### Trade-off Quiz

**Question:** When would HashMap be BETTER than Trie for dictionary?

- Your answer: _[Fill in before implementation]_
- Verified answer: _[Fill in after learning]_

**Question:** What's the MAIN advantage of trie over hash table?

- [ ] Faster single word lookup
- [ ] Less space usage
- [ ] Efficient prefix queries
- [ ] Simpler to implement

Verify after implementation: _[Which one(s)?]_

**Question:** What's the space complexity of trie with n words of average length m?

- [ ] O(n)
- [ ] O(m)
- [ ] O(n * m) worst case
- [ ] O(1)

Verify: _[Which one and why?]_

---

## Before/After: Why This Pattern Matters

**Your task:** Compare naive vs optimized approaches to understand the impact.

### Example: Autocomplete System

**Problem:** Find all words in dictionary that start with given prefix.

#### Approach 1: HashMap with Linear Scan

```java
// Naive approach - Check every word
public static List<String> autocomplete_Naive(Set<String> dictionary, String prefix) {
    List<String> results = new ArrayList<>();

    // Check every word in dictionary
    for (String word : dictionary) {
        if (word.startsWith(prefix)) {
            results.add(word);
        }
    }

    return results;
}
```

**Analysis:**

- Time: O(n * m) - Check n words, each comparison takes m characters
- Space: O(1) additional space (not counting results)
- For dictionary with 100,000 words, prefix length 3: ~300,000 character comparisons

#### Approach 2: Trie (Optimized)

```java
// Optimized approach - Navigate to prefix then collect
public List<String> autocomplete_Trie(String prefix) {
    List<String> results = new ArrayList<>();

    // Navigate to prefix node: O(p) where p = prefix length
    TrieNode node = root;
    for (char c : prefix.toCharArray()) {
        if (!node.children.containsKey(c)) {
            return results; // Prefix not found
        }
        node = node.children.get(c);
    }

    // Collect all words under this node: O(k) where k = results
    collectWords(node, prefix, results);

    return results;
}
```

**Analysis:**

- Time: O(p + k) - p = prefix length, k = number of results
- Space: O(n * m) for trie structure
- For same dictionary, prefix length 3: Only 3 comparisons + collecting results

#### Performance Comparison

| Dictionary Size | Prefix Length | HashMap (O(n*m)) | Trie (O(p+k)) | Speedup |
|-----------------|---------------|------------------|----------------|---------|
| n = 1,000       | m = 5         | 5,000 ops       | 5 + k ops      | 1000x   |
| n = 10,000      | m = 5         | 50,000 ops      | 5 + k ops      | 10000x  |
| n = 100,000     | m = 5         | 500,000 ops     | 5 + k ops      | 100000x |

**Your calculation:** For n = 50,000 and prefix length 4, the speedup is approximately _____ times faster.

#### Why Does Trie Work?

**Key insight to understand:**

Building dictionary: ["apple", "app", "application", "apply", "banana"]

```
Trie structure:
        root
       /    \
      a      b
      |      |
      p      a
      |      |
      p*     n
     / \     |
    l   l    a
    |   |    |
    e*  y*   n
    |        |
    a        a*
    |
    t
    |
    i
    |
    o
    |
    n*

(* = isEndOfWord)
```

**Autocomplete for "app":**

```
Step 1: Navigate to 'a' → 'p' → 'p' (3 steps only!)
Step 2: Collect all words from this subtree
Found: "app", "apple", "application", "apply"
```

**Why can we skip other words?**

- Words not starting with 'a' are in different branch (never visited)
- Words starting with 'a' but not 'ap' are in different branch (never visited)
- Only visit nodes relevant to prefix - massive pruning!

**After implementing, explain in your own words:**

- _[Why is trie better for prefix queries than hash table?]_
- _[What's the space-time tradeoff?]_
- _[When would hash table still be better?]_

---

### Example: Word Search in Grid

**Problem:** Find all words from dictionary in 2D character grid.

#### Approach 1: Brute Force with Hash Set

```java
// Naive approach - DFS from each cell for each word
public static List<String> findWords_BruteForce(char[][] board, String[] words) {
    Set<String> wordSet = new HashSet<>(Arrays.asList(words));
    Set<String> found = new HashSet<>();

    // Try to find each word starting from each cell
    for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[0].length; j++) {
            for (String word : words) {
                if (dfsSearch(board, i, j, word, 0, new boolean[board.length][board[0].length])) {
                    found.add(word);
                }
            }
        }
    }

    return new ArrayList<>(found);
}
```

**Analysis:**

- Time: O(w * m * n * 4^L) - w words, m*n cells, 4^L DFS
- For 1000 words, 4x4 grid, word length 10: Extremely slow
- No early pruning - explores impossible paths

#### Approach 2: Trie with DFS (Optimized)

```java
// Optimized approach - Single DFS using trie for pruning
public List<String> findWords_Trie(char[][] board, String[] words) {
    // Build trie once: O(w * L)
    TrieNode root = buildTrie(words);
    Set<String> found = new HashSet<>();

    // DFS from each cell, trie prunes invalid paths
    for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[0].length; j++) {
            dfs(board, i, j, root, found, new boolean[board.length][board[0].length]);
        }
    }

    return new ArrayList<>(found);
}

// Trie prunes branches early - if prefix doesn't exist in trie, stop!
```

**Analysis:**

- Time: O(m * n * 4^L) - Only one DFS per cell
- Trie pruning: If "xy" not in dictionary, never explore "xyz", "xya", etc.
- For same problem: 1000x faster with early pruning

#### Performance Comparison

| Dictionary Size | Grid Size | Brute Force | Trie with Pruning | Speedup |
|-----------------|-----------|-------------|-------------------|---------|
| 100 words       | 3x3       | ~100,000 ops| ~1,000 ops        | 100x    |
| 1,000 words     | 4x4       | ~10,000,000 | ~10,000 ops       | 1000x   |
| 10,000 words    | 5x5       | Timeout     | ~100,000 ops      | 100x+   |

**Key insight:** Trie eliminates redundant work by sharing prefixes and enabling early stopping.

**After implementing, explain in your own words:**

- _[How does trie enable early pruning in DFS?]_
- _[Why is single DFS with trie better than multiple DFS?]_

---

## Core Implementation

### Pattern 1: Basic Trie Operations

**Concept:** Build tree where each node represents a character.

**Use case:** Insert, search, prefix search, delete words.

```java
public class BasicTrie {

    /**
     * TrieNode: Basic node structure
     */
    static class TrieNode {
        // TODO: Create children array/map for next characters
        // TODO: Boolean flag for end of word

        TrieNode[] children;
        boolean isEndOfWord;

        TrieNode() {
            // TODO: Initialize children array (size 26 for a-z)
            // TODO: Set isEndOfWord = false
        }
    }

    static class Trie {
        private TrieNode root;

        public Trie() {
            // TODO: Initialize root node
        }

        /**
         * Insert word into trie
         * Time: O(m) where m = word length, Space: O(m)
         *
         * TODO: Implement insert
         */
        public void insert(String word) {
            // TODO: Start from root
            // TODO: For each character:
            //   Calculate index (char - 'a')
            //   If child doesn't exist, create new node
            //   Move to child node
            // TODO: Mark last node as end of word
        }

        /**
         * Search if word exists in trie
         * Time: O(m), Space: O(1)
         *
         * TODO: Implement search
         */
        public boolean search(String word) {
            // TODO: Traverse trie following characters
            // TODO: Return true only if:
            //   - All characters found
            //   - Last node is marked as end of word
            return false; // Replace with implementation
        }

        /**
         * Check if any word starts with prefix
         * Time: O(m), Space: O(1)
         *
         * TODO: Implement startsWith
         */
        public boolean startsWith(String prefix) {
            // TODO: Similar to search
            // TODO: Return true if all characters found
            //   (don't check isEndOfWord)
            return false; // Replace with implementation
        }

        /**
         * Delete word from trie
         * Time: O(m), Space: O(m) for recursion
         *
         * TODO: Implement delete
         */
        public boolean delete(String word) {
            // TODO: Use recursive helper
            // TODO: Only delete nodes that aren't part of other words
            return false; // Replace with implementation
        }

        private boolean deleteHelper(TrieNode node, String word, int index) {
            // TODO: Base case: reached end of word
            //   If not end of word, return false
            //   Mark isEndOfWord = false
            //   Return true if node has no children (can be deleted)

            // TODO: Recursive case:
            //   Get child for current character
            //   If child null, word doesn't exist
            //   Recursively delete from child
            //   If child should be deleted and has no other children,
            //   remove child and return true

            return false; // Replace with implementation
        }
    }
}
```

**Runnable Client Code:**

```java
public class BasicTrieClient {

    public static void main(String[] args) {
        System.out.println("=== Basic Trie Operations ===\n");

        BasicTrie.Trie trie = new BasicTrie.Trie();

        // Test 1: Insert and search
        System.out.println("--- Test 1: Insert and Search ---");
        String[] words = {"apple", "app", "application", "apply", "banana"};

        System.out.println("Inserting words:");
        for (String word : words) {
            trie.insert(word);
            System.out.println("  Inserted: " + word);
        }

        System.out.println("\nSearching:");
        String[] searchWords = {"apple", "app", "appl", "ban", "banana"};
        for (String word : searchWords) {
            boolean found = trie.search(word);
            System.out.printf("  search(\"%s\"): %s%n", word, found ? "FOUND" : "NOT FOUND");
        }

        // Test 2: Prefix search
        System.out.println("\n--- Test 2: Prefix Search ---");
        String[] prefixes = {"app", "ban", "cat"};
        for (String prefix : prefixes) {
            boolean hasPrefix = trie.startsWith(prefix);
            System.out.printf("  startsWith(\"%s\"): %s%n", prefix, hasPrefix ? "YES" : "NO");
        }

        // Test 3: Delete
        System.out.println("\n--- Test 3: Delete ---");
        String deleteWord = "app";
        System.out.println("Deleting: " + deleteWord);
        trie.delete(deleteWord);

        System.out.println("After deletion:");
        for (String word : new String[]{"app", "apple", "application"}) {
            boolean found = trie.search(word);
            System.out.printf("  search(\"%s\"): %s%n", word, found ? "FOUND" : "NOT FOUND");
        }
    }
}
```

---

### Pattern 2: Word Search in Grid with Trie

**Concept:** Use trie to efficiently search multiple words in 2D grid.

**Use case:** Word Search II, Boggle game solver.

```java
import java.util.*;

public class WordSearchTrie {

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        String word; // Store word at end node for easy retrieval
    }

    /**
     * Problem: Find all words from dictionary in 2D grid
     * Time: O(m*n*4^L) where L = max word length, Space: O(k*L) where k = words
     *
     * TODO: Implement word search II
     */
    public static List<String> findWords(char[][] board, String[] words) {
        // TODO: Build trie from words array
        // TODO: DFS from each cell using trie for pruning
        // TODO: Mark visited cells during DFS
        // TODO: Add found words to result (use Set to avoid duplicates)

        return new ArrayList<>(); // Replace with implementation
    }

    private static TrieNode buildTrie(String[] words) {
        // TODO: Create root node
        // TODO: Insert each word into trie
        // TODO: Store complete word at end node
        return new TrieNode(); // Replace with implementation
    }

    private static void dfs(char[][] board, int i, int j, TrieNode node,
                           Set<String> result, boolean[][] visited) {
        // TODO: Check bounds and visited
        // TODO: Get character at current position
        // TODO: Check if character exists in trie children
        // TODO: If node has word, add to result
        // TODO: Mark visited
        // TODO: DFS in 4 directions
        // TODO: Unmark visited (backtrack)
    }

    /**
     * Problem: Longest word in dictionary built one char at a time
     * Time: O(n * L), Space: O(n * L)
     *
     * TODO: Implement longest word
     */
    public static String longestWord(String[] words) {
        // TODO: Build trie
        // TODO: DFS/BFS to find longest word where all prefixes exist
        // TODO: Each character must form a valid word

        return ""; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class WordSearchTrieClient {

    public static void main(String[] args) {
        System.out.println("=== Word Search with Trie ===\n");

        // Test 1: Word Search II
        System.out.println("--- Test 1: Word Search II ---");
        char[][] board = {
            {'o','a','a','n'},
            {'e','t','a','e'},
            {'i','h','k','r'},
            {'i','f','l','v'}
        };

        String[] words = {"oath", "pea", "eat", "rain", "oat"};

        System.out.println("Board:");
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }

        System.out.println("\nDictionary: " + Arrays.toString(words));

        List<String> found = WordSearchTrie.findWords(board, words);
        System.out.println("Found words: " + found);

        // Test 2: Longest word
        System.out.println("\n--- Test 2: Longest Word ---");
        String[][] wordSets = {
            {"w", "wo", "wor", "worl", "world"},
            {"a", "banana", "app", "appl", "ap", "apply", "apple"}
        };

        for (String[] wordSet : wordSets) {
            String longest = WordSearchTrie.longestWord(wordSet);
            System.out.printf("Words: %s%n", Arrays.toString(wordSet));
            System.out.printf("Longest: \"%s\"%n%n", longest);
        }
    }
}
```

---

### Pattern 3: Autocomplete and Prefix Matching

**Concept:** Find all words with given prefix.

**Use case:** Autocomplete, suggestions, prefix search.

```java
import java.util.*;

public class Autocomplete {

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
        String word; // Store full word for easy retrieval
    }

    static class AutocompleteTrie {
        private TrieNode root;

        public AutocompleteTrie() {
            root = new TrieNode();
        }

        /**
         * Insert word
         * Time: O(m), Space: O(m)
         *
         * TODO: Implement insert
         */
        public void insert(String word) {
            // TODO: Standard trie insert
            // TODO: Store word at end node
        }

        /**
         * Find all words with given prefix
         * Time: O(p + n) where p=prefix length, n=results, Space: O(n)
         *
         * TODO: Implement autocomplete
         */
        public List<String> autocomplete(String prefix) {
            List<String> results = new ArrayList<>();

            // TODO: Navigate to end of prefix
            // TODO: If prefix not found, return empty list
            // TODO: DFS from prefix node to collect all words
            // TODO: Return results

            return results; // Replace with implementation
        }

        private void collectWords(TrieNode node, List<String> results) {
            // TODO: If node is end of word, add to results
            // TODO: DFS all children
        }

        /**
         * Find top K most frequent words with prefix
         * Time: O(p + n log k), Space: O(n)
         *
         * TODO: Implement top K suggestions
         */
        public List<String> topKSuggestions(String prefix, int k) {
            // TODO: Get all words with prefix
            // TODO: Use min-heap to track top K by frequency
            // TODO: Return top K

            return new ArrayList<>(); // Replace with implementation
        }
    }

    /**
     * Problem: Search suggestions system
     * Time: O(m*n) where m=products, n=searchWord length, Space: O(m*n)
     *
     * TODO: Implement search suggestions
     */
    public static List<List<String>> suggestedProducts(String[] products, String searchWord) {
        // TODO: Build trie from products
        // TODO: For each prefix of searchWord:
        //   Find up to 3 lexicographically smallest products
        // TODO: Return list of suggestions for each prefix

        return new ArrayList<>(); // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class AutocompleteClient {

    public static void main(String[] args) {
        System.out.println("=== Autocomplete ===\n");

        // Test 1: Basic autocomplete
        System.out.println("--- Test 1: Basic Autocomplete ---");
        Autocomplete.AutocompleteTrie trie = new Autocomplete.AutocompleteTrie();

        String[] dictionary = {
            "apple", "application", "apply", "app",
            "banana", "band", "bandana", "ban"
        };

        System.out.println("Dictionary:");
        for (String word : dictionary) {
            trie.insert(word);
            System.out.println("  " + word);
        }

        String[] prefixes = {"app", "ban", "cat"};
        System.out.println("\nAutocomplete:");
        for (String prefix : prefixes) {
            List<String> suggestions = trie.autocomplete(prefix);
            System.out.printf("  \"%s\" -> %s%n", prefix, suggestions);
        }

        // Test 2: Search suggestions system
        System.out.println("\n--- Test 2: Search Suggestions System ---");
        String[] products = {"mobile", "mouse", "moneypot", "monitor", "mousepad"};
        String searchWord = "mouse";

        System.out.println("Products: " + Arrays.toString(products));
        System.out.println("Search word: " + searchWord);

        List<List<String>> suggestions = Autocomplete.suggestedProducts(products, searchWord);
        System.out.println("Suggestions for each prefix:");
        for (int i = 0; i < suggestions.size(); i++) {
            System.out.printf("  \"%s\" -> %s%n",
                searchWord.substring(0, i + 1), suggestions.get(i));
        }
    }
}
```

---

### Pattern 4: Advanced Trie Applications

**Concept:** Use tries for complex string operations.

**Use case:** Longest common prefix, map sum pairs, replace words.

```java
import java.util.*;

public class AdvancedTrie {

    /**
     * Problem: Longest common prefix of all strings
     * Time: O(S) where S = sum of all characters, Space: O(S)
     *
     * TODO: Implement using trie
     */
    public static String longestCommonPrefix(String[] strs) {
        // TODO: Build trie from all strings
        // TODO: Traverse trie while:
        //   - Only one child exists
        //   - Not end of word
        // TODO: Return prefix

        return ""; // Replace with implementation
    }

    /**
     * MapSum: Sum of values with given prefix
     *
     * TODO: Implement map sum trie
     */
    static class MapSum {
        // TODO: TrieNode with value at each node
        // TODO: Store running sum at each node

        static class TrieNode {
            Map<Character, TrieNode> children = new HashMap<>();
            int value; // Value of word ending here
            int sum; // Sum of all words with this prefix
        }

        private TrieNode root;

        public MapSum() {
            // TODO: Initialize root
        }

        /**
         * Insert key with value
         * Time: O(m), Space: O(m)
         */
        public void insert(String key, int val) {
            // TODO: Navigate/create path for key
            // TODO: Update sum at each node
            // TODO: Store value at end node
        }

        /**
         * Sum of all values with given prefix
         * Time: O(m), Space: O(1)
         */
        public int sum(String prefix) {
            // TODO: Navigate to end of prefix
            // TODO: Return sum at that node
            return 0; // Replace with implementation
        }
    }

    /**
     * Problem: Replace words with shortest root from dictionary
     * Time: O(N + S), Space: O(N)
     *
     * TODO: Implement replace words
     */
    public static String replaceWords(List<String> dictionary, String sentence) {
        // TODO: Build trie from dictionary
        // TODO: For each word in sentence:
        //   Find shortest prefix that's in trie
        //   Replace word with prefix if found
        // TODO: Return modified sentence

        return ""; // Replace with implementation
    }
}
```

**Runnable Client Code:**

```java
import java.util.*;

public class AdvancedTrieClient {

    public static void main(String[] args) {
        System.out.println("=== Advanced Trie ===\n");

        // Test 1: Longest common prefix
        System.out.println("--- Test 1: Longest Common Prefix ---");
        String[][] stringSets = {
            {"flower", "flow", "flight"},
            {"dog", "racecar", "car"},
            {"interview", "internet", "interval"}
        };

        for (String[] strs : stringSets) {
            String lcp = AdvancedTrie.longestCommonPrefix(strs);
            System.out.printf("Strings: %s%n", Arrays.toString(strs));
            System.out.printf("LCP: \"%s\"%n%n", lcp);
        }

        // Test 2: Map sum
        System.out.println("--- Test 2: Map Sum ---");
        AdvancedTrie.MapSum mapSum = new AdvancedTrie.MapSum();

        mapSum.insert("apple", 3);
        System.out.println("insert(\"apple\", 3)");
        System.out.println("sum(\"ap\"): " + mapSum.sum("ap"));

        mapSum.insert("app", 2);
        System.out.println("insert(\"app\", 2)");
        System.out.println("sum(\"ap\"): " + mapSum.sum("ap"));

        // Test 3: Replace words
        System.out.println("\n--- Test 3: Replace Words ---");
        List<String> dictionary = Arrays.asList("cat", "bat", "rat");
        String sentence = "the cattle was rattled by the battery";

        System.out.println("Dictionary: " + dictionary);
        System.out.println("Sentence: " + sentence);

        String replaced = AdvancedTrie.replaceWords(dictionary, sentence);
        System.out.println("Result: " + replaced);
    }
}
```

---

## Debugging Challenges

**Your task:** Find and fix bugs in broken implementations. This tests your understanding.

### Challenge 1: Broken Insert

```java
/**
 * This insert method is supposed to add words to the trie.
 * It has 2 BUGS. Find them!
 */
public void insert_Buggy(String word) {
    TrieNode current = root;

    for (char c : word.toCharArray()) {
        int index = c - 'a';

        if (current.children[index] == null) {
            // BUG 1: What's missing here?
        }

        current = current.children[index];
    }

    // BUG 2: Missing something critical - what happens after loop?
}
```

**Your debugging:**

- **Bug 1 location:** _[Which line?]_
- **Bug 1 explanation:** _[What's wrong?]_
- **Bug 1 fix:** _[What code is missing?]_

- **Bug 2 location:** _[After the loop]_
- **Bug 2 explanation:** _[What's missing?]_
- **Bug 2 fix:** _[What should be added?]_

**Test case to expose the bug:**

- Insert: "app" and "apple"
- Search for "app": Expected true, Actual: _[Trace through manually]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1:** Missing node creation! Should be:
```java
if (current.children[index] == null) {
    current.children[index] = new TrieNode();
}
```

**Bug 2:** Missing `current.isEndOfWord = true;` after the loop. Without this, search will fail even though word was inserted.

**Result:** Without bug fixes, search("app") would return false because isEndOfWord is never set to true.
</details>

---

### Challenge 2: Broken Search

```java
/**
 * Search if word exists in trie.
 * This has 1 CRITICAL BUG.
 */
public boolean search_Buggy(String word) {
    TrieNode current = root;

    for (char c : word.toCharArray()) {
        int index = c - 'a';

        if (current.children[index] == null) {
            return false;
        }

        current = current.children[index];
    }

    return true;  // BUG: What's wrong with this return?
}
```

**Your debugging:**

- **Bug location:** _[Which line?]_
- **Bug explanation:** _[What's the problem?]_
- **Bug fix:** _[What should it return?]_

**Test case to expose the bug:**

- Dictionary: ["apple", "application"]
- Search for "app": Expected false, Actual: _[What happens?]_
- Search for "apple": Expected true, Actual: _[What happens?]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Should return `current.isEndOfWord`, not just `true`.

**Correct:**
```java
return current.isEndOfWord;
```

**Why:** We traversed all characters successfully, but that only means the prefix exists. We must check if it's marked as an actual word ending.

**Result:** Without fix, search("app") returns true even though "app" wasn't inserted (only "apple" was).
</details>

---

### Challenge 3: Broken Autocomplete

```java
/**
 * Find all words with given prefix.
 * This has 2 BUGS causing wrong results.
 */
public List<String> autocomplete_Buggy(String prefix) {
    List<String> results = new ArrayList<>();
    TrieNode current = root;

    // Navigate to prefix
    for (char c : prefix.toCharArray()) {
        int index = c - 'a';
        if (current.children[index] == null) {
            return results;
        }
        current = current.children[index];
    }

    // BUG 1: Missing check - what if prefix itself is a word?

    // Collect all words
    collectWords(current, prefix, results);
    return results;
}

private void collectWords(TrieNode node, String currentWord, List<String> results) {
    // BUG 2: Wrong order - when should we add the word?
    for (int i = 0; i < 26; i++) {
        if (node.children[i] != null) {
            collectWords(node.children[i], currentWord + (char)('a' + i), results);
        }
    }

    if (node.isEndOfWord) {
        results.add(currentWord);
    }
}
```

**Your debugging:**

- **Bug 1:** _[What check is missing?]_
- **Bug 1 fix:** _[What code to add?]_

- **Bug 2:** _[What's wrong with the order?]_
- **Bug 2 fix:** _[Where should the check go?]_

**Test case:**

- Dictionary: ["app", "apple", "application"]
- Autocomplete("app"): Expected ["app", "apple", "application"]
- Actual: _[Trace through - what's wrong?]_

<details markdown>
<summary>Click to verify your answers</summary>

**Bug 1:** Missing check if prefix itself is a word. After navigating to prefix, add:
```java
if (current.isEndOfWord) {
    results.add(prefix);
}
```

**Bug 2:** The isEndOfWord check should come BEFORE recursing to children, not after. This ensures parent words are added before children (proper DFS order).

**Correct order:**
```java
private void collectWords(TrieNode node, String currentWord, List<String> results) {
    if (node.isEndOfWord) {
        results.add(currentWord);  // Add current word first
    }

    for (int i = 0; i < 26; i++) {
        if (node.children[i] != null) {
            collectWords(node.children[i], currentWord + (char)('a' + i), results);
        }
    }
}
```
</details>

---

### Challenge 4: Memory Leak in Delete

```java
/**
 * Delete word from trie.
 * This has 1 SUBTLE BUG causing memory waste.
 */
public boolean delete_Buggy(String word) {
    return deleteHelper(root, word, 0);
}

private boolean deleteHelper(TrieNode node, String word, int index) {
    if (index == word.length()) {
        if (!node.isEndOfWord) {
            return false;  // Word not in trie
        }

        node.isEndOfWord = false;
        return true;  // BUG: When should we actually delete the node?
    }

    char c = word.charAt(index);
    int idx = c - 'a';
    TrieNode child = node.children[idx];

    if (child == null) {
        return false;  // Word not in trie
    }

    boolean shouldDeleteChild = deleteHelper(child, word, index + 1);

    if (shouldDeleteChild) {
        node.children[idx] = null;  // Delete child
        // BUG: Should we delete current node too? When?
    }

    return false;  // Don't delete this node
}
```

**Your debugging:**

- **Bug 1:** _[What's the logic error?]_
- **Bug 2:** _[When should we delete a node?]_
- **Bug 3:** _[What should the final return be?]_

**Test case:**

- Dictionary: ["app", "apple"]
- Delete "apple"
- Expected: "app" still works, nodes for "le" are removed
- Actual: _[What happens? Trace through]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** The logic for when to delete nodes is wrong. We should only delete a node if:
1. It's not an end of word (after marking false)
2. It has no children

**Correct implementation:**
```java
private boolean deleteHelper(TrieNode node, String word, int index) {
    if (index == word.length()) {
        if (!node.isEndOfWord) {
            return false;
        }

        node.isEndOfWord = false;

        // Can delete if no children
        return isEmpty(node);
    }

    char c = word.charAt(index);
    int idx = c - 'a';
    TrieNode child = node.children[idx];

    if (child == null) {
        return false;
    }

    boolean shouldDeleteChild = deleteHelper(child, word, index + 1);

    if (shouldDeleteChild) {
        node.children[idx] = null;

        // Delete this node if it's not end of word and has no children
        return !node.isEndOfWord && isEmpty(node);
    }

    return false;
}

private boolean isEmpty(TrieNode node) {
    for (int i = 0; i < 26; i++) {
        if (node.children[i] != null) {
            return false;
        }
    }
    return true;
}
```

**Why:** Without proper cleanup, deleted word's unique nodes remain in memory even though they're not part of any word.
</details>

---

### Challenge 5: Off-by-One in Word Search

```java
/**
 * DFS for word search in grid using trie.
 * This has 1 CRITICAL BUG causing wrong results.
 */
private void dfs_Buggy(char[][] board, int i, int j, TrieNode node,
                      Set<String> result, boolean[][] visited) {
    // BUG: Missing boundary and visited checks here!

    char c = board[i][j];

    if (!node.children.containsKey(c)) {
        return;  // Character not in trie
    }

    visited[i][j] = true;
    TrieNode next = node.children.get(c);

    if (next.word != null) {
        result.add(next.word);
    }

    // Explore 4 directions
    dfs_Buggy(board, i + 1, j, next, result, visited);
    dfs_Buggy(board, i - 1, j, next, result, visited);
    dfs_Buggy(board, i, j + 1, next, result, visited);
    dfs_Buggy(board, i, j - 1, next, result, visited);

    visited[i][j] = false;  // Backtrack
}
```

**Your debugging:**

- **Bug location:** _[What's missing at the start?]_
- **Bug explanation:** _[What will happen?]_
- **Bug fix:** _[What checks are needed?]_

**Test case:**

- Grid: [['a','b'],['c','d']]
- Dictionary: ["ab"]
- What error occurs? _[Fill in]_

<details markdown>
<summary>Click to verify your answer</summary>

**Bug:** Missing boundary and visited checks at the START of the function. Current code checks after accessing array, causing ArrayIndexOutOfBoundsException.

**Correct:**
```java
private void dfs(char[][] board, int i, int j, TrieNode node,
                Set<String> result, boolean[][] visited) {
    // Check bounds and visited FIRST
    if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) {
        return;
    }

    char c = board[i][j];

    if (!node.children.containsKey(c)) {
        return;
    }

    visited[i][j] = true;
    TrieNode next = node.children.get(c);

    if (next.word != null) {
        result.add(next.word);
    }

    dfs(board, i + 1, j, next, result, visited);
    dfs(board, i - 1, j, next, result, visited);
    dfs(board, i, j + 1, next, result, visited);
    dfs(board, i, j - 1, next, result, visited);

    visited[i][j] = false;
}
```

**Why:** Without boundary checks first, recursive calls will access invalid indices.
</details>

---

### Your Debugging Scorecard

After finding and fixing all bugs:

- [ ] Found all 8+ bugs across 5 challenges
- [ ] Understood WHY each bug causes incorrect behavior
- [ ] Could explain the fix to someone else
- [ ] Learned common trie mistakes to avoid

**Common mistakes you discovered:**

1. _[Forgetting to mark isEndOfWord]_
2. _[Not creating new nodes during insert]_
3. _[Not checking isEndOfWord in search]_
4. _[Wrong order in DFS collection]_
5. _[Missing boundary checks in grid search]_
6. _[Incomplete deletion logic]_

---

## Decision Framework

**Your task:** Build decision trees for trie problems.

### Question 1: What operations do you need?

Answer after solving problems:
- **Insert/search single word?** _[Basic trie]_
- **Prefix search?** _[Trie with prefix traversal]_
- **Find all words with prefix?** _[Autocomplete trie]_
- **Multiple word search in grid?** _[Trie + DFS]_

### Question 2: What are the constraints?

**Space critical:**

- Use: _[Hash map for children (sparse)]_
- Avoid: _[Array for children (dense)]_

**Need frequency/values:**

- Store: _[Additional data at nodes]_
- Examples: _[MapSum, frequency trie]_

**Need to delete:**

- Implement: _[Recursive deletion with cleanup]_

### Your Decision Tree

```
Trie Pattern Selection
│
├─ Basic operations?
│   ├─ Insert/search/prefix → Basic Trie ✓
│   └─ With deletion → Trie with delete ✓
│
├─ Multiple word search?
│   ├─ In grid → Trie + DFS ✓
│   └─ In stream → Trie + automaton ✓
│
├─ Prefix-based queries?
│   ├─ Find all with prefix → Autocomplete ✓
│   ├─ Count with prefix → Trie with counts ✓
│   └─ Sum with prefix → MapSum trie ✓
│
└─ String operations?
    ├─ Longest common prefix → Trie traversal ✓
    ├─ Replace with root → Dictionary trie ✓
    └─ Longest word → Trie with validation ✓
```

### The "Kill Switch" - When NOT to use Tries

**Don't use trie when:**

1. _[Small dictionary - hash set simpler]_
2. _[No prefix queries - hash table better]_
3. _[Memory constrained - hash set more compact]_
4. _[Single word lookup - hash table O(1) vs O(m)]_

### The Rule of Three: Alternatives

**Option 1: Trie**

- Pros: _[Prefix queries O(m), space-efficient for common prefixes]_
- Cons: _[More complex, pointer overhead]_
- Use when: _[Many prefix queries, common prefixes]_

**Option 2: Hash Set**

- Pros: _[Simple, O(1) lookup, less space]_
- Cons: _[No prefix queries]_
- Use when: _[Just membership testing]_

**Option 3: Sorted Array + Binary Search**

- Pros: _[Less space, can do prefix queries]_
- Cons: _[Slower insert/delete]_
- Use when: _[Static dictionary]_

---

## Practice

### LeetCode Problems

**Easy (Complete all 2):**

- [ ] [208. Implement Trie](https://leetcode.com/problems/implement-trie-prefix-tree/)
    - Pattern: _[Basic trie]_
    - Your solution time: ___
    - Key insight: _[Fill in after solving]_

- [ ] [720. Longest Word in Dictionary](https://leetcode.com/problems/longest-word-in-dictionary/)
    - Pattern: _[Trie with validation]_
    - Your solution time: ___
    - Key insight: _[Fill in]_

**Medium (Complete 3-4):**

- [ ] [211. Design Add and Search Words Data Structure](https://leetcode.com/problems/design-add-and-search-words-data-structure/)
    - Pattern: _[Trie with wildcard]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [212. Word Search II](https://leetcode.com/problems/word-search-ii/)
    - Pattern: _[Trie + DFS]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [1268. Search Suggestions System](https://leetcode.com/problems/search-suggestions-system/)
    - Pattern: _[Autocomplete]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

- [ ] [648. Replace Words](https://leetcode.com/problems/replace-words/)
    - Pattern: _[Dictionary trie]_
    - Difficulty: _[Rate 1-10]_
    - Key insight: _[Fill in]_

**Hard (Optional):**

- [ ] [472. Concatenated Words](https://leetcode.com/problems/concatenated-words/)
    - Pattern: _[Trie + DP]_
    - Key insight: _[Fill in after solving]_

- [ ] [1707. Maximum XOR With an Element From Array](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/)
    - Pattern: _[Binary trie]_
    - Key insight: _[Fill in after solving]_

---

## Review Checklist

Before moving to the next topic:

- [ ] **Implementation**
    - [ ] Basic trie: insert, search, prefix, delete all work
    - [ ] Word search: find multiple words in grid works
    - [ ] Autocomplete: prefix matching and suggestions work
    - [ ] Advanced: LCP, MapSum, replace words work
    - [ ] All client code runs successfully

- [ ] **Pattern Recognition**
    - [ ] Can identify when trie is appropriate
    - [ ] Understand trie vs hash table tradeoffs
    - [ ] Know how to store additional data in nodes
    - [ ] Recognize prefix query patterns

- [ ] **Problem Solving**
    - [ ] Solved 2 easy problems
    - [ ] Solved 3-4 medium problems
    - [ ] Analyzed time/space complexity
    - [ ] Understood when to use array vs map for children

- [ ] **Understanding**
    - [ ] Filled in all ELI5 explanations
    - [ ] Built decision tree
    - [ ] Identified when NOT to use tries
    - [ ] Can explain trie structure and traversal

- [ ] **Mastery Check**
    - [ ] Could implement all patterns from memory
    - [ ] Could recognize pattern in new problem
    - [ ] Could explain to someone else
    - [ ] Understand space complexity of tries

---

## Understanding Gate (Must Pass Before Continuing)

**Your task:** Prove mastery through explanation and application. You cannot move forward until you can confidently complete this section.

### Gate 1: Explain to a Junior Developer

**Scenario:** A junior developer asks you about tries.

**Your explanation (write it out):**

> "A trie is a data structure where..."
>
> _[Fill in your explanation in plain English - 3-4 sentences max]_

**Self-assessment:**

- Clarity score (1-10): ___
- Could your explanation be understood by a non-technical person? _[Yes/No]_
- Did you use analogies or real-world examples? _[Yes/No]_

If you scored below 7 or answered "No" to either question, revise your explanation.

---

### Gate 2: Whiteboard Exercise

**Task:** Draw a trie for words ["cat", "car", "card", "care", "dog"], without looking at code.

**Draw the trie structure:**

```
Root:
[Your drawing - show all nodes, mark end-of-word nodes with *]




```

**Verification:**

- [ ] Drew root node correctly
- [ ] Showed all character nodes
- [ ] Marked end-of-word nodes with isEndOfWord = true
- [ ] Showed shared prefixes (ca, car)
- [ ] Separate branch for "dog"

---

### Gate 3: Pattern Recognition Test

**Without looking at your notes, classify these problems:**

| Problem | Data Structure (Trie/HashMap/Array) | Why? |
|---------|-------------------------------------|------|
| Check if word exists in dictionary | _[Fill in]_ | _[Explain]_ |
| Autocomplete with prefix | _[Fill in]_ | _[Explain]_ |
| Find all anagrams of word | _[Fill in]_ | _[Explain]_ |
| Word search in 2D grid (multiple words) | _[Fill in]_ | _[Explain]_ |
| Count words with given prefix | _[Fill in]_ | _[Explain]_ |
| Longest common prefix of strings | _[Fill in]_ | _[Explain]_ |

**Score:** ___/6 correct

If you scored below 5/6, review the patterns and try again.

---

### Gate 4: Complexity Analysis

**Complete this table from memory:**

| Operation | Trie Time | Trie Space | HashMap Time | HashMap Space |
|-----------|-----------|------------|--------------|---------------|
| Insert word of length m | O(?) | O(?) | O(?) | O(?) |
| Search word of length m | O(?) | O(?) | O(?) | O(?) |
| Prefix search (find all with prefix p, k results) | O(?) | O(?) | O(?) | O(?) |
| Delete word of length m | O(?) | O(?) | O(?) | O(?) |

**Deep question:** When does trie use LESS space than storing words in array?

Your answer: _[Fill in - explain the shared prefix concept]_

**Deep question:** When does trie use MORE space than hash set?

Your answer: _[Fill in - explain pointer overhead]_

---

### Gate 5: Trade-off Decision

**Scenario:** You're building an autocomplete system for 1 million product names. Average name length is 15 characters. Users type prefix and expect instant suggestions.

**Option A:** Hash Set with linear scan
- Complexity: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Option B:** Trie with prefix traversal
- Complexity: _[Fill in]_
- Pros: _[Fill in]_
- Cons: _[Fill in]_

**Your decision:** I would choose _[A/B]_ because...

_[Fill in your reasoning - consider time, space, and user experience]_

**What would make you change your decision?**

- _[Fill in - what constraints would flip your choice?]_

---

### Gate 6: Implementation from Memory (Final Test)

**Set a 15-minute timer. Implement without looking at notes:**

```java
/**
 * Implement: Trie with insert, search, and startsWith
 */
class Trie {
    // Your implementation here







}
```

**After implementing, test with:**

- Insert: "apple", "app", "application"
- Search: "app" (true), "appl" (false)
- StartsWith: "app" (true), "ban" (false)

**Verification:**

- [ ] Implemented TrieNode correctly
- [ ] Insert works and marks isEndOfWord
- [ ] Search checks isEndOfWord flag
- [ ] StartsWith doesn't check isEndOfWord
- [ ] Handles empty strings and edge cases
- [ ] Time complexity is O(m) for all operations

---

### Gate 7: Bug Detection Challenge

**Without running code, find the bug:**

```java
public void insert(String word) {
    TrieNode current = root;
    for (char c : word.toCharArray()) {
        if (!current.children.containsKey(c)) {
            current.children.put(c, new TrieNode());
        }
        current = current.children.get(c);
    }
    // Bug is here somewhere - what's missing?
}
```

**Your answer:** _[What's the bug?]_

**How to fix:** _[Fill in]_

**Verification:**

- [ ] Identified the bug correctly (missing isEndOfWord = true)
- [ ] Explained why it causes problems
- [ ] Could describe test case that exposes it

---

### Gate 8: Real-World Application

**Scenario:** Design a spell checker that suggests corrections for misspelled words.

**Your design:**

1. **Data structure:** _[Trie/HashMap/Other - Why?]_
2. **Core operations needed:**
    - _[List 3-4 operations]_
3. **How to find suggestions:**
    - _[Describe algorithm]_
4. **Complexity:**
    - Build dictionary: _[O(?)]_
    - Find suggestions: _[O(?)]_

**Challenge question:** How would you handle typos (1 character off)?

Your approach: _[Fill in - BFS/DFS with edit distance?]_

---

### Gate 9: Teaching Check

**The ultimate test of understanding is teaching.**

**Task:** Explain why tries are better than hash tables for autocomplete.

Your explanation:

> "For autocomplete, tries are better because..."
>
> _[Fill in - use concrete example with numbers]_

**Examples you would show:**

1. _[Scenario where trie wins]_
2. _[Scenario where hash table might be better]_
3. _[Visual diagram you'd draw]_

---

### Gate 10: Mistake Prevention

**Common mistakes to avoid - can you explain each?**

1. **Forgetting isEndOfWord flag**
    - Why it matters: _[Fill in]_
    - Example problem: _[Fill in]_

2. **Not creating new nodes in insert**
    - Why it matters: _[Fill in]_
    - What happens: _[Fill in]_

3. **Wrong return in search method**
    - Correct return: _[Fill in]_
    - Wrong return: _[Fill in]_
    - Why it matters: _[Fill in]_

4. **Missing boundary checks in grid search**
    - Why it matters: _[Fill in]_
    - What happens: _[Fill in]_

5. **Incomplete deletion logic**
    - What to check: _[Fill in]_
    - Why it matters: _[Fill in]_

---

### Mastery Certification

**I certify that I can:**

- [ ] Implement basic trie (insert, search, prefix) from memory
- [ ] Explain when and why to use trie over hash table
- [ ] Implement autocomplete with prefix matching
- [ ] Use trie for word search in 2D grid
- [ ] Implement trie deletion correctly
- [ ] Analyze space and time complexity
- [ ] Debug common trie mistakes
- [ ] Apply tries to real-world problems
- [ ] Teach this concept to someone else

**Self-assessment score:** ___/10

**If score < 8:** Review the sections where you struggled, then retry this gate.

**If score >= 8:** Congratulations! You've mastered tries. Proceed to the next topic.
