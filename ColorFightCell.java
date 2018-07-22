import org.json.*;

public class ColorFightCell {
    private JSONObject cellData;

    public ColorFightCell(JSONObject cellData) {
        this.cellData = cellData;
    }

    public int getOwnerUID() {
        return cellData.getInt("o");
    }

    public boolean isEdge() {
        return getOwnerUID() == -1;
    }

    public boolean isEmpty() {
        return getOwnerUID() == 0;
    }

    public int getAttackerUID() {
        return cellData.getInt("a");
    }

    public boolean isTaking() {
        return cellData.getInt("c") == 1;
    }

    public int getX() {
        return cellData.getInt("x");
    }

    public int getY() {
        return cellData.getInt("y");
    }

    public float getOccupyTime() {
        return cellData.getFloat("ot");
    }

    public float getAttackTime() {
        return cellData.getFloat("at");
    }

    public float getTakeTime() {
        return cellData.getFloat("t");
    }

    public void setTakeTime(float takeTime) {
        cellData.put("t", takeTime);
    }

    public float getFinishTime() {
        return cellData.getFloat("f");
    }

    public String getCellType() {
        return cellData.getString("ct");
    }

    public String getBuildType() {
        return cellData.getString("b");
    }

    public boolean isBase() {
        return getBuildType().equals("base");
    }

    public boolean isBuilding() {
        return !cellData.getBoolean("bf");
    }

    public float getBuildTime() {
        return cellData.getFloat("bt");
    }

    public String toString() {
        return "{cell at (" + getX() + ", " + getY() + ");" +
                "occupied by " + getOwnerUID() + "}";
    }
}
