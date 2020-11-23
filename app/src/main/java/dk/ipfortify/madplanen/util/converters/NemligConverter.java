package dk.ipfortify.madplanen.util.converters;

import java.util.ArrayList;
import java.util.List;

import dk.ipfortify.madplanen.data.model.madplanen.RecipeIngredient;
import dk.ipfortify.madplanen.data.model.madplanen.RecipeInstruction;
import dk.ipfortify.madplanen.data.model.madplanen.Recipe;
import dk.ipfortify.madplanen.data.model.nemlig.NemligContent;
import dk.ipfortify.madplanen.data.model.nemlig.NemligIngredient;
import dk.ipfortify.madplanen.data.model.nemlig.NemligIngredientGroup;
import dk.ipfortify.madplanen.data.model.nemlig.NemligSubElement;


public class NemligConverter {

    @SuppressWarnings("unchecked")
    public static List<Recipe> convertToRecipe(Object content) {
        List<Recipe> recipes = new ArrayList<>();
        for (NemligContent nc : (List<NemligContent>) content) {
            if (nc.subElements != null && nc.subElements.size() > 0) {
                for (NemligSubElement se : nc.subElements) {
                    if (se.recipe != null) {
                            Recipe recipe = new Recipe(
                                    se.recipe.name,
                                    se.recipe.url,
                                    se.recipe.primaryImage,
                                    se.recipe.numberOfPersons,
                                    null,
                                    se.recipe.totalTime,
                                    null,
                                    null
                            );
                            recipes.add(recipe);
                    }
                }
            }
        }
        return recipes;
    }
    @SuppressWarnings("unchecked")
    public static Recipe convertToDetailedRecipe(Object content) {
        Recipe recipe = null;
        for (NemligContent nc : (List<NemligContent>) content) {
            List<RecipeIngredient> ingredients = new ArrayList<>();

            // add all ingredients to a list
            for (NemligIngredientGroup nig : nc.ingredientGroups){
                for (NemligIngredient ni : nig.ingredients) {
                    ingredients.add(convertNemligIngredientToIngredient(ni));
                }
            }
            // add instructions
            List<RecipeInstruction> instructions = new ArrayList<>(convertStringToInstruction(nc.instructions));

            // create pojo
            recipe = new Recipe(
                    nc.header,
                    null,
                    nc.media.get(0).url,
                    nc.numberOfPersons,
                    nc.workTime,
                    nc.totalTime,
                    ingredients,
                    instructions
            );
        }
        return recipe;
    }

    private static List<RecipeInstruction> convertStringToInstruction(String input){
        List<RecipeInstruction> instructions = new ArrayList<>();
        RecipeInstruction instruction = new RecipeInstruction();
        instruction.step = input;
        instructions.add(instruction);
        return instructions;
    }

    // The api is not consistent with provided values. Sometimes there is trailing whitespace on items, others '.' is replaced with ',' and so forth.
    private static RecipeIngredient convertNemligIngredientToIngredient(NemligIngredient ingredient) {
        RecipeIngredient newIngredient = new RecipeIngredient();
        newIngredient.nameOfIngredient = ingredient.text.trim(); // apparently the api provides whitespace 'sometimes' in front and end of a certain name like " bacon "...

        if (ingredient.measureMassInLiter != null && !((String)ingredient.measureMassInLiter).isEmpty()) {
            ingredient.measureMassInLiter = ((String) ingredient.measureMassInLiter).replace(',', '.');
            newIngredient.amountInMililiter = WeightConverter.literToMililiter(Float.parseFloat((String) ingredient.measureMassInLiter));
        }

        if (ingredient.amount != null && !ingredient.amount.isEmpty())
            newIngredient.amountNumber = Float.parseFloat(ingredient.amount);

        newIngredient.amountType = 0;
        for (RecipeAmountType t : RecipeAmountType.values()) {
            if (ingredient.unit.toLowerCase().startsWith(t.name())) {
                newIngredient.amountType = t.ordinal();
                break;
            }
        }

        if (ingredient.measureWeightInGram != null && !ingredient.measureWeightInGram.isEmpty())
            newIngredient.amountInGrams = Float.parseFloat(ingredient.measureWeightInGram);

        return newIngredient;
    }


}
