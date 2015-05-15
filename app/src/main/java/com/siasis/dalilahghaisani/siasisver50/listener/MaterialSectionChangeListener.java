package com.siasis.dalilahghaisani.siasisver50.listener;

import com.siasis.dalilahghaisani.siasisver50.head.MaterialHeadItem;
import com.siasis.dalilahghaisani.siasisver50.menu.item.MaterialSection;

/**
 * Created by marc on 08.03.2015.
 */
public interface MaterialSectionChangeListener {
    public void onBeforeChangeSection(MaterialSection newSection);

    public void onAfterChangeSection(MaterialSection newSection);
}
