# Declarative programming

- Language: Prolog and Elixir
- Goal: Making a solver for the specified puzzle.

#### Further description of implementation and specification in $prolog.pdf$ and $elixir.pdf$

## Specification - The Number Spiral Puzzle
A given n × n square grid contains numbers between 1 and m. The task is to place additional numbers (also between 1 and m) into the grid while satisfying the following conditions:
1. Each row and each column must contain all the numbers 1 to m exactly once.
2. Along the spiral path starting from the top-left corner, the numbers must follow the repeating sequence 1, 2, ..., m, 1, 2, ..., m, ....

The spiral path is defined as follows:
- Start at the top-left corner and move left to right along the first row.
- Then move top to bottom along the last column.
- Next, move right to left along the last row.
- Then move bottom to top along the first column, stopping at the second row, first column.
- Once the outermost rows and columns have been traversed, the process recursively continues inside the remaining (n-2) × (n-2) square, starting from the second row, second column.
