import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.*;
import java.net.*;
import org.json.*;

class MyXMLFormatter extends XMLFormatter
{
    public String getHead(Handler h)
    {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> \n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"logging.xsl\"?> \n" +
                "<!DOCTYPE log SYSTEM \"logger.dtd\">\n" +
                "<log>";
    }
}

public class ColorFightClient {
    private String token = ""; // For authentication
    private String password = ""; // Player's password
    private int uid; // Players' User ID
    private JSONObject gameInfo; // Game information
    public List<ColorFightCell> cellInfo; // Cell information
    private double lastUpdateTime = -1; // Timestamp of last refresh

    public final String SERVER_URL; // Server url
    public final Logger LOGGER; // Log handler
    public static final String CONFIGURATION_FILE_PATH = "configuration.txt"; // File to save token
    public static final String LOG_FILE_PATH = "logging.xml"; // File to save log

    public ColorFightPlayer myInfo; // Player's information
    public List<ColorFightPlayer> otherPlayerInfo; // Other players' information

    public static final int PROTOCOL_DELTA_REFRESH = 1; // Protocol ID for delta refresh
    public static final int PORTOCOL_COMPLETE_REFRESH = 2; // Protocol ID for complete refresh

    public ColorFightClient(String serverURL) {
        // Create log handler
        LOGGER = Logger.getLogger("ColorFight");

        // Create file hander
        try {
            Handler fileHandler = new FileHandler(LOG_FILE_PATH);
            XMLFormatter xmlFormatter = new MyXMLFormatter();

            fileHandler.setFormatter(xmlFormatter);
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.warning("Error while creating log file. Log won't be saved to disk. Details: " +
                e.toString());
        }

        // Save server URL
        SERVER_URL = serverURL;
        LOGGER.info("Server address is " + serverURL);

        if (!SERVER_URL.endsWith("/"))
            LOGGER.warning("Correct server URL should end with \"/\", but current URL is " + SERVER_URL);
    }

    public ColorFightClient() {
        this("http://colorfightuw.herokuapp.com/");
    }

