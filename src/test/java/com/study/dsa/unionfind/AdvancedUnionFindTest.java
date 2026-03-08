package com.study.dsa.unionfind;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class AdvancedUnionFindTest {

    @Test
    void testEquationsPossibleContradiction() {
        // a==b, b!=a: contradiction -> false
        assertFalse(AdvancedUnionFind.equationsPossible(new String[]{"a==b", "b!=a"}));
    }

    @Test
    void testEquationsPossibleConsistent() {
        // b==a, a==b: consistent -> true
        assertTrue(AdvancedUnionFind.equationsPossible(new String[]{"b==a", "a==b"}));
    }

    @Test
    void testEquationsPossibleTransitive() {
        // a==b, b==c, a==c: consistent -> true
        assertTrue(AdvancedUnionFind.equationsPossible(new String[]{"a==b", "b==c", "a==c"}));
    }

    @Test
    void testEquationsPossibleUnequalDistinctVars() {
        // a!=b with no equality constraint: true
        assertTrue(AdvancedUnionFind.equationsPossible(new String[]{"a!=b"}));
    }

    @Test
    void testCalcEquation() {
        // a/b = 2.0, b/c = 3.0
        // queries: a/c, b/a, a/e (unknown), a/a, x/x (unknown var)
        List<List<String>> equations = Arrays.asList(
            Arrays.asList("a", "b"),
            Arrays.asList("b", "c")
        );
        double[] values = {2.0, 3.0};
        List<List<String>> queries = Arrays.asList(
            Arrays.asList("a", "c"),   // 2*3 = 6.0
            Arrays.asList("b", "a"),   // 1/2 = 0.5
            Arrays.asList("a", "e"),   // unknown -> -1.0
            Arrays.asList("a", "a"),   // 1.0
            Arrays.asList("x", "x")    // unknown var -> -1.0
        );
        double[] results = AdvancedUnionFind.calcEquation(equations, values, queries);
        assertEquals(5, results.length);
        assertEquals(6.0,  results[0], 0.001);
        assertEquals(0.5,  results[1], 0.001);
        assertEquals(-1.0, results[2], 0.001);
        assertEquals(1.0,  results[3], 0.001);
        assertEquals(-1.0, results[4], 0.001);
    }

    @Test
    void testAreSentencesSimilarTrue() {
        // great~good~fine, acting~drama, skills~talent
        String[] words1 = {"great", "acting", "skills"};
        String[] words2 = {"fine", "drama", "talent"};
        List<List<String>> pairs = Arrays.asList(
            Arrays.asList("great", "good"),
            Arrays.asList("fine", "good"),
            Arrays.asList("acting", "drama"),
            Arrays.asList("skills", "talent")
        );
        assertTrue(AdvancedUnionFind.areSentencesSimilar(words1, words2, pairs));
    }

    @Test
    void testAreSentencesSimilarDifferentLength() {
        String[] words1 = {"I", "love", "leetcode"};
        String[] words2 = {"I", "love"};
        List<List<String>> pairs = Arrays.asList();
        assertFalse(AdvancedUnionFind.areSentencesSimilar(words1, words2, pairs));
    }
}
