package bundles;

import java.util.ListResourceBundle;

public class Authors_pl extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"authors", "Autorzy:\nPiotr Matuszczyk, Tymon Kowalewsky"}
        };
    }
}
