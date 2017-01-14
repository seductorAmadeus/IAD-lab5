package lab5.resources;

import java.util.*;

public class loc_data_en extends ListResourceBundle {
    protected Object[][] getContents() {
        return new Object[][]{
                {"change_radius", "Choose the value of a radius:: "},
                {"change_x_point", "Choose the X-coordinate of a point:"},
                {"change_y_point", "Choose the Y-coordinate of a point:"},
                {"change_label_with_coordinates", "Set point"},
        };
    }
}