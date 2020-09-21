from __future__ import print_function
import math
from ortools.linear_solver import pywraplp


def solve_a():
    print("Problem (a)")
    solver = pywraplp.Solver.CreateSolver('simple_mip_program', 'CBC')
    x1 = solver.IntVar(0, math.inf, 'x1')
    x2 = solver.IntVar(0, math.inf, 'x2')
    x3 = solver.IntVar(0, math.inf, 'x3')

    # constraints
    solver.Add(-x1 + 2 * x2 - 2 * x3 >= 8)
    solver.Add(-x1 + x2 + x3 <= 4)
    solver.Add(2 * x1 - x2 - 4 * x3 <= 10)
    solver.Maximize(2 * x3)

    status = solver.Solve()

    if status == pywraplp.Solver.OPTIMAL:
        print('Solution:')
        print('Objective value =', solver.Objective().Value())
        print('x1 =', x1.solution_value())
        print('x2 =', x2.solution_value())
        print('x3 =', x3.solution_value())
    else:
        print('The problem does not have an optimal solution.')

    print()


def solve_b():
    print("Problem (b)")
    solver = pywraplp.Solver.CreateSolver('simple_mip_program', 'CBC')
    x1 = solver.IntVar(0, math.inf, 'x1')
    x2 = solver.IntVar(0, math.inf, 'x2')

    # constraints
    solver.Add(x1 - x2 <= 20)
    solver.Add(x1 + x2 >= 40)
    solver.Add(2 * x1 - 2 * x2 >= 30)
    solver.Maximize(x1 - 3 * x2)

    status = solver.Solve()

    if status == pywraplp.Solver.OPTIMAL:
        print('Solution:')
        print('Objective value =', solver.Objective().Value())
        print('x1 =', x1.solution_value())
        print('x2 =', x2.solution_value())
    else:
        print('The problem does not have an optimal solution.')

    print()


def solve_c():
    print("Problem (c)")
    solver = pywraplp.Solver.CreateSolver('simple_mip_program', 'CBC')
    x1 = solver.IntVar(0, math.inf, 'x1')
    x2 = solver.IntVar(0, math.inf, 'x2')

    # constraints
    solver.Add(x1 - 4 * x2 >= 5)
    solver.Add(x1 - 3 * x2 <= 1)
    solver.Add(2 * x1 - 5 * x2 >= 1)
    solver.Minimize(-x1 + x2)

    status = solver.Solve()

    if status == pywraplp.Solver.OPTIMAL:
        print('Solution:')
        print('Objective value =', solver.Objective().Value())
        print('x1 =', x1.solution_value())
        print('x2 =', x2.solution_value())
    else:
        print('The problem does not have an optimal solution.')

    print()


def main():
    solve_a()
    solve_b()
    solve_c()

if __name__ == '__main__':
    main()
