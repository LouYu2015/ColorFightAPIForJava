import java.util.*;

public class Main {

    public static void main(String[] args) {
        ColorFightClient game = new ColorFightClient();
        // game.enableDebugMode();
        // game.clearToken();

        game.joinGame("test");

        while(true) {
            List<ColorFightCell> attackable = new LinkedList<>();

            for (ColorFightCell cell: game.cellInfo) {
                int x = cell.getX(), y = cell.getY();

                if (game.canAttack(x, y)) {
                    attackable.add(cell);
                }
            }

            for (ColorFightCell cell: attackable)
            {
                int x = cell.getX(), y = cell.getY();

                game.attackCell(x, y);
                game.sleep((long)cell.getTakeTime() * 1000);
                game.refresh();
            }

            game.LOGGER.info("Scanned all cells.");
            game.refresh();
        }
    }
}