package eg.net.game.out.codec;

import eg.model.Animation;
import eg.model.Charactor;
import eg.model.Coordinate;
import eg.model.Effect;
import eg.model.ForceChatMessage;
import eg.model.Hit;
import eg.model.npc.NpcType;
import eg.model.player.Player;
import eg.model.sync.SyncBlock;
import eg.model.sync.SyncBlockSet;
import eg.model.sync.SyncSection;
import eg.model.sync.SyncStatus;
import eg.net.game.AbstractGamePacketEncoder;
import eg.net.game.GamePacket;
import eg.net.game.out.NpcSyncPacket;
import eg.util.io.Buffer;
import eg.util.io.Buffers;

public final class NpcSyncPacketEncoder implements AbstractGamePacketEncoder<NpcSyncPacket> {
	
	@Override
	public GamePacket encode(NpcSyncPacket packet) throws Exception {
		
		Buffer buf = new Buffer();
		buf.beginBitAccess();
		
		Buffer payloadBuf = Buffers.allocate();
		
		buf.putBits(8, packet.getLocalNpcCount());
		
		for (SyncSection sec : packet.getSections()) {
			putSection(sec, buf, payloadBuf, packet.getOrigin());
		}
		
		if (payloadBuf.getPosition() != 0) {
			buf.putBits(14, 16383);
			buf.endBitAccess();
			buf.putBytes(payloadBuf.getData(), 0, payloadBuf.getPosition());
		} else {
			buf.endBitAccess();
		}
		Buffers.release(payloadBuf);
		
		return new GamePacket(65, buf.getData(), buf.getPosition());
	}
	
	private void putSection(SyncSection sec, Buffer buf, Buffer payloadBuf, Coordinate origin) {
		
		SyncStatus status = sec.getStatus();
		SyncBlockSet set = sec.getBlockSet();
		
		switch (status.getType()) {
		
		case REMOVAL:
			buf.putBit(true).putBits(2, 3);
			break;
			
		case NPC_ADDITION:
			Coordinate coordinate = ((SyncStatus.NpcAddition) status).getCoordinate();
			buf.putBits(14, ((SyncStatus.NpcAddition) status).getIndex());
			buf.putBits(5, coordinate.getY() - origin.getY());
			buf.putBits(5, coordinate.getX() - origin.getX());
			buf.putBit(false);
			buf.putBits(12, ((SyncStatus.NpcAddition) status).getNpcType().getId());
			buf.putBit(set.size() != 0);
			break;
			
		case RUN:
			buf.putBit(true).putBits(2, 2);
			buf.putBits(3, ((SyncStatus.Run) status).getPrimaryDirection().toInt());
			buf.putBits(3, ((SyncStatus.Run) status).getSecondaryDirection().toInt());
			buf.putBit(set.size() != 0);
			break;
			
		case WALK:
			buf.putBit(true).putBits(2, 1);
			buf.putBits(3, ((SyncStatus.Walk) status).getDirection().toInt());
			buf.putBit(set.size() != 0);
			break;
			
		case STAND:
			if (set.size() != 0) {
				buf.putBit(true).putBits(2, 0);
			} else {
				buf.putBit(false);
			}
			break;
		
		default:
			throw new IllegalArgumentException("Unknown sync status type.");
		}
		
		if (set.size() != 0) {
			
			int config = (set.contains(SyncBlock.Type.ANIMATION) ? 0b1_0000 : 0)
					| (set.contains(SyncBlock.Type.PRIMARY_HIT) ? 0b1000 : 0)
					| (set.contains(SyncBlock.Type.EFFECT) ? 0b1000_0000 : 0)
					| (set.contains(SyncBlock.Type.INTERACT) ? 0b10_0000 : 0)
					| (set.contains(SyncBlock.Type.FORCE_CHAT_MESSAGE) ? 0b1 : 0)
					| (set.contains(SyncBlock.Type.SECONDARY_HIT) ? 0b100_0000 : 0)
					| (set.contains(SyncBlock.Type.TRANSFORM) ? 0b10 : 0)
					| (set.contains(SyncBlock.Type.TURN) ? 0b100 : 0);
			
			payloadBuf.putByte(config);
			
			if (set.contains(SyncBlock.Type.ANIMATION)) {
				SyncBlock.Animation block = set.get(SyncBlock.Type.ANIMATION);
				Animation animation = block.getAnimation();
				payloadBuf.putLeShort(animation != null ? animation.getId() : 0xffff);
				payloadBuf.putByte(animation != null ? animation.getDelay() : 0);
			}
			
			if (set.contains(SyncBlock.Type.PRIMARY_HIT)) {
				SyncBlock.PrimaryHit block = set.get(SyncBlock.Type.PRIMARY_HIT);
				Hit hit = block.getHit();
				payloadBuf.putAddedByte(hit.getDamage());
				payloadBuf.putNegatedByte(hit.getType().toInt());
				payloadBuf.putAddedByte(block.getHealthLeft());
				payloadBuf.putByte(block.getHealthTotal());
			}
			
			if (set.contains(SyncBlock.Type.EFFECT)) {
				SyncBlock.Effect block = set.get(SyncBlock.Type.EFFECT);
				Effect effect = block.getEffect();
				payloadBuf.putShort(effect != null ? effect.getId() : 0xffff);
				payloadBuf.putShort(effect != null ? effect.getHeight() : 0);
				payloadBuf.putShort(effect != null ? effect.getDelay() : 0);
			}
			
			if (set.contains(SyncBlock.Type.INTERACT)) {
				SyncBlock.Interact block = set.get(SyncBlock.Type.INTERACT);
				Charactor target = block.getTarget();
				int index = target != null ? target.getIndex() : 0xffff;
				if (target instanceof Player) {
					index += 0x8000;
				}
				payloadBuf.putShort(index);
			}
			 
			if (set.contains(SyncBlock.Type.FORCE_CHAT_MESSAGE)) {
				SyncBlock.ForceChatMessage block = set.get(SyncBlock.Type.FORCE_CHAT_MESSAGE);
				ForceChatMessage forceChatMessage = block.getForceChatMessage();
				// TODO disallow showInChatbox for npcs
				if (forceChatMessage != null) {
					payloadBuf.putLine(forceChatMessage.getMessage());
				} else {
					payloadBuf.putByte('\n');
				}
			}
			
			if (set.contains(SyncBlock.Type.SECONDARY_HIT)) {
				SyncBlock.SecondaryHit block = set.get(SyncBlock.Type.SECONDARY_HIT);
				Hit hit = block.getHit();
				payloadBuf.putNegatedByte(hit.getDamage());
				payloadBuf.putSubtractedByte(hit.getType().toInt());
				payloadBuf.putSubtractedByte(block.getHealthLeft());
				payloadBuf.putNegatedByte(block.getHealthTotal());
			}
			 
			if (set.contains(SyncBlock.Type.TRANSFORM)) {
				SyncBlock.Transform block = set.get(SyncBlock.Type.TRANSFORM);
				NpcType npcType = block.getNpcType(); // TODO do something if type is null?
				payloadBuf.putAddedLeShort(npcType.getId());
			}
			
			if (set.contains(SyncBlock.Type.TURN)) {
				SyncBlock.Turn block = set.get(SyncBlock.Type.TURN);
				Coordinate target = block.getTarget();
				int x = 0;
				int y = 0;
				if (target != null) {
					x = target.getX() << 1 | 1;
					y = target.getY() << 1 | 1;
				}
				payloadBuf.putLeShort(x);
				payloadBuf.putLeShort(y);
			}
		}
	}
}
