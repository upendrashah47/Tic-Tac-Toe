package com.us47codex.tictactoe.util;

import org.json.JSONException;
import org.json.JSONObject;

public class DBSetter {
    public String getString(JSONObject jsonObject, String name) {
        String _string = "";
        try {
            if (jsonObject.has(name)) {
                if (!jsonObject.getString(name).equals("null")
                        || !jsonObject.getString(name).equals(null)) {
                    _string = getDBStr(jsonObject.getString(name));
                }
            } else {
                Log.print("==================================");
                Log.print("==================================");
                Log.print("=========== getString NOT FOUND =============" + name);
                Log.print("==================================");
                Log.print("==================================");
            }
        } catch (JSONException e) {
            Log.error(getClass().getName() + "::getString()::", e);
            e.printStackTrace();
        }
        return _string;
    }

    public int getInt(JSONObject jsonObject, String name) {
        int _int = 0;
        try {
            if (jsonObject.has(name)) {
                if (!jsonObject.getString(name).equals("null") || !jsonObject.getString(name).equals(null)) {
                    _int = jsonObject.getInt(name);
                }
            } else {
                Log.print("==================================");
                Log.print("==================================");
                Log.print("=========== getInt NOT FOUND =============" + name);
                Log.print("==================================");
                Log.print("==================================");
            }
        } catch (JSONException e) {
            Log.error(getClass().getName() + "::getInt()::", e);
            e.printStackTrace();
        }
        return _int;
    }

    public double getDouble(JSONObject jsonObject, String name) {
        double _double = 0;
        try {
            if (jsonObject.has(name)) {
                if (!jsonObject.getString(name).equals("null")
                        || !jsonObject.getString(name).equals(null)) {
                    _double = jsonObject.getDouble(name);
                }
            } else {
                Log.print("==================================");
                Log.print("==================================");
                Log.print("=========== getDouble NOT FOUND =============" + name);
                Log.print("==================================");
                Log.print("==================================");
            }
        } catch (JSONException e) {
            Log.error(getClass().getName() + "::getDouble()::", e);
            e.printStackTrace();
        }
        return _double;
    }

    public long getLong(JSONObject jsonObject, String name) {
        long _long = 0;
        try {
            if (jsonObject.has(name)) {
                if (!jsonObject.getString(name).equals("null")
                        || !jsonObject.getString(name).equals(null)) {
                    _long = jsonObject.getLong(name);
                }
            } else {
                Log.print("==================================");
                Log.print("==================================");
                Log.print("=========== getLong NOT FOUND =============" + name);
                Log.print("==================================");
                Log.print("==================================");
            }
        } catch (JSONException e) {
            Log.error(getClass().getName() + "::getLong()::", e);
            e.printStackTrace();
        }
        return _long;
    }

    public boolean getBoolean(JSONObject jsonObject, String name) {
        boolean _boolean = false;
        try {
            if (jsonObject.has(name)) {
                if (!jsonObject.getString(name).equals("null")
                        || !jsonObject.getString(name).equals(null)) {
                    _boolean = jsonObject.getBoolean(name);
                }
            } else {
                Log.print("==================================");
                Log.print("==================================");
                Log.print("=========== getBoolean NOT FOUND =============" + name);
                Log.print("==================================");
                Log.print("==================================");
            }
        } catch (JSONException e) {
            Log.error(getClass().getName() + "::getBoolean()::", e);
            e.printStackTrace();
        }
        return _boolean;
    }

    protected String getDBStr(String str) {
        //str = str != null ? str.replaceAll("'", "''") : null;
        str = str != null ? str.replaceAll("&#039;", "'") : null;
        str = str != null ? str.replaceAll("&amp;", "&") : null;
        return str;
    }
}