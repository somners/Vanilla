/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.data;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.math.vector.Vector3;

public enum PaintingType {
	KEBAB("Kebab", 16, 16),
	AZTEC("Aztec", 16, 16),
	ALBAN("Alban", 16, 16),
	AZTEC_2("Aztec2", 16, 16),
	BOMB("Bomb", 16, 16),
	PLANT("Plant", 16, 16),
	WASTELAND("Wasteland", 16, 16),
	WANDERER("Wanderer", 16, 32),
	GRAHAM("Graham", 16, 32),
	POOL("Pool", 32, 16),
	COURBET("Courbet", 32, 16),
	SUNSET("Sunset", 32, 16),
	SEA("Sea", 32, 16),
	CREEBET("Creebet", 32, 16),
	MATCH("Match", 32, 32),
	BUST("Bust", 32, 32),
	STAGE("Stage", 32, 32),
	VOID("Void", 32, 32),
	SKULL_AND_ROSES("SkullAndRoses", 32, 32),
	// WITHER("Wither", 32, 32),
	FIGHTERS("Fighters", 64, 32),
	SKELETON("Skeleton", 64, 64),
	DONKEY_KONG("DonkeyKong", 64, 64),
	POINTER("Pointer", 64, 64),
	PIG_physics("Pigphysics", 64, 64),
	FLAMING_SKULL("Flaming Skull", 64, 64);
	private final String name;
	private final int width, height;
	private static final Map<String, PaintingType> nameMap = new HashMap<String, PaintingType>();

	private PaintingType(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public Point getCenter(BlockFace direction, Point pos) {
		final int x = pos.getBlockX();
		final int y = pos.getBlockY();
		final int z = pos.getBlockZ();

		final int blockWidth = 16;
		int centerHeight = Math.round(height / blockWidth / 2);
		int centerWidth = width / blockWidth / 2;

		if (centerHeight >= 1) {
			centerHeight -= 1;
		}
		if (centerWidth >= 1) {
			centerWidth -= 1;
		}
		int centerX = x;
		int centerY = y + centerHeight;
		int centerZ = z;

		switch (direction) {
			case NORTH:
				centerZ -= centerWidth;
				break;
			case SOUTH:
				centerZ += centerWidth;
				break;
			case EAST:
				centerX -= centerWidth;
				break;
			case WEST:
				centerX += centerWidth;
				break;
		}

		return new Point(pos.getWorld(), centerX, centerY, centerZ);
	}

	public Point getOriginalPos(BlockFace direction, Point pos) {
		final int x = pos.getBlockX();
		final int y = pos.getBlockY();
		final int z = pos.getBlockZ();

		final int blockWidth = 16;
		int centerHeight = Math.round(height / blockWidth / 2);
		int centerWidth = width / blockWidth / 2;

		if (centerHeight >= 1) {
			centerHeight -= 1;
		}
		if (centerWidth >= 1) {
			centerWidth -= 1;
		}
		int centerX = x;
		int centerY = y - centerHeight;
		int centerZ = z;

		switch (direction) {
			case NORTH:
				centerZ += centerWidth;
				break;
			case SOUTH:
				centerZ -= centerWidth;
				break;
			case EAST:
				centerX += centerWidth;
				break;
			case WEST:
				centerX -= centerWidth;
				break;
		}

		return new Point(pos.getWorld(), centerX, centerY, centerZ);
	}

	static {
		for (PaintingType type : PaintingType.values()) {
			nameMap.put(type.getName(), type);
		}
	}

	public static PaintingType get(String name) {
		return nameMap.get(name);
	}

	public boolean canBePlaced(Block block, BlockFace direction) {
		boolean good = true;
		// A value higher than 0 means that it takes more block than just 1
		int blockHeight = getHeight() / 16 - 1;
		int blockWidth = getWidth() / 16 - 1;
		for (int height = 0; height <= blockHeight && good; height++) {
			for (int width = 0; width <= blockWidth && good; width++) {
				Vector3 vector = Vector3.ZERO;
				switch (direction) {
					case NORTH:
					case SOUTH:
						vector = vector.add(0, 0, width);
						break;
					case EAST:
					case WEST:
						vector = vector.add(width, 0, 0);
						break;
				}
				if (!block.translate(direction.getOpposite().getOffset()).translate(vector.add(0, height, 0)).getMaterial().equals(BlockMaterial.AIR)
						|| block.translate(vector.add(0, height, 0)).getMaterial().equals(BlockMaterial.AIR)) {
					good = false;
				}
			}
		}
		return good;
	}
}
