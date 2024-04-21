import java.util.*;

public class GameApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
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

    static void initialize() {
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    static void printField() {
        System.out.print("+");
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print("-" + (x + 1));
        }
        System.out.println("-");

        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int x = 0; x < fieldSizeX * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
    }

    static void humanTurn() {
        int x;
        int y;
        do {
            System.out.println("Введите координаты хода Y и X\n(от 1 до " + fieldSizeX + ") через пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    static void aiTurn() {
        // Проверка возможных победных ходов
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = DOT_AI;
                    if (checkWin(DOT_AI)) {
                        return;
                    }
                    field[i][j] = DOT_EMPTY; // Вернуть поле в исходное состояние
                }
            }
        }

        // Блокировка ходов противника
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN)) {
                        field[i][j] = DOT_AI;
                        return;
                    }
                    field[i][j] = DOT_EMPTY; // Вернуть поле в исходное состояние
                }
            }
        }

        // Случайный ход, если нет явных выигрышных ходов
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    static boolean checkWin(char dot) {
        // Проверка победы по горизонталям
        for (int i = 0; i < fieldSizeX; i++) {
            if (checkLine(i, 0, 1, 0, dot)) return true;
        }

        // Проверка победы по вертикалям
        for (int j = 0; j < fieldSizeY; j++) {
            if (checkLine(0, j, 0, 1, dot)) return true;
        }

        // Проверка победы по диагоналям
        if (checkLine(0, 0, 1, 1, dot)) return true;
        if (checkLine(0, fieldSizeY - 1, 1, -1, dot)) return true;

        return false;
    }

    static boolean checkLine(int startX, int startY, int dx, int dy, char dot) {
        for (int i = 0; i < fieldSizeX; i++) {
            int x = startX + i * dx;
            int y = startY + i * dy;
            if (x < 0 || x >= fieldSizeX || y < 0 || y >= fieldSizeY) return false;
            if (field[x][y] != dot) return false;
        }
        return true;
    }

    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

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
