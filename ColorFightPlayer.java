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

    public float getCDTime() {
        return userData.getFloat("cd_time");
    }

    public float getBuildCDTime() {
        return userData.getFloat("build_cd_time");
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

    public float getEnergy() {
        return userData.getFloat("energy");
    }

    public float getGold() {
        return userData.getFloat("gold");
    }

    public String toString() {
        return "user(name=" + getName() + ", uid=" + getID() + ")";
    }
}
