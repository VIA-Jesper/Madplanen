package dk.ipfortify.madplanen.util.converters;

public class WeightConverter {

    /*
        Converts from ML og Gram to Liter or Kg
     */
    public static float literToMililiter(float liter) {
        return liter * 1000;
    }

    public static float mililiterToLiter(float miliLiter) {
        return miliLiter / 1000;
    }

    public static float kilogramToGram(float kilogram) {
        return kilogram * 1000;
    }

    public static float gramToKilogram(float gram) {
        return gram / 1000;
    }


}
