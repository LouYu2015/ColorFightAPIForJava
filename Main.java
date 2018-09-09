public class Main {

    public static void main(String[] args) {
        ColorFightClient game = new ColorFightClient();
        // game.enableDebugMode();
        // game.clearToken();

        game.joinGame("louyu5");
        double loopUpdateTime = 0;

        while(true) {
            for (ColorFightCell cell: game.cellInfo) {

                int x = cell.getX(), y = cell.getY();

                if (game.canAttack(x, y, loopUpdateTime)) {
                    game.attackCell(x, y);
                    game.sleep((long)cell.getTakeTime() * 1000);
                    game.refresh();
                }
            }

            game.LOGGER.info("Scanned all cells.");
            game.refresh();
            loopUpdateTime = game.getLastUpdateTime();
        }
    }
}