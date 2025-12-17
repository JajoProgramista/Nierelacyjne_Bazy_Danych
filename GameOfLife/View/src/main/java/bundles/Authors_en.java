package bundles;

import java.util.ListResourceBundle;

public class Authors_en extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"authors", "Authors:\nPiotr Matuszczyk, Tymon Kowalewsky"}
        };
    }
}
