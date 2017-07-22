package williamle.drones.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import williamle.drones.entity.EntityDrone;

public abstract interface IItemInteractDrone
{
  public abstract EnumActionResult interactWithDrone(World paramWorld, EntityDrone paramEntityDrone, EntityPlayer paramEntityPlayer, Vec3d paramVec3d, ItemStack paramItemStack, EnumHand paramEnumHand);
}


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\IItemInteractDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */