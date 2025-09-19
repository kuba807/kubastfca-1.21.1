package net.kuba807.kubastfca.common.compat.jei.category;

import mezz.jei.api.recipe.RecipeType;
import net.kuba807.kubastfca.common.item.KubastfcaItems;
import net.dries007.tfc.compat.jei.category.PotRecipeCategory;
import net.minecraft.world.item.crafting.Ingredient;

import net.minecraft.client.gui.GuiGraphics;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.dries007.tfc.common.recipes.PotRecipe;

public class DumplingPotRecipeCategory extends PotRecipeCategory<PotRecipe> {
    public DumplingPotRecipeCategory(RecipeType<PotRecipe> type, IGuiHelper helper)
    {
        super(type, helper,175, 70);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PotRecipe recipe, IFocusGroup focuses)
    {
        int i = 0;
        for (Ingredient ingredient : recipe.getItemIngredients())
        {
            if (!ingredient.isEmpty())
            {
                IRecipeSlotBuilder inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 6 + 20 * i, 6);
                inputSlot.addItemStack(KubastfcaItems.RAW_DUMPLING.get().getDefaultInstance());
                inputSlot.setBackground(slot, -1, -1);
                i++;
            }
        }

        int ingredientCount = 0;
        for (Ingredient ingredient : recipe.getItemIngredients())
        {
            if (!ingredient.isEmpty())
                ingredientCount++;
        }
        for( i=0 ; i<ingredientCount ;i++){

            switch (i) {
                case 0 :
                    IRecipeSlotBuilder outputItem = builder.addSlot(RecipeIngredientRole.OUTPUT, 126, 6);
                    outputItem.setBackground(slot, -1, -1);
                    outputItem.addItemStack(KubastfcaItems.COOKED_DUMPLING.get().getDefaultInstance());
                    break;

                case 1:
                    IRecipeSlotBuilder outputItem2 = builder.addSlot(RecipeIngredientRole.OUTPUT, 146, 6);
                    outputItem2.setBackground(slot, -1, -1);
                    outputItem2.addItemStack(KubastfcaItems.COOKED_DUMPLING.get().getDefaultInstance());
                    break;

                case 2:
                    IRecipeSlotBuilder outputItem3 = builder.addSlot(RecipeIngredientRole.OUTPUT, 126, 26);
                    outputItem3.setBackground(slot, -1, -1);
                    outputItem3.addItemStack(KubastfcaItems.COOKED_DUMPLING.get().getDefaultInstance());
                    break;
                case 3:
                    IRecipeSlotBuilder outputItem4 = builder.addSlot(RecipeIngredientRole.OUTPUT, 146, 26);
                    outputItem4.setBackground(slot, -1, -1);
                    outputItem4.addItemStack(KubastfcaItems.COOKED_DUMPLING.get().getDefaultInstance());
                    break;
                case 4:
                    IRecipeSlotBuilder outputItem5 = builder.addSlot(RecipeIngredientRole.OUTPUT, 136, 46);
                    outputItem5.setBackground(slot, -1, -1);
                    outputItem5.addItemStack(KubastfcaItems.COOKED_DUMPLING.get().getDefaultInstance());
                    break;
            }
    }
    }

    @Override
    public void draw(PotRecipe recipe, IRecipeSlotsView recipeSlots, GuiGraphics stack, double mouseX, double mouseY)
    {
        // fire
        fire.draw(stack, 27, 25);
        fireAnimated.draw(stack, 27, 25);
        // arrow
        arrow.draw(stack, 103, 26);
        arrowAnimated.draw(stack, 103, 26);
    }
}