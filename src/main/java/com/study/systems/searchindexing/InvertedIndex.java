package com.study.systems.searchindexing;

import java.util.*;
class InvertedIndex {
    // Term → Posting List
    Map<String, PostingList> index;

    class PostingList {
        List<Posting> postings;
        int documentFrequency; // How many docs contain this term
    }

    class Posting {
        int documentId;
        int termFrequency;     // How many times in this doc
        List<Integer> positions; // Where in doc
    }

    // Search for term
    PostingList search(String term) {
        return index.get(normalize(term));
    }

    // Boolean AND query: term1 AND term2
    List<Integer> intersect(String term1, String term2) {
        PostingList p1 = search(term1);
        PostingList p2 = search(term2);

        // Merge postings lists (efficient with sorted lists)
        return mergeSortedLists(p1, p2);
    }

    // Merge posting lists for boolean AND query (returns matching document IDs)
    private List<Integer> mergeSortedLists(PostingList p1, PostingList p2) {
        // TODO: implement sorted-list intersection
        return new ArrayList<>();
    }

    // Normalize a query term (lowercase, strip punctuation, etc.)
    private String normalize(String term) {
        return term.toLowerCase();
    }
}
