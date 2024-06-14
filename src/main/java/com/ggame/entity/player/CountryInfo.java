package com.ggame.entity.player;

public class CountryInfo {
	private String gameId; // Mã game
	private int idLanguage; // id quốc gia
	private int index; // thứ tự sắp xếp
	private String code; // Mã Zip Code quốc gia
	private String name; // Tên quốc gia hiển thị
	private String nameCode; // Tên quốc gia so sánh code

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getIdLanguage() {
		return idLanguage;
	}

	public void setIdLanguage(int idLanguage) {
		this.idLanguage = idLanguage;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameCode() {
		return nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}

}
