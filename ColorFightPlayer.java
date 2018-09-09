import org.json.*;

public class ColorFightPlayer {
    private JSONObject userData;

    public ColorFightPlayer(JSONObject userData) {
        this.userData = userData;
    }

    public int getID() {
        return userData.getInt("id");
    }

    public String getName() {
        return userData.getString("name");
    }

    public double getCDTime() {
        return userData.getDouble("cd_time");
    }

    public double getBuildCDTime() {
        return userData.getDouble("build_cd_time");
    }

    public int getCellNum() {
        return userData.getInt("cell_num");
    }

    public int getBaseNum() {
        return userData.getInt("base_num");
    }

    public int getGoldCellNum() {
        return userData.getInt("gold_cell_num");
    }

    public int getEnergyCellNum() {
        return userData.getInt("energy_cell_num");
    }

    public double getEnergy() {
        return userData.getDouble("energy");
    }

    public double getGold() {
        return userData.getDouble("gold");
    }

    public String toString() {
        return "user(name=" + getName() + ", uid=" + getID() + ")";
    }
}
