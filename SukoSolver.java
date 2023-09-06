package com.sarath;

import com.google.ortools.Loader;
import com.google.ortools.sat.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SukoSolver {

    private CpModel cpModel;
    private List<SukoCell> sukoCells;
    private int firstQuadrantSum;
    private int secondQuadrantSum;
    private int thirdQuadrantSum;
    private int fourthQuadrantSum;
    private int greySum;
    private int pinkSum;
    private int yellowSum;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class SukoCell {
        public enum Color {
            GREY, YELLOW, PINK
        }

        private Color color;
        private int index;
        private IntVar variable;

    }

    public static void main(String[] args) {
        Loader.loadNativeLibraries();
        constructProblemNumber875();
        constructProblemNumber1392();
    }

    private static void constructProblemNumber875() {
        printHeader(875);
        var suko = new SukoSolver();
        var cpModel = new CpModel();
        suko.setCpModel(cpModel);

        suko.setGreySum(21);
        suko.setYellowSum(14);
        suko.setPinkSum(10);

        suko.setFirstQuadrantSum(25);
        suko.setSecondQuadrantSum(26);
        suko.setThirdQuadrantSum(13);
        suko.setFourthQuadrantSum(14);

        var cells = new ArrayList<SukoCell>();
        //Grey cells
        cells.add(new SukoCell(SukoCell.Color.GREY, 1, cpModel.newIntVar(1L, 9L, "1")));
        cells.add(new SukoCell(SukoCell.Color.GREY, 2, cpModel.newIntVar(1L, 9L, "2")));
        cells.add(new SukoCell(SukoCell.Color.GREY, 5, cpModel.newIntVar(1L, 9L, "5")));

        //Yellow cells
        cells.add(new SukoCell(SukoCell.Color.YELLOW, 3, cpModel.newIntVar(1L, 9L, "3")));
        cells.add(new SukoCell(SukoCell.Color.YELLOW, 6, cpModel.newIntVar(1L, 9L, "6")));

        //Pink cells
        cells.add(new SukoCell(SukoCell.Color.PINK, 4, cpModel.newIntVar(1L, 9L, "4")));
        cells.add(new SukoCell(SukoCell.Color.PINK, 7, cpModel.newIntVar(1L, 9L, "7")));
        cells.add(new SukoCell(SukoCell.Color.PINK, 8, cpModel.newIntVar(1L, 9L, "8")));
        cells.add(new SukoCell(SukoCell.Color.PINK, 9, cpModel.newIntVar(1L, 9L, "9")));
        suko.setSukoCells(cells);


        //Run solver
        suko.solve();
        printFooter();
    }

    private static void printHeader(int number) {
        System.out.println(String.format("Solving problem %s", number));
        System.out.println("═══════════════════════════════════════════");
    }

    private static void printFooter() {
        System.out.println("═══════════════════════════════════════════");
        System.out.println();
    }

    private static void constructProblemNumber1392() {
        printHeader(1392);
        var suko = new SukoSolver();
        var cpModel = new CpModel();
        suko.setCpModel(cpModel);

        suko.setGreySum(16);
        suko.setYellowSum(17);
        suko.setPinkSum(12);

        suko.setFirstQuadrantSum(13);
        suko.setSecondQuadrantSum(18);
        suko.setThirdQuadrantSum(23);
        suko.setFourthQuadrantSum(18);

        var cells = new ArrayList<SukoCell>();
        //Grey cells
        cells.add(new SukoCell(SukoCell.Color.GREY, 1, cpModel.newIntVar(1L, 9L, "1")));
        cells.add(new SukoCell(SukoCell.Color.YELLOW, 2, cpModel.newIntVar(1L, 9L, "2")));
        cells.add(new SukoCell(SukoCell.Color.YELLOW, 3, cpModel.newIntVar(1L, 9L, "3")));
        cells.add(new SukoCell(SukoCell.Color.GREY, 4, cpModel.newIntVar(1L, 9L, "4")));
        cells.add(new SukoCell(SukoCell.Color.GREY, 5, cpModel.newIntVar(1L, 9L, "5")));
        cells.add(new SukoCell(SukoCell.Color.YELLOW, 6, cpModel.newIntVar(1L, 9L, "6")));
        cells.add(new SukoCell(SukoCell.Color.GREY, 7, cpModel.newIntVar(1L, 9L, "7")));
        cells.add(new SukoCell(SukoCell.Color.PINK, 8, cpModel.newIntVar(1L, 9L, "8")));
        cells.add(new SukoCell(SukoCell.Color.PINK, 9, cpModel.newIntVar(1L, 9L, "9")));
        suko.setSukoCells(cells);


        //Run solver
        suko.solve();
        System.out.println("═══════════════════════════════════════════");
    }


    private void solve() {
        addAllDifferentConstraint();
        addQuadrantConstraints();

        //Color constraints
        var greyCells = new ArrayList<IntVar>();
        var yellowCells = new ArrayList<IntVar>();
        var pinkCells = new ArrayList<IntVar>();

        allocateCellsBasedOnColor(greyCells, yellowCells, pinkCells);

        var greyCellArr = new IntVar[greyCells.size()];
        var yellowCellArr = new IntVar[yellowCells.size()];
        var pinkCellArr = new IntVar[pinkCells.size()];

        copyArrayListToArray(greyCells, greyCellArr);
        copyArrayListToArray(yellowCells, yellowCellArr);
        copyArrayListToArray(pinkCells, pinkCellArr);

        constructColorConstraint(greyCellArr, yellowCellArr, pinkCellArr);

        findSolution();
    }

    private void addAllDifferentConstraint() {
        var variables = new IntVar[sukoCells.size()];
        for(var i = 0; i < sukoCells.size(); i++) {
            variables[i] = sukoCells.get(i).variable;
        }
        cpModel.addAllDifferent(variables);
    }

    private void findSolution() {
        var solver = new CpSolver();
        var status = solver.solve(cpModel);

        if (CpSolverStatus.FEASIBLE.equals(status) || CpSolverStatus.OPTIMAL.equals(status)) {
            for(var cell: sukoCells) {
                System.out.println(String.format("%s = %s", cell.index, solver.value(cell.variable)));
            }
        } else {
            System.out.println("No solution found");
        }
    }

    private void constructColorConstraint(IntVar[] greyCellArr, IntVar[] yellowCellArr, IntVar[] pinkCellArr) {
        var greyColorExpr = LinearExpr.newBuilder()
                .addSum(greyCellArr)
                .build();
        var yellowColorExpr = LinearExpr.newBuilder()
                .addSum(yellowCellArr)
                .build();
        var pinkColorExpr = LinearExpr.newBuilder()
                .addSum(pinkCellArr)
                .build();
        cpModel.addEquality(greyColorExpr, greySum);
        cpModel.addEquality(yellowColorExpr, yellowSum);
        cpModel.addEquality(pinkColorExpr, pinkSum);
    }

    private void addQuadrantConstraints() {
        var firstQuadrantConstraintExpression = LinearExpr.newBuilder()
                .addSum(new LinearArgument[]{sukoCells.get(0).variable,
                        sukoCells.get(1).variable,
                        sukoCells.get(3).variable,
                        sukoCells.get(4).variable, })
                .build();
        cpModel.addEquality(firstQuadrantConstraintExpression, firstQuadrantSum);

        var secondQuadrantConstraintExpression = LinearExpr.newBuilder()
                .addSum(new LinearArgument[]{sukoCells.get(1).variable,
                        sukoCells.get(2).variable,
                        sukoCells.get(4).variable,
                        sukoCells.get(5).variable, })
                .build();
        cpModel.addEquality(secondQuadrantConstraintExpression, secondQuadrantSum);

        var thirdQuadrantConstraintExpression = LinearExpr.newBuilder()
                .addSum(new LinearArgument[]{sukoCells.get(3).variable,
                        sukoCells.get(4).variable,
                        sukoCells.get(6).variable,
                        sukoCells.get(7).variable, })
                .build();
        cpModel.addEquality(thirdQuadrantConstraintExpression, thirdQuadrantSum);

        var fourthQuadrantConstraintExpression = LinearExpr.newBuilder()
                .addSum(new LinearArgument[]{sukoCells.get(4).variable,
                        sukoCells.get(5).variable,
                        sukoCells.get(7).variable,
                        sukoCells.get(8).variable, })
                .build();
        cpModel.addEquality(fourthQuadrantConstraintExpression, fourthQuadrantSum);
    }

    private void allocateCellsBasedOnColor(ArrayList<IntVar> greyCells, ArrayList<IntVar> yellowCells, ArrayList<IntVar> pinkCells) {
        for(var cell: sukoCells) {
            switch (cell.color) {
                case GREY -> greyCells.add(cell.variable);
                case YELLOW -> yellowCells.add(cell.variable);
                case PINK -> pinkCells.add(cell.variable);
                default -> throw new RuntimeException("Unknown cell color");
            }
        }
    }

    private static void copyArrayListToArray(ArrayList<IntVar> list, IntVar[] arr) {
        for(int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
    }
}

/**

 <dependency>
 <groupId>com.google.ortools</groupId>
 <artifactId>ortools-java</artifactId>
 <version>9.7.2996</version>
 </dependency>

 */