package com.company;

class MyMatrix {
    /** count - округление квадратных корней с машинной точностью,
     *  с 2-мя, с 4-мя, с 6-ю, с 10-ю, знаками после запятой соответственно*/
    static int[] count = new int[]{-1,2,4,6,10};

    public static void main(String[] args) {
        System.out.println();
        double[][] array;
        System.out.println("Метод Гаусса: ");
        for (int i = 0; i < count.length; i++) {

            int[] vector = new int[]{0,1,2,3};

            array = init(count[i]);
            printMatrix(array,count[i]);
            gaussMain(array,vector);

            textSlay(count[i]);
            discrepancy(init(count[i]),getValuesMain(array,vector));
        }

        System.out.println("Метод Гаусса с выбором главного элемента: ");
        for (int i = 0; i < count.length; i++) {
            array = init(count[i]);

            for (int k = 0; k < array.length; k++) {
                gauss(array, k);
            }

            printMatrix(array,count[i]);
            textSlay(count[i]);
            discrepancy(init(count[i]),getValues(array));
        }
    }

    /** Передаём значения о количестве знаков после запятой */
    public static double[][] init(int n){
        if (n == -1) return new double[][]{{3, 3 * Math.sqrt(2), ((-1) / 3.0), 0.25, 0.23},
                {0, 1+Math.sqrt(2), -((Math.sqrt(3)) / 2), 1 + (1 / Math.sqrt(2)), 0.08},
                {6, 2*Math.sqrt(2), 0, 2, 0.28},
                {(-3)/2.0, 1, -((Math.sqrt(3)) / 2), 1 / Math.sqrt(2) , 0.06}};
        else return new double[][]{{3, 3 * sqrt(2, n), ((-1) / 3.0), 0.25, 0.23},
                {0, 1 + sqrt(2, n), (-sqrt(3, n)) / 2, 1 + (1 / sqrt(2, n)), 0.08},
                {6, 2 * sqrt(2, n), 0, 2, 0.28},
                {(-3)/2.0, 1, (-sqrt(3, n)) / 2, 1 / sqrt(2, n) , 0.06}};
    }

    /** первое значение - само число, второе - количество знаков после запятой('-1' - машинная точность) */
    public static double sqrt(double a, int n){
        return (Math.round(Math.sqrt(a) * Math.pow(10,n)) / Math.pow(10,n));
    }

    /** Реализация метода Гаусса с выбором главного элемента */
    public static void gaussMain(double[][] matrix,int[] vector) {
        for (int k = 0; k < matrix.length; k++) {
            int maxRow = getMax(matrix, k);
            swapColumns(matrix, k, maxRow, vector);

            gauss(matrix, k);
        }
    }

    /** Реализация метода Гаусса*/
    public static void gauss(double[][] matrix, int k){
        for (int i = k + 1; i < matrix.length; i++) {
            double factor = matrix[i][k] / matrix[k][k];
            for (int j = k + 1; j <= matrix.length; j++) {
                matrix[i][j] -= factor * matrix[k][j];
            }
        }
    }

    /** Возвращает наибольший элемент в строке */
    public static int getMax(double[][] matrix, int k){
        int maxRow = k;
        for (int i = k + 1; i < matrix.length; i++) {
            if (Math.abs(matrix[k][i]) > Math.abs(matrix[k][maxRow])) {
                maxRow = i;
            }
        }
        return maxRow;
    }

    /** Перестановка столбцов в матрице*/
    public static void swapColumns(double[][] matrix, int col1, int col2,int[] vector) {
        for (int i = 0; i < matrix.length; i++) {
            double temp = matrix[i][col1];
            matrix[i][col1] = matrix[i][col2];
            matrix[i][col2] = temp;
        }
        int tempV = vector[col1];
        vector[col1] = vector[col2];
        vector[col2] = tempV;
    }

    /** Обратный ход */
    private static double[] backSubstitution(double[][] matrix) {
        int n = matrix.length;
        double[] solution = new double[n];

        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * solution[j];
            }
            solution[i] = (matrix[i][n] - sum) / matrix[i][i];
        }
        return solution;
    }

    /** Вывод и подсчёт невязки */
    public static void discrepancy(double[][] matrix,double[] a){
        System.out.println("Невязка: ");
        double[] x = new double[4];
        for (int j = 0; j < matrix.length; j++) {
            x[j] = matrix[j][4] - (matrix[j][0] * a[0] +
                    matrix[j][1] * a[1] + matrix[j][2] * a[2] +
                    matrix[j][3] * a[3]);
        }
        System.out.println(getMaxDouble(x));
        System.out.println();
        System.out.println();
    }

    /** Возвращает максимальное по модулю значение невязки */
    public static double getMaxDouble(double[] array){
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (Math.abs(array[i]) > Math.abs(max)){
                max = Math.abs(array[i]);
            }
        }
        return Math.abs(max);
    }

    /** Вывод и подсчёт итоговых значений для Гаусса с выбором гласного элемента
     * (второе значение - вектор перестановки) */
    public static double[] getValuesMain(double[][] matrix, int[] vector){
        double[] vectors = new double[matrix.length];
        double[] solution = backSubstitution(matrix);

        for (int i = 0; i < matrix.length; i++) {
            int j = vector[i];
            vectors[j] = solution[i];
        }
        for (int i = 0; i < matrix.length; i++) {
            System.out.println("x" + i + " = " + vectors[i]);
        }
        System.out.println();

        return vectors;
    }

    /** Вывод и подсчёт итоговых значений для метода Гаусса */
    public static double[] getValues(double[][] matrix){
        double[] solution = backSubstitution(matrix);

        for (int i = 0; i < matrix.length; i++) {
            System.out.println("x" + i + " = " + solution[i]);
        }
        System.out.println();

        return solution;
    }

    /** Вывод матрицы в консоль */
    public static void printMatrix(double[][] matrix, int n){
        textMatrix(n);

        for (int i = 0; i < matrix.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] < 0) {
                    sb.append(String.format("%.15f  ", matrix[i][j]));
                }else{
                    sb.append(String.format("%.15f   ", matrix[i][j]));
                }
            }
            System.out.println(sb.toString().trim());
        }
        System.out.println();
    }

    public static void textMatrix(int n){
        if (n == -1) System.out.println("Матрица, в которой квадратные корни подсчитаны с машинной точностью: ");
        else System.out.println("Матрица, в которой квадратные корни подсчитаны с " + n + " знаков после запятой: ");
    }

    public static void textSlay(int n){
        if (n == -1) System.out.println("Решение СЛАУ, в которой квадратные корни подсчитаны с машинной точностью: ");
        else System.out.println("Решение СЛАУ, с округлением квадратных корней до " + n + " знаков после запятой: ");
    }

}