    private void loadToken() {
        try {
            // Read configuration fron file
            File file = new File(CONFIGURATION_FILE_PATH);
            FileInputStream fin = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];

            fin.read(data);
            fin.close();

            // Parse token
            JSONObject config = new JSONObject(new String(data, "UTF-8"));

            token = config.getString("token");

            LOGGER.fine("Succesfully loaded token \"" + token + "\"");
        } catch (FileNotFoundException e) {
            LOGGER.warning("Didn't found token for existing user. Will create new user.");
            LOGGER.warning(e.toString());
        } catch (UnsupportedEncodingException e) {
            LOGGER.severe("Failed to parse configuration file because it is not encoded in UTF-8.");
            LOGGER.severe(e.toString());
        } catch (JSONException e) {
            LOGGER.severe("Configuration file is malformed.");
            LOGGER.severe(e.toString());
        } catch (IOException e) {
            LOGGER.severe("Configuration file is malformed.");
            LOGGER.severe(e.toString());
        }
    }

    private void saveToken() {
        JSONObject config = new JSONObject();
        config.put("token", token);

        try {
            FileWriter fout = new FileWriter(CONFIGURATION_FILE_PATH);
            fout.write(config.toString());
            fout.close();
            LOGGER.info("Token saved to " + CONFIGURATION_FILE_PATH);
        } catch (IOException e) {
            LOGGER.severe("Failed to save token to file.");
            LOGGER.severe(e.toString());
        }
    }

    public void enableDebugMode() {
        LOGGER.setLevel(Level.ALL);
    }

    public void disableLogging() {
        LOGGER.setLevel(Level.OFF);
    }

    public void clearToken() {
        token = "";
        saveToken();
    }

    public void setPassword(String password) {
        this.password = password;
    }


    private JSONObject sendAction(String actionName, JSONObject parameters) {
        String urlPath = SERVER_URL + actionName;
        try {
            // Establish connection
            URL url = new URL(urlPath);

            LOGGER.info("Connecting to " + urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String content = parameters.toString();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestProperty("content-length", Integer.toString(content.getBytes().length));

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Send data
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.writeBytes(parameters.toString());
            writer.flush();
            writer.close();

            // Receive response
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String strResponse = reader.readLine();
            JSONObject response = new JSONObject(strResponse);

            // Check error code
            if (response.has("err_code") && response.getInt("err_code") != 0) {
                LOGGER.severe("Server responded game error code: " + response.getInt("err_code"));
                if (response.has("err_msg")) {
                    LOGGER.severe("Error message is: " + response.getString("err_msg"));
                }
            } else {
                LOGGER.fine("Request success. Server responded with " + strResponse);
            }

            // Done
            return response;

        } catch (IOException e) {
            LOGGER.severe("Error while connecting to " + urlPath);
            LOGGER.severe(e.toString());
        }
        return new JSONObject("{}");
    }

    private JSONObject sendActionWithToken(String actionName, JSONObject parameters) {
        parameters.put("token", token);
        return sendAction(actionName, parameters);
    }

    private JSONObject tokenLogin(String username)
    {
        LOGGER.info("Using token " + token);

        JSONObject requestData = new JSONObject();
        requestData.put("token", token);

        JSONObject result = sendAction("checktoken", requestData);
        result.put("token", token);

        String acturalUserName = result.getString("name");
        if (!acturalUserName.equals(username)) {
            LOGGER.warning("The token is for \"" + result.getString("name") +
                    "\", but your username is \"" + username + "\"." +
                    "Use clearToken() to force creating new user.");
        }

        return result;
    }

    public void joinGame(String username) {
        LOGGER.info("Joining game with username as \"" + username + "\"");
        LOGGER.config("Server address was set to " + SERVER_URL);

        loadToken();

        JSONObject result;

        if (token.isEmpty()) {
            JSONObject requestData = new JSONObject();
            requestData.put("name", username);
            requestData.put("password", password);

            result = sendAction("joingame", requestData);

            token = result.getString("token");

            LOGGER.info("Joined game as new user.");

            saveToken();
        } else {
            result = tokenLogin(username);
        }

        uid = result.getInt("uid");

        LOGGER.info("Success! UID is " + uid);
        LOGGER.info("Automatically refresh game data.");
        refresh();
    }

    public void refresh() {
        refresh(false);
    }

    public void refresh(boolean forceCompleteRefresh) {
        JSONObject requestData = new JSONObject();
        if (lastUpdateTime < 0 || forceCompleteRefresh) {
            LOGGER.info("Doing complete refresh");
            requestData.put("protocol", PORTOCOL_COMPLETE_REFRESH);

            JSONObject respond = sendAction("getgameinfo", requestData);

            refreshUsers(respond.getJSONArray("users"));
            gameInfo = respond.getJSONObject("info");

            JSONArray cells = respond.getJSONArray("cells");
            cellInfo = new ArrayList<>();
            for (int i = 0; i < cells.length(); i++) {
                cellInfo.add(new ColorFightCell(cells.getJSONObject(i)));
            }
        } else {
            LOGGER.info("Doing delta refresh");
            requestData.put("protocol", PROTOCOL_DELTA_REFRESH);
            requestData.put("timeAfter", lastUpdateTime);

            JSONObject respond = sendAction("getgameinfo", requestData);
            refreshUsers(respond.getJSONArray("users"));
            gameInfo = respond.getJSONObject("info");

            JSONArray newCells = respond.getJSONArray("cells");
            for (int i = 0; i < newCells.length(); i++) {
                ColorFightCell currentCell = new ColorFightCell(newCells.getJSONObject(i));
                setCell(currentCell.getX(), currentCell.getY(), currentCell);
            }
            updateCells();
        }
        lastUpdateTime = getLastUpdateTime();
    }

    private void refreshUsers(JSONArray userData) {
        LOGGER.fine("Updating user data");
        otherPlayerInfo = new LinkedList<ColorFightPlayer>();
        for (int i = 0; i < userData.length(); i++) {
            ColorFightPlayer player = new ColorFightPlayer(userData.getJSONObject(i));

            if (player.getID() == uid) {
                myInfo = player;
            } else {
                otherPlayerInfo.add(player);
            }
        }
    }

    private int getCID(int x, int y) {
        return x + y * getWidth();
    }

    private void setCell(int x, int y, ColorFightCell data) {
        cellInfo.set(getCID(x, y), data);
    }

    public static double calculateTakeTime(double timeDiff) {
        if (timeDiff <= 0) {
            return 33;
        } else {
            return 30*(Math.pow(2, -timeDiff/30.0)) + 3;
        }
    }

    private void updateCells() {
        for (ColorFightCell currentCell: cellInfo) {
            if (currentCell.isTaking()) {
                currentCell.setTakeTime(-1);
            } else if (currentCell.getOwnerUID() == 0) {
                currentCell.setTakeTime(2);
            } else {
                double timeDiff = getLastUpdateTime() - currentCell.getOccupyTime();
                currentCell.setTakeTime(calculateTakeTime(timeDiff));
            }
        }
    }

    public void attackCell(int x, int y, boolean boost) {
        LOGGER.info("Attacking (" + x + ", " + y + ").");
        JSONObject request = new JSONObject();
        request.put("cellx", x);
        request.put("celly", y);
        request.put("boost", boost);

        sendActionWithToken("attack", request);
    }

    public void attackCell(int x, int y) {
        attackCell(x, y, false);
    }

    public void buildBase(int x, int y) {
        JSONObject request = new JSONObject();
        request.put("cellx", x);
        request.put("celly", y);

        sendActionWithToken("buildbase", request);
    }

    public void blast(int x, int y, BlastDirection direction) {
        JSONObject request = new JSONObject();
        request.put("cellx", x);
        request.put("celly", y);
        request.put("direction", direction.toString());

        sendActionWithToken("blast", request);
    }

    public void multiAttack(int x, int y) {
        JSONObject request = new JSONObject();
        request.put("cellx", x);
        request.put("celly", y);

        sendActionWithToken("multiattack", request);
    }

    public boolean canAttack(int x, int y) {
        return canAttack(x, y, 0);
    }

    public boolean canAttack(int x, int y, double timeFilter) {
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

        ColorFightCell currentCell = getCell(x, y);
        if (currentCell.isEdge() || currentCell.getOwnerUID() == uid) {
            return false;
        }

        for (int[] direction: directions) {
            ColorFightCell nextCell = getCell(x + direction[0], y + direction[1]);
            if (nextCell.getOwnerUID() == uid
                    && nextCell.getAttackTime() < timeFilter) {
                return true;
            }
        }
        return false;
    }

    public int getWidth() {
        return gameInfo.getInt("width");
    }

    public int getHeight() {
        return gameInfo.getInt("height");
    }

    public double getLastUpdateTime() {
        return gameInfo.getDouble("time");
    }

    public double getEndTime() {
        return gameInfo.getDouble("end_time");
    }

    public double getJoinEndTime() {
        return gameInfo.getDouble("join_end_time");
    }

    public int getGameID() {
        return gameInfo.getInt("game_id");
    }

    public ColorFightCell getCell(int x, int y) {
        LOGGER.fine("Reading cell data at (" + x + ", " + y + ")");
        if (x >= 0 && x < getWidth()
            && y >= 0 && y < getHeight()) {
            return cellInfo.get(getCID(x, y));
        } else {
            LOGGER.fine("Reached edge of map");
            return new ColorFightCell(new JSONObject("{\"o\": -1}"));
        }
    }

    public void sleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            LOGGER.warning("Program interrupted. Exiting...");
            // Because we converted InterruptedException to RuntimeException,
            // we should restore the flag to notify other part of the program about this interruption
            // so that the program can still respond to this signal.
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        }
    }
}
