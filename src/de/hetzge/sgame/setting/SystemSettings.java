package de.hetzge.sgame.setting;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class SystemSettings implements Serializable {

	private String playerName = "Someone";

	private short resolutionX = 800;
	private short resolutionY = 600;

	private float cameraMoveSpeed = 10f;
	private float cameraZoomSpeed = 0.02f;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		if (StringUtils.isEmpty(playerName)) {
			// TODO error
		}
		this.playerName = playerName;
	}

	public short getResolutionX() {
		return resolutionX;
	}

	public void setResolutionX(short resolutionX) {
		this.resolutionX = resolutionX;
	}

	public short getResolutionY() {
		return resolutionY;
	}

	public void setResolutionY(short resolutionY) {
		this.resolutionY = resolutionY;
	}

	public float getCameraMoveSpeed() {
		return cameraMoveSpeed;
	}

	public void setCameraMoveSpeed(float cameraMoveSpeed) {
		this.cameraMoveSpeed = cameraMoveSpeed;
	}

	public float getCameraZoomSpeed() {
		return cameraZoomSpeed;
	}

	public void setCameraZoomSpeed(float cameraZoomSpeed) {
		this.cameraZoomSpeed = cameraZoomSpeed;
	}

}
