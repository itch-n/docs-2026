package com.study.dsa.backtracking;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ConstraintSatisfactionTest {

    // ---- solveNQueens ---------------------------------------------------

    @Test
    void testSolveNQueens4Solutions() {
        // 4-queens has exactly 2 solutions
        List<List<String>> solutions = ConstraintSatisfaction.solveNQueens(4);
        assertEquals(2, solutions.size());
    }

    @Test
    void testSolveNQueens4SolutionContents() {
        List<List<String>> solutions = ConstraintSatisfaction.solveNQueens(4);
        // The two valid boards for n=4
        List<String> board1 = Arrays.asList(".Q..", "...Q", "Q...", "..Q.");
        List<String> board2 = Arrays.asList("..Q.", "Q...", "...Q", ".Q..");
        assertTrue(solutions.contains(board1) || solutions.contains(board2),
            "At least one known solution must be present");
        // Verify both known solutions are present
        assertTrue(solutions.contains(board1));
        assertTrue(solutions.contains(board2));
    }

    @Test
    void testSolveNQueens1() {
        // 1-queens: single queen on 1x1 board
        List<List<String>> solutions = ConstraintSatisfaction.solveNQueens(1);
        assertEquals(1, solutions.size());
        assertEquals(Arrays.asList("Q"), solutions.get(0));
    }

    @Test
    void testSolveNQueens2NoSolution() {
        // 2-queens and 3-queens have 0 solutions
        assertEquals(0, ConstraintSatisfaction.solveNQueens(2).size());
        assertEquals(0, ConstraintSatisfaction.solveNQueens(3).size());
    }

    // ---- totalNQueens ---------------------------------------------------

    @Test
    void testTotalNQueensKnownCounts() {
        // Well-known sequence: 1,0,0,2,10,4,40,92 for n=1..8
        assertEquals(1,  ConstraintSatisfaction.totalNQueens(1));
        assertEquals(0,  ConstraintSatisfaction.totalNQueens(2));
        assertEquals(0,  ConstraintSatisfaction.totalNQueens(3));
        assertEquals(2,  ConstraintSatisfaction.totalNQueens(4));
        assertEquals(10, ConstraintSatisfaction.totalNQueens(5));
        assertEquals(4,  ConstraintSatisfaction.totalNQueens(6));
        assertEquals(40, ConstraintSatisfaction.totalNQueens(7));
        assertEquals(92, ConstraintSatisfaction.totalNQueens(8));
    }

    // ---- solveSudoku ----------------------------------------------------

    @Test
    void testSolveSudokuKnownPuzzle() {
        char[][] board = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };

        ConstraintSatisfaction.solveSudoku(board);

        // Known solution for this puzzle
        char[][] expected = {
            {'5','3','4','6','7','8','9','1','2'},
            {'6','7','2','1','9','5','3','4','8'},
            {'1','9','8','3','4','2','5','6','7'},
            {'8','5','9','7','6','1','4','2','3'},
            {'4','2','6','8','5','3','7','9','1'},
            {'7','1','3','9','2','4','8','5','6'},
            {'9','6','1','5','3','7','2','8','4'},
            {'2','8','7','4','1','9','6','3','5'},
            {'3','4','5','2','8','6','1','7','9'}
        };

        for (int row = 0; row < 9; row++) {
            assertArrayEquals(expected[row], board[row],
                "Row " + row + " mismatch");
        }
    }

    @Test
    void testSolveSudokuNoBlanks() {
        // A fully-solved board should remain unchanged
        char[][] board = {
            {'5','3','4','6','7','8','9','1','2'},
            {'6','7','2','1','9','5','3','4','8'},
            {'1','9','8','3','4','2','5','6','7'},
            {'8','5','9','7','6','1','4','2','3'},
            {'4','2','6','8','5','3','7','9','1'},
            {'7','1','3','9','2','4','8','5','6'},
            {'9','6','1','5','3','7','2','8','4'},
            {'2','8','7','4','1','9','6','3','5'},
            {'3','4','5','2','8','6','1','7','9'}
        };
        char[][] copy = Arrays.stream(board).map(char[]::clone).toArray(char[][]::new);
        ConstraintSatisfaction.solveSudoku(board);
        for (int row = 0; row < 9; row++) {
            assertArrayEquals(copy[row], board[row]);
        }
    }
}
