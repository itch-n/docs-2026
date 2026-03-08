package com.study.systems.searchindexing;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SearchIndex and LinearScanner inner classes inside SearchIndexBenchmark.
 *
 * Both are static inner classes with package-private visibility — accessible from
 * tests in the same package.
 */
class SearchIndexBenchmarkTest {

    // ------------------------------------------------------------------
    // SearchIndex tests
    // ------------------------------------------------------------------

    @Test
    void testSearchReturnsDocumentsThatContainTerm() {
        SearchIndexBenchmark.SearchIndex index = new SearchIndexBenchmark.SearchIndex();
        index.add(new SearchIndexBenchmark.Document(1, List.of("java", "search", "index")));
        index.add(new SearchIndexBenchmark.Document(2, List.of("search", "engine")));
        index.add(new SearchIndexBenchmark.Document(3, List.of("database", "index")));

        List<Integer> results = index.search("search");

        assertNotNull(results);
        assertTrue(results.contains(1));
        assertTrue(results.contains(2));
        assertFalse(results.contains(3));
    }

    @Test
    void testSearchForMissingTermReturnsEmptyList() {
        SearchIndexBenchmark.SearchIndex index = new SearchIndexBenchmark.SearchIndex();
        index.add(new SearchIndexBenchmark.Document(1, List.of("java", "search")));

        List<Integer> results = index.search("python");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchAndReturnsDocumentsContainingBothTerms() {
        SearchIndexBenchmark.SearchIndex index = new SearchIndexBenchmark.SearchIndex();
        index.add(new SearchIndexBenchmark.Document(1, List.of("java", "search")));
        index.add(new SearchIndexBenchmark.Document(2, List.of("java", "database")));
        index.add(new SearchIndexBenchmark.Document(3, List.of("search", "database")));

        List<Integer> results = index.searchAnd("java", "search");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.contains(1));
    }

    @Test
    void testSearchAndReturnsEmptyWhenNoDocumentMatchesBothTerms() {
        SearchIndexBenchmark.SearchIndex index = new SearchIndexBenchmark.SearchIndex();
        index.add(new SearchIndexBenchmark.Document(1, List.of("alpha")));
        index.add(new SearchIndexBenchmark.Document(2, List.of("beta")));

        List<Integer> results = index.searchAnd("alpha", "beta");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testEmptyIndexReturnsNoResults() {
        SearchIndexBenchmark.SearchIndex index = new SearchIndexBenchmark.SearchIndex();

        assertTrue(index.search("anything").isEmpty());
        assertTrue(index.searchAnd("a", "b").isEmpty());
    }

    // ------------------------------------------------------------------
    // LinearScanner tests
    // ------------------------------------------------------------------

    @Test
    void testLinearScannerFindsTermInCorpus() {
        List<SearchIndexBenchmark.Document> corpus = List.of(
            new SearchIndexBenchmark.Document(0, List.of("cat", "dog")),
            new SearchIndexBenchmark.Document(1, List.of("bird", "cat")),
            new SearchIndexBenchmark.Document(2, List.of("fish"))
        );
        SearchIndexBenchmark.LinearScanner scanner = new SearchIndexBenchmark.LinearScanner(corpus);

        List<Integer> results = scanner.search("cat");

        assertEquals(2, results.size());
        assertTrue(results.contains(0));
        assertTrue(results.contains(1));
    }

    @Test
    void testLinearScannerAndQueryRequiresBothTerms() {
        List<SearchIndexBenchmark.Document> corpus = List.of(
            new SearchIndexBenchmark.Document(0, List.of("red", "blue")),
            new SearchIndexBenchmark.Document(1, List.of("red", "green")),
            new SearchIndexBenchmark.Document(2, List.of("blue", "green"))
        );
        SearchIndexBenchmark.LinearScanner scanner = new SearchIndexBenchmark.LinearScanner(corpus);

        List<Integer> results = scanner.searchAnd("red", "blue");

        assertEquals(1, results.size());
        assertTrue(results.contains(0));
    }

    @Test
    void testBuildCorpusReturnsExpectedSize() {
        List<SearchIndexBenchmark.Document> corpus =
                SearchIndexBenchmark.buildCorpus(50, 42L);

        assertEquals(50, corpus.size());
        // Each document should have words
        for (SearchIndexBenchmark.Document doc : corpus) {
            assertNotNull(doc.words);
            assertFalse(doc.words.isEmpty());
        }
    }
}
