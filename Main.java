import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Create a new game client
        ColorFightClient game = new ColorFightClient();

        // If you want to discard last game session, you can uncomment the following line
        // game.clearToken();

        // Connect to game server. You can change "test" to your username
        game.joinGame("test");

        while(true) {
            // Store all cells that we want to attack
            List<ColorFightCell> targets = new LinkedList<>();

            // Loop through all cells to find targets
            for (ColorFightCell cell: game.cellInfo) {
                // Get coordinate of cell
                int x = cell.getX(), y = cell.getY();

                // If this location can be attacked, add to target
                if (game.canAttack(x, y)) {
                    targets.add(cell);
                }
            }

            // Start attacking
            for (ColorFightCell cell: targets)
            {
                // Get target location
                int x = cell.getX(), y = cell.getY();

                // Attack
                game.attackCell(x, y);

                // Wait for attack to be finished
                game.sleep((long)cell.getTakeTime() * 1000);

                // Refresh game status
                game.refresh();
            }

            // Print information
            game.LOGGER.info("Finished one iteration.");

            // Refresh game status
            game.refresh();
        }
    }
}
