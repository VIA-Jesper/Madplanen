package dk.ipfortify.madplanen.util.converters;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/*

        Displays the usage of junit


 */

public class WeightConverterJUnitTest {

    @Test
    public void literToMililiter() {
        // Arrange
        float liter = 4;
        float expectedML = 4000;
        float actual;
        // Act
        actual = WeightConverter.literToMililiter(liter);

        // assert
        Assert.assertEquals(expectedML, actual, 0.001f);


    }

    @Test
    public void mililiterToLiter() {

        // Arrange
        float ml = 4500;
        float expected = 4.5f;
        float actual;
        // Act
        actual = WeightConverter.mililiterToLiter(ml);

        // assert
        Assert.assertEquals(expected, actual, 0.001f);


    }

    @Test
    public void kilogramToGram() {

        // Arrange
        float kg = 7.3f;
        float expected = 7300f;
        float actual;
        // Act
        actual = WeightConverter.kilogramToGram(kg);

        // assert
        Assert.assertEquals(expected, actual, 0.001f);


    }

    @Test
    public void gramToKilogram() {

        // Arrange
        float g = 3339f;
        float expected = 3.339f;
        float actual;
        // Act
        actual = WeightConverter.gramToKilogram(g);

        // assert
        Assert.assertEquals(expected, actual, 0.001f);


    }
}