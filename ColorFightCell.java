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

    public double getOccupyTime() {
        return cellData.getDouble("ot");
    }

    public double getAttackTime() {
        return cellData.getDouble("at");
    }

    public double getTakeTime() {
        return cellData.getDouble("t");
    }

    public void setTakeTime(double takeTime) {
        cellData.put("t", takeTime);
    }

    public double getFinishTime() {
        return cellData.getDouble("f");
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

    public double getBuildTime() {
        return cellData.getDouble("bt");
    }

    public String toString() {
        return "{cell at (" + getX() + ", " + getY() + ");" +
                "occupied by " + getOwnerUID() + "}";
    }
}
