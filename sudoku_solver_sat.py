from ortools.sat.python import cp_model

""" Sudoku Solver:
    The below program solves a 9x9 sudoku puzzle with 3 x 3 blocks
    The encoded constraints for solving are as follows
        - Any cell of the 9x9 grid should only occupy values from domain [1 to 9]
        - Elements in a column should be all different
        - Elements in a row should be all different
        - Elements in a block should be all different
        
    The above four constraints are sufficient to generate arbitrary sudoku problems.
    
    A sudoku problem will have partially filled values in its grid, that is added as additional equality constrains
    against the cells.
    
    The sudoku solver is an Integer linear programming satisfiability or feasibility problem, where the objective function 
    is non existent. Any solution from the feasible region is acceptable. 
"""
def main():
    #https://www.7sudoku.com/very-difficult
    problem_grid = [
        [3, 0, 0, 0, 0, 5, 1, 0, 0],
        [0, 0, 0, 0, 7, 6, 0, 2, 0],
        [0, 4, 0, 2, 0, 0, 5, 0, 0],
        [0, 0, 0, 5, 0, 1, 0, 0, 8],
        [0, 1, 0, 0, 0, 0, 0, 5, 0],
        [2, 0, 0, 9, 0, 7, 0, 0, 0],
        [0, 0, 7, 0, 0, 2, 0, 3, 0],
        [0, 8, 0, 7, 1, 0, 0, 0, 0],
        [0, 0, 9, 3, 0, 0, 0, 0, 7]]

    block_size = 3 #horizontal and vertical blocks in the grid
    n = 9 #size of grid
    model = cp_model.CpModel()
    design_grid = [[], [], [], [], [], [], [], [], []] #design_point grid
    for i in range(n):
        for j in range(n):
            var = model.NewIntVar(1, 9, f'c-{i}-{j}')
            design_grid[i].append(var)

    # Add existing grid value equality, row specific and column specific constraints
    for i in range(n):
        row = []
        col = []
        for j in range(n):
            cell_val = problem_grid[i][j]
            if cell_val > 0:
                model.Add(design_grid[i][j] == cell_val)
            row.append(design_grid[i][j])
            col.append(design_grid[j][i])
        model.AddAllDifferent(row)
        model.AddAllDifferent(col)

    # Constraints for the 9 blocks
    for i in range(block_size):
        for j in range(block_size):
            block = []
            for bi in range(block_size):
                for bj in range(block_size):
                    block.append(design_grid[i * block_size + bi][j * block_size + bj])
            model.AddAllDifferent(block)

    solver = cp_model.CpSolver()
    status = solver.Solve(model)
    if status == cp_model.OPTIMAL or status == cp_model.FEASIBLE:
        print("Problem            | Solution")
        for i in range(n):
            for j in range(n):
                print(problem_grid[i][j], end="")
                print(" ", end="")
            print(" | ", end="")
            for j in range(n):
                print(solver.Value(design_grid[i][j]), end="")
                print(" ", end="")
            print()


if __name__ == '__main__':
    main()


"""
 http://profs.sci.univr.it/~rrizzi/classes/PLS2015/sudoku/doc/497_Olszowy_Wiktor_Sudoku.pdf 
 The above document expresses sudoku system as a linear model with binary constraint, yet to explore
"""