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

**Next Topic:** [16. Advanced Topics →](16-advanced-topics.md)

**Back to:** [14. Dynamic Programming (2D) ←](14-dynamic-programming-2d.md)
