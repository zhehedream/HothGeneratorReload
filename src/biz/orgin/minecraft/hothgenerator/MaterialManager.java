package biz.orgin.minecraft.hothgenerator;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.item.ItemType;
import com.sk89q.worldedit.world.registry.LegacyMapper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * Handles converting materials between ID's an instances of the Material class.
 * Since all type ID functions have been deprecated in bukkit then this
 * plugin must handle ID's on its own.
 * Unknown ID's and Materials are treated with the deprecated functions for now.
 * Unfortunately any new block must be added to this class..
 * @author orgin
 *
 */
public class MaterialManager
{
        public static int toID(Material material) {
            if(material.isBlock()) {
                BlockData data = material.createBlockData();
                BlockState state = BukkitAdapter.adapt(data);
                LegacyMapper lm = LegacyMapper.getInstance();
                int[] res = lm.getLegacyFromBlock(state);
                if(res == null) return 0;
                return res[0];
            } else {
                LegacyMapper lm = LegacyMapper.getInstance();
                ItemType it = BukkitAdapter.asItemType(material);
                int[] res = lm.getLegacyFromItem(it);
                if(res == null) return 0;
                return res[0];
            }
        }
        
        public static Material toMaterial(int id) {
            LegacyMapper lm = LegacyMapper.getInstance();
            BlockState state = lm.getBlockFromLegacy(id);
            if(state == null) return Material.AIR;
            BlockData data = BukkitAdapter.adapt(state);
            return data.getMaterial();
        }
}
