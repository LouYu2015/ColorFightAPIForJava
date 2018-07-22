public class Main {

    public static void main(String[] args) {
        ColorFightClient game = new ColorFightClient();
        // game.enableDebugMode();

        game.joinGame("louyu");
        while(true) {
            for (ColorFightCell cell: game.cellInfo) {
                int x = cell.getX(), y = cell.getY();

                if (game.canAttack(x, y)) {
                    game.attackCell(x, y);

                    try {
                        Thread.sleep((long) (cell.getTakeTime() * 1000 + 500));
                    } catch (InterruptedException e) {}

                    game.refresh();
                }
            }

            game.LOGGER.info("Scanned all cells.");
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {}
        }
    }
}