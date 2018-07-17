package com.ss.editor.ui.control.property.impl;

import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.property.PropertyControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.editor.ui.util.DynamicIconSupport;
import com.ss.rlib.fx.util.FxControlUtils;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of the {@link PropertyControl} to edit an elements from scene.
 *
 * @param <C> the type of a change consumer.
 * @param <D> the type of an editing object.
 * @param <T> the type of an editing property.
 * @author JavaSaBr
 */
public class ElementPropertyControl<C extends ChangeConsumer, D, T> extends PropertyControl<C, D, T> {

    protected static final String NO_ELEMENT = Messages.ABSTRACT_ELEMENT_PROPERTY_CONTROL_NO_ELEMENT;

    /**
     * The type of an element.
     */
    @NotNull
    protected final Class<T> type;

    /**
     * The label with name of the element.
     */
    @NotNull
    protected final Label elementLabel;

    public ElementPropertyControl(
            @NotNull Class<T> type,
            @Nullable T propertyValue,
            @NotNull String propertyName,
            @NotNull C changeConsumer
    ) {
        super(propertyValue, propertyName, changeConsumer);
        this.type = type;
        this.elementLabel = new Label(NO_ELEMENT);
    }

    @Override
    @FxThread
    protected void createControls(@NotNull HBox container) {
        super.createControls(container);

        elementLabel.prefWidthProperty()
            .bind(container.widthProperty());

        var changeButton = new Button();
        changeButton.setGraphic(new ImageView(Icons.ADD_16));
        changeButton.setOnAction(event -> addElement());

        var removeButton = new Button();
        removeButton.setGraphic(new ImageView(Icons.REMOVE_12));
        removeButton.setOnAction(event -> removeElement());
        removeButton.disableProperty()
            .bind(elementLabel.textProperty().isEqualTo(NO_ELEMENT));

        FxUtils.addClass(container,
                    CssClasses.TEXT_INPUT_CONTAINER,
                    CssClasses.ABSTRACT_PARAM_CONTROL_INPUT_CONTAINER)
            .addClass(elementLabel,
                    CssClasses.ABSTRACT_PARAM_CONTROL_ELEMENT_LABEL)
            .addClass(changeButton, removeButton,
                    CssClasses.FLAT_BUTTON,
                    CssClasses.INPUT_CONTROL_TOOLBAR_BUTTON);

        FxControlUtils.onAction(changeButton, this::addElement);
        FxControlUtils.onAction(removeButton, this::removeElement);

        FxUtils.addChild(container, elementLabel, changeButton, removeButton);

        DynamicIconSupport.addSupport(changeButton, removeButton);
    }

    /**
     * Show a dialog to choose an element.
     */
    @FxThread
    protected void addElement() {
    }

    /**
     * Remove the current element.
     */
    @FxThread
    protected void removeElement() {
        changed(null, getPropertyValue());
    }

    @Override
    @FxThread
    protected void reloadImpl() {
        elementLabel.setText(getElementText());
        super.reloadImpl();
    }

    /**
     * Get the current element's text.
     *
     * @return the current element's text.
     */
    @FxThread
    protected @NotNull String getElementText() {
        throw new UnsupportedOperationException();
    }
}
