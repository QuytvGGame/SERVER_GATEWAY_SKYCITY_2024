package com.ggame.network;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Message {

	private JSONObject jsonMesage;
	private JSONObject jsonData;
	private int cmd;

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public Message(int cmd) {
		this.jsonMesage = new JSONObject();
		this.cmd = cmd;
		putCMD(cmd);
		this.jsonData = new JSONObject();
	}

	public Message(String strJson) {
		try {
			this.jsonMesage = new JSONObject(strJson);
			this.cmd = jsonMesage.getInt(Key.Cmd);
			jsonData = jsonMesage.getJSONObject(Key.Data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		try {
			jsonMesage.put(Key.Data, jsonData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonMesage.toString();
	}

	public int getCMD() {
		return cmd;
	}

	public void putCMD(int cmd) {
		try {
			jsonMesage.put(Key.Cmd, cmd);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getInt(String key) {
		try {
			if(jsonData.has(key))
				return jsonData.getInt(key);
			return 0;
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void putBoolean(String key, boolean value) {
		putData(key, value);
	}

	public boolean getBoolean(String key) {
		try {
			return jsonData.getBoolean(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void putInt(String key, int value) {
		putData(key, value);
	}

	public void putObject(String key, Object value) {
		putData(key, value);
	}

	public long getLong(String key) {
		try {
			return jsonData.getLong(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return 0L;
		}
	}

	public void putLong(String key, long value) {
		putData(key, value);
	}
	
	public void putFloat(String key, float value) {
		putData(key, value);
	}
	

	public String getString(String key) {
		try {
			return jsonData.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	public void putString(String key, String value) {
		putData(key, value);
	}

	public JSONArray getArrayJson(String key) {
		try {
			return jsonData.getJSONArray(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void putArrayJson(String key, JSONArray value) {
		putData(key, value);
	}

	public void putJson(String key, JSONObject value) {
		putData(key, value);
	}

	public void setJsonData(JSONObject jsonObject) {
		this.jsonData = jsonObject;
	}

	public void putArrayInt(String key, int[] value) {
		putData(key, value);
	}

	public void putArrayLong(String key, long[] value) {
		putData(key, value);
	}

	public void putListString(String key, List<String> value) {
		putData(key, value);
	}

	private void putData(String key, Object value) {
		try {
			jsonData.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int[] getArrayInt(String key) {
		try {
			JSONArray jsonArray = jsonData.getJSONArray(key);
			int size = jsonArray.length();
			int[] arrayData = new int[size];
			for (int i = 0; i < arrayData.length; i++) {
				arrayData[i] = Integer.parseInt(jsonArray.get(i).toString());
			}
			return arrayData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public long[] getArrayLong(String key) {
		try {
			JSONArray jsonArray = jsonData.getJSONArray(key);
			int size = jsonArray.length();
			long[] arrayData = new long[size];
			for (int i = 0; i < arrayData.length; i++) {
				arrayData[i] = Long.parseLong(jsonArray.get(i).toString());
			}
			return arrayData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String[] getArrayString(String key) {
		try {
			JSONArray jsonArray = jsonData.getJSONArray(key);
			int size = jsonArray.length();
			String[] arrayData = new String[size];
			for (int i = 0; i < arrayData.length; i++) {
				arrayData[i] = jsonArray.get(i).toString();
			}
			return arrayData;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void putArray2String(String key, String[][] value) {
		putData(key, value);
	}

	public String[][] getArray2String(String key) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(jsonData.get(key).toString(), new TypeToken<String[][]>() {
			}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean checkKey(String key) {
		return jsonData.has(key);
	}
}
