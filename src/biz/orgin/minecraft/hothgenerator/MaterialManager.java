package biz.orgin.minecraft.hothgenerator;

import me.zhehe.MagicIdHandler;
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
            int id;
            try {
                id = material.getId();
            } catch(Exception ex) {
                id = 0;
            }
            return id;
        }
        
        public static Material toMaterial(int id) {
            BlockData data = MagicIdHandler.fromId(id, 0);
            if(data == null) return Material.AIR;
            return data.getMaterial();
        }
}
