package net.kuba807.kubastfca.common.block;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Constants;
import net.dries007.tfc.client.IHighlightHandler;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.devices.DeviceBlock;
import net.dries007.tfc.util.Helpers;
import net.kuba807.kubastfca.common.blockentities.CeramicChurnBlockEntity;
import net.kuba807.kubastfca.common.blockentities.KubastfcaBlockEntities;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;


import static net.kuba807.kubastfca.common.blockentities.CeramicChurnBlockEntity.SLOT_INPUT;
import static net.kuba807.kubastfca.common.blockentities.CeramicChurnBlockEntity.SLOT_OUTPUT;

public class CeramiChurnBlock extends DeviceBlock implements IHighlightHandler
{
    private static final VoxelShape BASE_SHAPE = box(5.0D, 0.0D, 5.0D, 11.0D, 12.0D, 11.0D);
    private static final AABB BASE_AABB = BASE_SHAPE.bounds().inflate(0.01D);

    //private static final VoxelShape HANDSTONE_SHAPE = box(7.0D, 10.0D, 7.0D, 8.0D, 16.0D, 8.0D);
    //private static final AABB HANDSTONE_AABB = HANDSTONE_SHAPE.bounds().inflate(0.01D);


    private static final VoxelShape HANDLE_SHAPE = box(7.0D, 10.0D, 7.0D, 8.0D, 16.0D, 8.0D);
    private static final AABB HANDLE_AABB = HANDLE_SHAPE.bounds().inflate(0.01D);
    private static final Vec3 HANDLE_SHAPE_CENTER = HANDLE_SHAPE.bounds().getCenter();

    private static final VoxelShape INPUT_SLOT_SHAPE = box(6.0D, 13.76D, 6.0D, 10.0D, 16.24D, 10.0D);
    private static final AABB INPUT_SLOT_AABB = INPUT_SLOT_SHAPE.bounds().inflate(0.01D);

    private static final VoxelShape FULL_SHAPE = Shapes.join(Shapes.or(BASE_SHAPE,  HANDLE_SHAPE), INPUT_SLOT_SHAPE, BooleanOp.ONLY_FIRST);
    private static final VoxelShape COLLISION_FULL_SHAPE = Shapes.or(BASE_SHAPE);

    private static SelectionPlace getPlayerSelection(BlockGetter level, BlockPos pos, Player player, BlockHitResult result)
    {
        final CeramicChurnBlockEntity ceramic_butter_churn = level.getBlockEntity(pos, KubastfcaBlockEntities.CERAMIC_BUTTER_CHURN.get()).orElse(null);
        if (ceramic_butter_churn != null)
        {
            final IItemHandler inventory = ceramic_butter_churn.getInventory();
            final ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
            final Vec3 hit = result.getLocation();
                if (!ceramic_butter_churn.isGrinding() && HANDLE_AABB.move(pos).contains(hit))
                {
                    return SelectionPlace.HANDLE;
                }
                else if (!ceramic_butter_churn.isGrinding() && !held.isEmpty() || !inventory.getStackInSlot(SLOT_INPUT).isEmpty() && INPUT_SLOT_AABB.move(pos).contains(hit))
                {
                    return SelectionPlace.INPUT_SLOT;
                }
        }
        return SelectionPlace.BASE;
    }

    private static ItemInteractionResult insertOrExtract(Level level, CeramicChurnBlockEntity quern, IItemHandler inventory, Player player, ItemStack stack, int slot)
    {
        if (!stack.isEmpty())
        {
            player.setItemInHand(InteractionHand.MAIN_HAND, inventory.insertItem(slot, stack, false));
        }
        quern.setAndUpdateSlots(slot);
        quern.markForSync();
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }


    public CeramiChurnBlock(ExtendedProperties properties)
    {
        super(properties, InventoryRemoveBehavior.DROP);
    }


    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
    {
        if (level.getBlockEntity(pos) instanceof CeramicChurnBlockEntity ceramic_butter_churn)
        {
            final float rotationSpeed = ceramic_butter_churn.getRotationSpeed();
            if (rotationSpeed != 0f && HANDLE_AABB.move(pos).contains(entity.position()) && !BASE_AABB.contains(entity.position()))
            {
                Helpers.rotateEntity(level, entity, HANDLE_SHAPE_CENTER.add(pos.getX(), pos.getY(), pos.getZ()), -rotationSpeed * Constants.RAD_TO_DEG);
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        final CeramicChurnBlockEntity ceramic_butter_churn = level.getBlockEntity(pos, KubastfcaBlockEntities.CERAMIC_BUTTER_CHURN.get()).orElse(null);
        if (ceramic_butter_churn != null && !ceramic_butter_churn.isGrinding())
        {
            final IItemHandler inventory = ceramic_butter_churn.getInventory();
            final ItemStack heldStack = player.getItemInHand(hand);
            final SelectionPlace selection = getPlayerSelection(level, pos, player, hitResult);
            return switch (selection)
            {
                case HANDLE -> attemptGrind(level, pos, ceramic_butter_churn);
                case INPUT_SLOT -> insertOrExtract(level, ceramic_butter_churn, inventory, player, heldStack, SLOT_INPUT);
                case BASE -> insertOrExtract(level, ceramic_butter_churn, inventory, player, ItemStack.EMPTY, SLOT_OUTPUT);
            };
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private ItemInteractionResult attemptGrind(Level level, BlockPos pos, CeramicChurnBlockEntity ceramic_butter_churn)
    {
        return !ceramic_butter_churn.isConnectedToNetwork() && ceramic_butter_churn.startGrinding()
                ? ItemInteractionResult.sidedSuccess(level.isClientSide)
                : ItemInteractionResult.FAIL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return  FULL_SHAPE ;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return COLLISION_FULL_SHAPE;
    }

    @Override
    public boolean drawHighlight(Level level, BlockPos pos, Player player, BlockHitResult rayTrace, PoseStack poseStack, MultiBufferSource buffers, Vec3 renderPos)
    {
        SelectionPlace selection = getPlayerSelection(level, pos, player, rayTrace);
        if (selection != SelectionPlace.BASE)
        {
            IHighlightHandler.drawBox(poseStack, selection.shape, buffers, pos, renderPos, 1.0F, 0.0F, 0.0F, 0.4F);
            return true;
        }
        return false;
    }

    /**
     * Just a helper enum to figure out where player is looking at
     * Used to draw selection boxes + handle interaction
     */
    private enum SelectionPlace
    {
        HANDLE(HANDLE_SHAPE),
     //   HANDSTONE(HANDSTONE_SHAPE),
        INPUT_SLOT(INPUT_SLOT_SHAPE),
        BASE(BASE_SHAPE);

        final VoxelShape shape;

        SelectionPlace(VoxelShape shape)
        {
            this.shape = shape;
        }
    }
}