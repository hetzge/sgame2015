package de.hetzge.sgame.game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.hetzge.sgame.entity.E_EntityType;
import de.hetzge.sgame.entity.Entity;
import de.hetzge.sgame.game.input.E_Cursor;
import de.hetzge.sgame.game.input.IF_InputMode;
import de.hetzge.sgame.game.input.InputModeCommandUnits;
import de.hetzge.sgame.game.input.MouseEventPosition;

/**
 * This class contains information about the current running game which are only
 * visible for the local client and not shared with the server. The information
 * here is not relevant for the normal progress of the game. It contains the
 * state of the user input.
 * 
 * @author hetzge
 */
public class LocalGameState {

	private MouseEventPosition mouseDownEventPosition;

	private E_EntityType buildEntityType = null;
	private E_Cursor cursor = E_Cursor.DEFAULT;
	private IF_InputMode inputMode = new InputModeCommandUnits();

	private final Set<Entity> selection = new HashSet<>();
	private E_EntityType entityTypeToBuild = null;

	private boolean showPaths = false;
	private boolean showCollisions = false;
	private boolean showRegistrations = false;
	private boolean showIds = false;
	private boolean showWorldOwner = false;
	private boolean showDoors = false;
	private boolean showWorldOwnerColor = false;
	private boolean showWorldOwnerBorder = true;

	public void unsetMouseDownEventPosition() {
		this.mouseDownEventPosition = null;
	}

	public void setMouseDownEventPosition(MouseEventPosition mouseDownEventPosition) {
		this.mouseDownEventPosition = mouseDownEventPosition;
	}

	public MouseEventPosition getMouseDownEventPosition() {
		return this.mouseDownEventPosition;
	}

	public void setBuildEntityType(E_EntityType buildEntityType) {
		this.buildEntityType = buildEntityType;
	}

	public E_EntityType getBuildEntityType() {
		return this.buildEntityType;
	}

	public void setCursor(E_Cursor cursor) {
		this.cursor = cursor;
	}

	public E_Cursor getCursor() {
		return this.cursor;
	}

	public IF_InputMode getInputMode() {
		return this.inputMode;
	}

	public void setInputMode(IF_InputMode inputMode) {
		this.inputMode = inputMode;
	}

	public boolean hasSelection() {
		return !this.selection.isEmpty();
	}

	public void clearSelection() {
		this.selection.clear();
	}

	public Set<Entity> getSelection() {
		return this.selection;
	}

	public List<Entity> getSelection(Predicate<Entity> predicate) {
		return this.selection.stream().filter(predicate).collect(Collectors.toList());
	}

	public void addSelection(Entity entity) {
		this.selection.add(entity);
	}

	public void addSelection(List<Entity> entities) {
		this.selection.addAll(entities);
	}

	public void setSelection(List<Entity> entities) {
		clearSelection();
		this.selection.addAll(entities);
	}

	public int getSelectionSize() {
		return this.selection.size();
	}

	public boolean hasEntityTypeToBuild() {
		return this.entityTypeToBuild != null;
	}

	public E_EntityType getEntityTypeToBuild() {
		return this.entityTypeToBuild;
	}

	public void setEntityTypeToBuild(E_EntityType entityTypeToBuild) {
		clearSelection();
		this.entityTypeToBuild = entityTypeToBuild;
	}

	public boolean isShowPaths() {
		return this.showPaths;
	}

	public void setShowPaths(boolean showPaths) {
		this.showPaths = showPaths;
	}

	public void toggleShowPaths() {
		this.showPaths = !this.showPaths;
	}

	public boolean isShowCollisions() {
		return this.showCollisions;
	}

	public void setShowCollisions(boolean showCollisions) {
		this.showCollisions = showCollisions;
	}

	public void toggleShowCollision() {
		this.showCollisions = !this.showCollisions;
	}

	public boolean isShowRegistrations() {
		return this.showRegistrations;
	}

	public void setShowRegistrations(boolean showRegistrations) {
		this.showRegistrations = showRegistrations;
	}

	public void toggleShowRegistrations() {
		this.showRegistrations = !this.showRegistrations;
	}

	public boolean isShowIds() {
		return this.showIds;
	}

	public void setShowIds(boolean showIds) {
		this.showIds = showIds;
	}

	public void toggleShowIds() {
		this.showIds = !this.showIds;
	}

	public boolean isShowWorldOwner() {
		return this.showWorldOwner;
	}

	public void setShowWorldOwner(boolean showWorldOwner) {
		this.showWorldOwner = showWorldOwner;
	}

	public void toggleShowWorldOwner() {
		this.showWorldOwner = !this.showWorldOwner;
	}

	public boolean isShowDoors() {
		return this.showDoors;
	}

	public void setShowDoors(boolean showDoors) {
		this.showDoors = showDoors;
	}

	public void toggleShowDoors() {
		this.showDoors = !this.showDoors;
	}

	public void toggleShowWorldOwnerBorder() {
		this.showWorldOwnerBorder = !this.showWorldOwnerBorder;
	}

	public void toggleShowWorldOwnerColor() {
		this.showWorldOwnerColor = !this.showWorldOwnerColor;
	}

	public boolean isShowWorldOwnerColor() {
		return this.showWorldOwnerColor;
	}

	public void setShowWorldOwnerColor(boolean showWorldOwnerColor) {
		this.showWorldOwnerColor = showWorldOwnerColor;
	}

	public boolean isShowWorldOwnerBorder() {
		return this.showWorldOwnerBorder;
	}

	public void setShowWorldOwnerBorder(boolean showWorldOwnerBorder) {
		this.showWorldOwnerBorder = showWorldOwnerBorder;
	}

	public E_CommandMode getCommandMode() {
		if (hasSelection()) {
			return E_CommandMode.COMMAND;
		} else if (hasEntityTypeToBuild()) {
			return E_CommandMode.BUILD;
		} else {
			return E_CommandMode.NON;
		}
	}

}
