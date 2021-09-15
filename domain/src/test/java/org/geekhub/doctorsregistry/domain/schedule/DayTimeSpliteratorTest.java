package org.geekhub.doctorsregistry.domain.schedule;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class DayTimeSpliteratorTest {

    private DayTimeSpliterator dayTimeSpliterator;

    @BeforeMethod
    private void setUp() {
        dayTimeSpliterator = new DayTimeSpliterator();
    }

    @Test
    public void returns_empty_List_when_given_empty_List() {
        List<DayTime> result = dayTimeSpliterator.splitToDayTime(Collections.emptyList());
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void returns_list_of_the_same_size() {
        List<String> given = List.of("MONDAY&08:00", "MONDAY&08:20", "TUESDAY&08:00");
        List<DayTime> actual = dayTimeSpliterator.splitToDayTime(given);
        Assert.assertEquals(actual.size(), given.size());
    }

    @DataProvider(name = "throws_IllegalArgumentException_when_contains_not_valid_entry_parameters")
    private Object[][] throws_IllegalArgumentException_when_contains_not_valid_entry_parameters() {
        return new Object[][]{
            {""},
            {"&"},
            {"MONDAY08:00"},
            {"MONDAY&&08:00"},
            {"MONDAY$08:00"},
            {"&08:00"},
            {"MONDAY&"}
        };
    }

    @Test(dataProvider = "throws_IllegalArgumentException_when_contains_not_valid_entry_parameters")
    public void throws_IllegalArgumentException_when_contains_not_valid_entry(String givenString) {
        List<String> givenList = List.of(givenString);
        Assertions.assertThatCode(() -> dayTimeSpliterator.splitToDayTime(givenList))
            .isInstanceOf(IllegalArgumentException.class);
    }
}