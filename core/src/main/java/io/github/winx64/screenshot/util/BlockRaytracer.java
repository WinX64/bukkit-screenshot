package io.github.winx64.screenshot.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class BlockRaytracer extends BlockIterator {

    private final Vector position;
    private final Vector direction;

    private Block lastBlock;
    private BlockFace currentFace;

    public BlockRaytracer(Location loc) {
        super(loc);

        this.position = loc.toVector();
        this.direction = loc.getDirection();
    }

    public BlockFace getIntersectionFace() {
        if (currentFace == null) {
            throw new IllegalStateException("Called before next()");
        }

        return currentFace;
    }

    public Vector getIntersectionPoint() {
        BlockFace lastFace = getIntersectionFace();
        Vector planeNormal = new Vector(lastFace.getModX(), lastFace.getModY(), lastFace.getModZ());
        Vector planePoint = lastBlock.getLocation().add(0.5, 0.5, 0.5).toVector()
                .add(planeNormal.clone().multiply(0.5));

        return MathUtil.getLinePlaneIntersection(position, direction, planePoint, planeNormal, true);
    }

    @Override
    public Block next() {
        Block currentBlock = super.next();
        currentFace = lastBlock == null ? BlockFace.SELF : currentBlock.getFace(lastBlock);

        return (lastBlock = currentBlock);
    }
}
