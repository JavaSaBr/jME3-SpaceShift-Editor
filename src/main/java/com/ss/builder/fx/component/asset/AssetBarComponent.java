package com.ss.builder.fx.component.asset;

import com.ss.builder.fx.Icons;
import com.ss.builder.fx.event.FxEventManager;
import com.ss.builder.fx.event.impl.RequestedRefreshAssetEvent;
import com.ss.builder.fx.Icons;
import com.ss.builder.fx.event.FxEventManager;
import com.ss.builder.fx.event.impl.RequestedRefreshAssetEvent;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * The toolbar of the {@link AssetComponent} with actions.
 *
 * @author JavaSaBr.
 */
public class AssetBarComponent extends HBox {

    private static final FxEventManager FX_EVENT_MANAGER = FxEventManager.getInstance();

    public AssetBarComponent() {

        var refreshAction = new Button();
        refreshAction.setGraphic(new ImageView(Icons.REFRESH_18));
        refreshAction.setOnAction(event -> FX_EVENT_MANAGER.notify(new RequestedRefreshAssetEvent()));

        FxUtils.addChild(this, refreshAction);
    }
}
