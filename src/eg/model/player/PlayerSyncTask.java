package eg.model.player;

import java.util.Iterator;

import eg.global.World;
import eg.net.game.GamePacket;
import eg.util.io.Buffer;
import eg.util.task.Task;

public final class PlayerSyncTask implements Task {
	
	private static final int NEW_PLAYERS_PER_CYCLE = 20;
	
	@Override
	public void execute() {
		
		World.getWorld().getPlayerList().parallelStream()
				.forEach(this::preSyncProcess);
		
		World.getWorld().getPlayerList().parallelStream()
				.forEach(this::syncProcess);
		
		World.getWorld().getPlayerList().parallelStream()
				.forEach(this::postSyncProcess);
	}
	
	private void preSyncProcess(Player player) {
		
		player.getMovement().preSyncProcess();
		player.getActions().preSyncProcess();
		
		if (player.getMovement().isRegionChanging()) {
			player.getSession().send(new GamePacket(73, new Buffer(4)
					.put128PlusShort(player.getCoord().getRegionX())
					.putShort(player.getCoord().getRegionY())
					.getData()));
		}
	}
	
	private void postSyncProcess(Player player) {
		
		player.getMovement().postSyncProcess();
		player.getActions().postSyncProcess();
	}
	
	private void syncProcess(Player player) {
		
		Buffer buf = new Buffer();
		buf.beginBitAccess();
		
		Buffer payloadBuf = new Buffer();
		
		updateThisPlayerMovement(player, buf);
		if (player.getActions().isSyncRequired()) {
			player.getActions().putToBuffer(payloadBuf, false, true);
		}
		
		buf.putBits(8, player.getLocalPlayers().size());
		for (Iterator<Player> i = player.getLocalPlayers().iterator(); i.hasNext();) {
			Player other = i.next();
			if (other.isActive() && !other.getMovement().isTeleporting() &&
					player.getCoord().getBoxDistance(other.getCoord()) <=
					player.getViewingDistance()) {
				updatePlayerMovement(buf, other);
				if (other.getActions().isSyncRequired()) {
					other.getActions().putToBuffer(payloadBuf, false, false);
				}
			} else {
				i.remove();
				buf.putBit(true).putBits(2, 3);
			}
		}
		int added = 0;
		for (Player other : World.getWorld().getPlayerList()) { //TODO players on region
			if (player.getLocalPlayers().size() >= 255) {
				break;
			}
			if ((added >= NEW_PLAYERS_PER_CYCLE)) {
				break;
			}
			if (other == player || !player.isActive() ||
					player.getCoord().getBoxDistance(other.getCoord()) >
					player.getViewingDistance() ||
					player.getLocalPlayers().contains(other)) {
				continue;
			}
			added++;
			player.getLocalPlayers().add(other);
			buf.putBits(11, other.getIndex());
			buf.putBit(true).putBit(true);
			int y = other.getCoord().getY() - player.getCoord().getY();
            int x = other.getCoord().getX() - player.getCoord().getX();
            buf.putBits(5, y).putBits(5, x);
			other.getActions().putToBuffer(payloadBuf, true, false);
		}
		if (payloadBuf.getPosition() != 0) {
			buf.putBits(11, 2047);
			buf.endBitAccess();
			buf.putBytes(payloadBuf.getData(), 0, payloadBuf.getPosition());
		} else {
			buf.endBitAccess();
		}
		player.getSession().send(new GamePacket(81, buf.getData(), buf.getPosition()));
	}
	
	//TODO: Update not required when only chat has changed.
    private void updateThisPlayerMovement(Player player, Buffer buf) {
    	if (player.getMovement().isTeleporting() || player.getMovement().isRegionChanging()) {
    		buf.putBit(true).putBits(2, 3);
    		buf.putBits(2, player.getCoord().getHeight());
    		buf.putBit(player.getMovement().isTeleporting());
    		buf.putBit(player.getActions().isSyncRequired());
    		buf.putBits(7, player.getCoord().getRelativeY(player.getMovement().getLastKnownRegion()));
    		buf.putBits(7, player.getCoord().getRelativeX(player.getMovement().getLastKnownRegion()));
    	} else if (player.getMovement().isRunning()) {
    		buf.putBit(true).putBits(2, 2);
    		buf.putBits(3, player.getMovement().getPrimaryDir().toInt());
    		buf.putBits(3, player.getMovement().getSecondaryDir().toInt());
    		buf.putBit(player.getActions().isSyncRequired());
    	} else if (player.getMovement().isWalking()) {
    		buf.putBit(true).putBits(2, 1);
    		buf.putBits(3, player.getMovement().getPrimaryDir().toInt());
    		buf.putBit(player.getActions().isSyncRequired());
    	} else if (player.getActions().isSyncRequired()) {
    		buf.putBit(true).putBits(2, 0);
    	} else {
    		buf.putBit(false);
    	}
    }
    
	public void updatePlayerMovement(Buffer buf, Player otherPlayer) {
        if (otherPlayer.getMovement().isStanding()) {
        	if (otherPlayer.getActions().isSyncRequired()) {
        		buf.putBit(true).putBits(2, 0);
        	} else {
        		buf.putBit(false);
        	}
        } else if (otherPlayer.getMovement().isWalking()) {
        	buf.putBit(true).putBits(2, 1);
        	buf.putBits(3, otherPlayer.getMovement().getPrimaryDir().toInt());
        	buf.putBit(otherPlayer.getActions().isSyncRequired());
        } else {
        	buf.putBit(true).putBits(2, 2);
        	buf.putBits(3, otherPlayer.getMovement().getPrimaryDir().toInt());
        	buf.putBits(3, otherPlayer.getMovement().getSecondaryDir().toInt());
        	buf.putBit(otherPlayer.getActions().isSyncRequired());
        }
    }
}
