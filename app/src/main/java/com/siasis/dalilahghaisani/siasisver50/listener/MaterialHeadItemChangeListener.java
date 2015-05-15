package com.siasis.dalilahghaisani.siasisver50.listener;

import com.siasis.dalilahghaisani.siasisver50.head.MaterialHeadItem;

public interface MaterialHeadItemChangeListener {

    public void onBeforeChangeHeadItem(MaterialHeadItem newHeadItem);

    public void onAfterChangeHeadItem(MaterialHeadItem newHeadItem);
}
