package org.codinjutsu.tools.nosql.commons.view.add;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;

import static org.codinjutsu.tools.nosql.commons.model.internal.json.JsonHelperKt.convert;

public class JsonFieldArrayWrapper extends JTextFieldWrapper {

    @Override
    public DatabaseElement getValue() {
        return convert(new Gson().fromJson(component.getText(), JsonArray.class));
    }
}
