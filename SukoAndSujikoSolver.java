package com.sarath;

import com.google.ortools.Loader;
import com.google.ortools.sat.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SukoAndSujikoSolver {

    private CpModel cpModel;
    private List<Cell> cells;
    private int firstQuadrantSum;
    private int secondQuadrantSum;
    private int thirdQuadrantSum;
    private int fourthQuadrantSum;

    private boolean hasColor = true;
    private int greySum;
    private int pinkSum;
    private int yellowSum;


    @Getter
    @Setter
    @Builder
    public static class Cell {
        public enum Color {
            GREY, YELLOW, PINK
        }

        private Color color;
        private int index;
        @Builder.Default
        private int cellValue = 0;
        private IntVar variable;

    }

    public static void main(String[] args) {
        Loader.loadNativeLibraries();
        constructProblemNumber875();
        constructProblemNumber1392();
        constructProblemNumber4695();
    }

    private static void constructProblemNumber875() {
        printHeader(875);
        var suko = new SukoAndSujikoSolver();
        var cpModel = new CpModel();
        suko.setCpModel(cpModel);

        suko.setGreySum(21);
        suko.setYellowSum(14);
        suko.setPinkSum(10);

        suko.setFirstQuadrantSum(25);
        suko.setSecondQuadrantSum(26);
        suko.setThirdQuadrantSum(13);
        suko.setFourthQuadrantSum(14);

        var cells = new ArrayList<Cell>();
        //Grey cells
        cells.add(buildCell(cpModel, Cell.Color.GREY, 1));
        cells.add(buildCell(cpModel, Cell.Color.GREY, 2));
        cells.add(buildCell(cpModel, Cell.Color.GREY, 5));

        //Yellow cells
        cells.add(buildCell(cpModel, Cell.Color.YELLOW, 3));
        cells.add(buildCell(cpModel, Cell.Color.YELLOW, 6));

        //Pink cells
        cells.add(buildCell(cpModel, Cell.Color.PINK, 4));
        cells.add(buildCell(cpModel, Cell.Color.PINK, 7));
        cells.add(buildCell(cpModel, Cell.Color.PINK, 8));
        cells.add(buildCell(cpModel, Cell.Color.PINK, 9));
        suko.setCells(cells);


        //Run solver
        suko.solve();
        printFooter();
    }

    private static Cell buildCell(CpModel cpModel, Cell.Color color, int index) {
        return Cell.builder()
                .color(color)
                .index(index)
                .variable(cpModel.newIntVar(1L, 9L, String.valueOf(index)))
                .build();
    }

    private static Cell buildCell(CpModel cpModel, int index, int cellValue) {
        return Cell.builder()
                .index(index)
                .cellValue(cellValue)
                .variable(cpModel.newIntVar(1L, 9L, String.valueOf(index)))
                .build();
    }

    private static Cell buildCell(CpModel cpModel, int index) {
        return Cell.builder()
                .index(index)
                .variable(cpModel.newIntVar(1L, 9L, String.valueOf(index)))
                .build();
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
        var suko = new SukoAndSujikoSolver();
        var cpModel = new CpModel();
        suko.setCpModel(cpModel);

        suko.setGreySum(16);
        suko.setYellowSum(17);
        suko.setPinkSum(12);

        suko.setFirstQuadrantSum(13);
        suko.setSecondQuadrantSum(18);
        suko.setThirdQuadrantSum(23);
        suko.setFourthQuadrantSum(18);

        var cells = new ArrayList<Cell>();
        //Grey cells
        cells.add(buildCell(cpModel, Cell.Color.GREY, 1));
        cells.add(buildCell(cpModel, Cell.Color.YELLOW, 2));
        cells.add(buildCell(cpModel, Cell.Color.YELLOW, 3));
        cells.add(buildCell(cpModel, Cell.Color.GREY, 4));
        cells.add(buildCell(cpModel, Cell.Color.GREY, 5));
        cells.add(buildCell(cpModel, Cell.Color.YELLOW, 6));
        cells.add(buildCell(cpModel, Cell.Color.GREY, 7));
        cells.add(buildCell(cpModel, Cell.Color.PINK, 8));
        cells.add(buildCell(cpModel, Cell.Color.PINK, 9));
        suko.setCells(cells);


        //Run solver
        suko.solve();
        System.out.println("═══════════════════════════════════════════");
    }

    private static void constructProblemNumber4695() {
        printHeader(4695);
        var suko = new SukoAndSujikoSolver();
        var cpModel = new CpModel();
        suko.setCpModel(cpModel);

        suko.setHasColor(false);

        suko.setFirstQuadrantSum(11);
        suko.setSecondQuadrantSum(18);
        suko.setThirdQuadrantSum(17);
        suko.setFourthQuadrantSum(23);

        var cells = new ArrayList<Cell>();
        //Grey cells
        cells.add(buildCell(cpModel,1));
        cells.add(buildCell(cpModel,2));
        cells.add(buildCell(cpModel,3, 9));
        cells.add(buildCell(cpModel,4));
        cells.add(buildCell(cpModel,5));
        cells.add(buildCell(cpModel,6));
        cells.add(buildCell(cpModel,7));
        cells.add(buildCell(cpModel,8));
        cells.add(buildCell(cpModel,9, 7));
        suko.setCells(cells);


        //Run solver
        suko.solve();
        System.out.println("═══════════════════════════════════════════");
    }


    private void solve() {
        addCellEqualityConstraint();
        addAllDifferentConstraint();
        addQuadrantConstraints();

        processColor();

        findSolution();
    }

    private void addCellEqualityConstraint() {
        cells.stream()
                .filter(cell -> cell.cellValue > 0)
                .forEach(cell -> cpModel.addEquality(cell.variable, cell.cellValue));
    }

    private void processColor() {
        if (!hasColor) {
            return;
        }
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
    }

    private void addAllDifferentConstraint() {
        var variables = new IntVar[cells.size()];
        for(var i = 0; i < cells.size(); i++) {
            variables[i] = cells.get(i).variable;
        }
        cpModel.addAllDifferent(variables);
    }

    private void findSolution() {
        var solver = new CpSolver();
        var status = solver.solve(cpModel);

        if (CpSolverStatus.FEASIBLE.equals(status) || CpSolverStatus.OPTIMAL.equals(status)) {
            for(var cell: cells) {
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
                .addSum(new LinearArgument[]{cells.get(0).variable,
                        cells.get(1).variable,
                        cells.get(3).variable,
                        cells.get(4).variable, })
                .build();
        cpModel.addEquality(firstQuadrantConstraintExpression, firstQuadrantSum);

        var secondQuadrantConstraintExpression = LinearExpr.newBuilder()
                .addSum(new LinearArgument[]{cells.get(1).variable,
                        cells.get(2).variable,
                        cells.get(4).variable,
                        cells.get(5).variable, })
                .build();
        cpModel.addEquality(secondQuadrantConstraintExpression, secondQuadrantSum);

        var thirdQuadrantConstraintExpression = LinearExpr.newBuilder()
                .addSum(new LinearArgument[]{cells.get(3).variable,
                        cells.get(4).variable,
                        cells.get(6).variable,
                        cells.get(7).variable, })
                .build();
        cpModel.addEquality(thirdQuadrantConstraintExpression, thirdQuadrantSum);

        var fourthQuadrantConstraintExpression = LinearExpr.newBuilder()
                .addSum(new LinearArgument[]{cells.get(4).variable,
                        cells.get(5).variable,
                        cells.get(7).variable,
                        cells.get(8).variable, })
                .build();
        cpModel.addEquality(fourthQuadrantConstraintExpression, fourthQuadrantSum);
    }

    private void allocateCellsBasedOnColor(ArrayList<IntVar> greyCells, ArrayList<IntVar> yellowCells, ArrayList<IntVar> pinkCells) {
        for(var cell: cells) {
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