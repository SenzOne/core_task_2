import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class GameApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final int WIN_COUNT = 3;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';
    private static final int fieldSizeX = 5;
    private static final int fieldSizeY = 5;
    private static char[][] field;

    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.println("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }

    }

    /**
     * Инициализация объектов игры
     */
    static void initialize() {
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */

    static void printField(){
        System.out.print("+");
        for(int x = 0; x < fieldSizeX; x++){
            System.out.print("-" + (x + 1));
        }
        System.out.println("-");


        for(int x = 0; x < fieldSizeX; x++){
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++){
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for(int x = 0; x < fieldSizeX * 2 + 2; x++){
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn() {
        int x;
        int y;
        do {
            System.out.println("Введите координаты хода Y и X\n(от 1 до 3) через пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }


    /**
     * Проверка, является ли ячейка игрового поля пустой
     */
    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка валидности координат хода
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }


    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn() {
        String turn = getCountMark(DOT_HUMAN);

        switch (turn) {
            case "horizontal1" -> horizontal(1);
            case "horizontal2" -> horizontal(2);
            case "horizontal3" -> horizontal(3);
            case "vertical1" -> vertical(1);
            case "vertical2" -> vertical(2);
            case "vertical3" -> vertical(3);
            case "mainDiagonal" -> mainDiagonal();
            case "diagonal" -> diagonal();
            default -> randomTurn();
        }
    }

    private static void randomTurn() {
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    /**
     * Проверка на ничью
     */
    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    /**
     * TODO: Переработать в рамках домашней работы
     * Метод проверки победы
     *
     * @param dot фишка игрока
     */
    static boolean checkWin(char dot) {
        // Проверка победы по горизонталям
        for (int i = 0; i < fieldSizeX; i++) {
            if (field[i][0] == dot && field[i][1] == dot && field[i][2] == dot) {
                return true;
            }
        }

        // Проверка победы по вертикалям
        for (int j = 0; j < fieldSizeY; j++) {
            if (field[0][j] == dot && field[1][j] == dot && field[2][j] == dot) {
                return true;
            }
        }

        // Проверка победы по диагоналям
        if ((field[0][0] == dot && field[1][1] == dot && field[2][2] == dot) ||
            (field[0][2] == dot && field[1][1] == dot && field[2][0] == dot)) {
            return true;
        }
        return false;
    }

    static void horizontal(int row) {
        for (int x = 0; x < fieldSizeX; x++) {
            while (isCellEmpty(row -1, x)) {
                field[row -1][x] = DOT_AI;
                return;
            }
        }
    }

    static void vertical(int coll) {
        for (int y = 0; y < fieldSizeY; y++) {
            while (isCellEmpty(coll- 1, y)) {
                field[y][coll - 1] = DOT_AI;
                return;
            }
        }
    }

    static void mainDiagonal() {
        for (int i = 0; i < fieldSizeX; i++) {
            while (isCellEmpty(i, i)) {
                field[i][i] = DOT_AI;
                return;
            }
        }
    }

    static void diagonal() {
        for (int i = 0; i < fieldSizeX; i++) {
            while (isCellEmpty(i, i)) {
                field[i][2 - i] = DOT_AI;
                return;
            }
        }
    }

    static String getCountMark(char dot) {
        Map<String, Integer> resMap = new HashMap<>();
        // Счетчик поставленных меток по горизонталям
        int[] horizontalMarksCount = new int[fieldSizeX];
        // Счетчик поставленных меток по вертикалям
        int[] verticalMarksCount = new int[fieldSizeY];
        // Счетчик поставленных меток по диагоналям
        int mainDiagonalMarksCount = 0;
        int diagonalMarksCount = 0;

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (field[i][j] == dot) {
                    horizontalMarksCount[i]++;
                    verticalMarksCount[j]++;
                    // Проверка на диагонали
                    if (i == j) mainDiagonalMarksCount++;
                    if (i + j == fieldSizeX - 1) diagonalMarksCount++;
                }
            }
        }

        // Заполнение map с подсчетами
        for (int i = 0; i < fieldSizeX; i++) {
            resMap.put("horizontal" + (i + 1), horizontalMarksCount[i]);
            resMap.put("vertical" + (i + 1), verticalMarksCount[i]);
        }
        resMap.put("mainDiagonal", mainDiagonalMarksCount);
        resMap.put("diagonal", diagonalMarksCount);
        return resMap.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("random");
    }


    /**
     * Проверка состояния игры
     *
     * @param dot фишка игрока
     * @param s   победный слоган
     * @return
     */
    static boolean checkState(char dot, String s) {
        if (checkWin(dot)) {
            System.out.println(s);
            return true;
        } else if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }
}
